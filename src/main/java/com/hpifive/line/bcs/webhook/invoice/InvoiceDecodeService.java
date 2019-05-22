package com.hpifive.line.bcs.webhook.invoice;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.common.HttpClientUtil;
import com.hpifive.line.bcs.webhook.entities.InvoiceDetailEntity;
import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;
import com.hpifive.line.bcs.webhook.entities.config.InvoiceStatus;
import com.hpifive.line.bcs.webhook.exception.CustomException;

@Service
public class InvoiceDecodeService {

	private static final String INV_NUM = "invNum";
	private static final String INV_TERM = "invTerm";
	private static final String RANDOM_NUMBER = "randomNumber";

	private static final int QR_CODE = 0;
	private static final int CODE_39 = 1;

	private static final int MSG_INVOICE_SUCCESS = 0;
	private static final int MSG_INVOICE_NOT_FOUND = 1;
	private static final int MSG_INVOICE_PARAMETER_ERROR = 2;
	private static final int MSG_INVOICE_INTERNAL_ERROR = 3;
	
	private DateFormat yyyMMDf = new SimpleDateFormat("yyyMM");
	private DateFormat yyyMMddDf = new SimpleDateFormat("yyyMMdd");
	private DateFormat df = new SimpleDateFormat("yyyyMMdd hh:mm:ss");

	private static final Logger logger = LoggerFactory.getLogger(InvoiceDecodeService.class);

	/**
	 * 為發票做 decode，取得發票訊息
	 * @param image 圖片
	 * @return 返回 Invoice Entity
	 */
	public InvoiceEntity decode(BufferedImage image) {
		InvoiceEntity invoice = null;
		try {
			invoice = this.decodeByQRCode(image, false);
			if (invoice != null) {
				return invoice;
			}
		} catch (Exception e) {
			logger.info("decodeByQRCode - false can't decode");
		}
		try {
			invoice = this.decodeByQRCode(image, true);
			if (invoice != null) {
				return invoice;
			}
		} catch (Exception e) {
			logger.info("decodeByQRCode - true can't decode");
		}
		try {
			invoice = this.decodeByCode39(image, false);
			if (invoice != null) {
				return invoice;
			}
		} catch (Exception e) {
			logger.info("decodeByCode39 - false can't decode");
		}
		try {
			invoice = this.decodeByCode39(image, true);
			if (invoice != null) {
				return invoice;
			}
		} catch (Exception e) {
			logger.info("decodeByCode39 - true can't decode");
		}
		return invoice;
	}

	/**
	 * 進行解析 QRCode
	 * @param image 發票
	 * @param isPureBarcode 是否為 Barcode
	 * @return Invoice Entity
	 * @throws NotFoundException 
	 * @throws Exception
	 */
	private InvoiceEntity decodeByQRCode(BufferedImage image, boolean isPureBarcode) throws NotFoundException {
		Result[] decodeQRCodeResults = BarcodeDetector.decodeQRCode(image, isPureBarcode);
		InvoiceEntity invoice = this.getInvoice(decodeQRCodeResults);
		if(invoice != null) {
			return invoice; 
		}
		return null;
	}

	/**
	 * 進行解析 Code39
	 * @param image 發票
	 * @param isPureBarcode 是否為 Barcode
	 * @return Invoice Entity
	 * @throws NotFoundException 
	 * @throws Exception
	 */
	private InvoiceEntity decodeByCode39(BufferedImage image, boolean isPureBarcode) throws NotFoundException {
		Result[] decodeCode39Results = BarcodeDetector.decodeCode39(image, isPureBarcode);
		logger.debug("length: {}", decodeCode39Results.length);
		InvoiceEntity invoice = this.getInvoice(decodeCode39Results);
		if(invoice != null) {
			return invoice;
		}
		return null;
	}
	
	/**
	 * 取得發票內容，繼續流程
	 * @param decodeQRCodeResults 圖片轉回來的文字訊息
	 * @return Invoice Entity
	 * @throws Exception
	 */
	private InvoiceEntity getInvoice(Result[] decodeQRCodeResults) {
		for (Result decodeResult : decodeQRCodeResults) {
			logger.debug(decodeResult.getText());
			InvoiceEntity invoice = getInvoice(decodeResult.getText(), QR_CODE);
			if (invoice != null && !InvoiceStatus.DECODE_FAIL.toString().equals(invoice.getStatus())) {
				return invoice;
			}
		}
		return null;
	}

	/**
	 * 將發票轉為 Invoice Entity
	 * @param qrCodeText 發票訊息
	 * @param type 類別
	 * @return Invoice Entity
	 * @throws Exception
	 */
	private InvoiceEntity getInvoice(String qrCodeText, int qrcodeType) {
		InvoiceEntity invoice = null;
		ObjectNode param = this.decodeBarcodeText(qrCodeText, qrcodeType);
		if (param != null) {
			String invoiceNum = getText(param, INV_NUM);
			String invoiceTerm = getText(param, INV_TERM);
			String invoiceRand = getText(param, RANDOM_NUMBER);
			if (invoiceNum != null && invoiceTerm != null && invoiceRand != null) {
				invoice = new InvoiceEntity();
				invoice.setInvNum(invoiceNum);
				invoice.setInvTerm(invoiceTerm);
				invoice.setInvRandom(invoiceRand);
			}
		}
		return invoice;
	}

	/**
	 * 將訊息驗證，並返回 ObjectNode
	 * @param text 內容
	 * @param qrcodeType 類別
	 * @return
	 */
	private ObjectNode decodeBarcodeText(String text, Integer qrcodeType) {
		if (validateDecodeText(text, qrcodeType)) {
			switch(qrcodeType) {
			case QR_CODE:
				return this.decodeTypeIsQRCode(text);
			case CODE_39:
				return this.decodeTypeIsCode39(text);
			default:
				return decodeBarcodeText(text, QR_CODE); 
			}
		}
		return null;
	}
	
	/**
	 * 建立 QRCode 的 Node，QRCode 格式為 yyyMMdd
	 * @param text 發票內容
	 * @return ObjectNode
	 */
	private ObjectNode decodeTypeIsQRCode(String text) {
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put(INV_NUM, text.substring(0, 10));
		node.put(INV_TERM, text.substring(10, 17));
		node.put(RANDOM_NUMBER, text.substring(17, 21));
		return node;
	}
	
	/**
	 * 建立 Code39 的 Node，Code39 格式為 yyyMM
	 * @param text 發票內容
	 * @return ObjectNode
	 */
	private ObjectNode decodeTypeIsCode39(String text) {
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put(INV_TERM, text.substring(0, 5));
		node.put(INV_NUM, text.substring(5, 15));
		node.put(RANDOM_NUMBER, text.substring(15, 19));
		return node;
	}

	/**
	 * 驗證發票文字內容
	 * @param text 內容
	 * @param qrcodeType 碼的類別
	 * @return 是否驗證通過
	 */
	private boolean validateDecodeText(String text, Integer qrcodeType) {
		if (qrcodeType == QR_CODE && text != null && text.length() >= 21) {
			return this.validateQRCodeText(text);
		} else if (qrcodeType == CODE_39 && text != null && text.length() == 19) {
			return this.validateCode39Text(text);
		}
		return false;
	}
	
	/**
	 * 驗證 QRCode 的文字內容
	 * @param text 發票內容
	 * @return 是否驗證通過
	 */
	private boolean validateQRCodeText(String text) {
		String qrcodeText = text.substring(0, 21);
		Pattern pattern = Pattern.compile("[A-Z]{2}\\d{8}\\d{7}\\d{4}", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(qrcodeText);
		logger.debug("validateQRCodeText {} ", qrcodeText);
		return matcher.matches();
	}
	
	/**
	 * 驗證 Code39 的文字內容
	 * @param text 發票內容
	 * @return 是否驗證通過
	 */
	private boolean validateCode39Text(String text) {
		String qrcodeText = text.substring(0, 19);
		Pattern pattern = Pattern.compile("\\d{5}[A-Z]{2}\\d{8}\\d{4}", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(qrcodeText);
		logger.debug("validateCode39Text {}", qrcodeText);
		return matcher.matches();
	}

	/**
	 * 取得 node 中的資料
	 * @param node ObjectNode 
	 * @param key key
	 * @return 返回該資料
	 */
	private String getText(ObjectNode node, String key) {
		if (node.get(key) != null) {
			return node.get(key).asText();
		}
		return null;
	}

	/**
	 * 取得 node 中的資料
	 * @param node JsonNode
	 * @param key key 
	 * @return 返回該資料
	 */
	private String getText(JsonNode node, String key) {
		if (node.get(key) != null) {
			return node.get(key).asText();
		}
		return null;
	}
	
	public InvoiceEntity getInvoiceFromGov(InvoiceEntity inv) throws CustomException, IOException, ParseException {
		InvoiceEntity tempInvoice =  this.getInvoiceFromGov(inv.getInvNum(), inv.getInvTerm(), inv.getInvRandom());
		if (tempInvoice != null) {
			return tempInvoice;
		}
		return inv;
	}
	
	/**
	 * 透過發票的三個基本資訊，到財政部確認該發票的訊息
	 * @param invNum 發票號碼
	 * @param invTerm 發票年月
	 * @param randomNumber 發票隨機碼
	 * @return Invoice Entity
	 * @throws IOException 
	 * @throws CustomException 
	 * @throws ParseException 
	 * @throws Exception
	 */
	public InvoiceEntity getInvoiceFromGov(String invNum, String invTerm, String randomNumber) throws CustomException, IOException, ParseException {
		if (invNum != null && invTerm != null && randomNumber != null) {
			ObjectNode invoiceJson = checkInvoiceFromMinistryOfFinance(invNum, invTerm, randomNumber);
			if (invoiceJson != null) {
				InvoiceEntity invoice = parseInvoice(invoiceJson);
				invoice.setInvNum(invNum);
				invoice.setInvTerm(invTerm);
				invoice.setInvRandom(randomNumber);
				int validateResult = validateInvoiceResp(invoiceJson);
				if (MSG_INVOICE_SUCCESS != validateResult) {
					invoice.setStatus(InvoiceStatus.NOT_FOUND);
				}
				return invoice;
			}
		}
		return null;
	}
	
	/**
	 * 透過財政部的 api 確認該發票是否存在，並取的內容
	 * @param invNum 發票號碼
	 * @param invTerm 發票年月
	 * @param randomNumber 發票隨機碼
	 * @return ObjectNode
	 * @throws CustomException 
	 * @throws IOException 
	 * @throws Exception
	 */
	private ObjectNode checkInvoiceFromMinistryOfFinance(String invNum, String invTerm, String randomNumber) throws CustomException, IOException {
		invNum = (invNum != null ? invNum.toUpperCase() : invNum);
		invTerm = handleInvTerm(invTerm);
		String uri = getURL(invNum, invTerm, randomNumber);
		logger.info("uri= {}", uri);
		String result = this.sendHttpGetToMinistryOfFinance(uri);
		return (ObjectNode) (new ObjectMapper()).readTree(result);
	}
	
	/**
	 * 透過 http 發送請求給財政部
	 * @param uri 網址
	 * @return
	 * @throws CustomException
	 */
	private String sendHttpGetToMinistryOfFinance(String uri) throws CustomException {
		try {
			HttpClient httpClient = HttpClientUtil.generateClient();
			HttpGet requestGet = new HttpGet(uri);
			HttpResponse clientResponse = httpClient.execute(requestGet);
			int status = clientResponse.getStatusLine().getStatusCode();
			requestGet.releaseConnection();
			logger.info("status= {}", status);
			return readResponseToString(clientResponse, new StringBuilder());
		} catch (Exception e) {
			throw new CustomException(e);
		}
	}
	
	/**
	 * 將 response 資料讀取出來
	 * @param clientResponse 財政部返回的資料
	 * @param result 
	 * @return 返回資料為字串型態
	 * @throws IOException
	 */
	private String readResponseToString(HttpResponse clientResponse, StringBuilder result) throws IOException {
		if (clientResponse.getEntity() != null && clientResponse.getEntity().getContent() != null) {
			InputStream is = clientResponse.getEntity().getContent();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			result.append(buffer.lines().collect(Collectors.joining("\n")));
		}
		return result.toString();
	}

	/**
	 * 處理 發票圖片 格式
	 * @param dateStr 發票日期
	 * @return 正確的日期格式
	 */
	private String handleInvTerm(String dateStr) {
		String invTerm = null;
		Date date = null;
		if (dateStr != null) {
			date = this.parseOrginalalInvTermToDate(dateStr);
			if (date != null) {
				invTerm = this.parseInvDateToDoubleAndToString(date);
			}
		}
		return invTerm;
	}
	
	/**
	 * 將 發票圖片 解析完的日期作轉換，轉為 Date 型態
	 * @param dateStr 日期字串
	 * @return 
	 */
	private Date parseOrginalalInvTermToDate(String dateStr) {
		try {
			if (dateStr.length() == 5) {
				return yyyMMDf.parse(dateStr);
			} else if (dateStr.length() == 7) {
				return yyyMMddDf.parse(dateStr);
			}
		} catch (ParseException e) {
			logger.error("parseOrginalalInvTermToDate Error {}", e);
		}
		return null; 
	}
	
	/**
	 * 將 發票日期格式 轉為 雙月分，且轉字串型態
	 * @param date 發票日期
	 * @return
	 */
	private String parseInvDateToDoubleAndToString(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int mmInt = cal.get(Calendar.MONTH) + 1;
		if (mmInt % 2 == 1) {
			cal.add(Calendar.MONTH, 1);
		}
		return yyyMMDf.format(cal.getTime());
	}
	
	/**
	 * 將財政部的 url 經過 format，拼湊出新的 url 
	 * @param invNum 發票號碼
	 * @param invTerm 發票年月
	 * @param randomNumber 發票隨機碼
	 * @return 正確的 url
	 */
	private static String getURL(String invNum, String invTerm, String randomNumber) {
		String url = "https://api.einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp?version=0.3&type=Barcode&invNum=%s&action=qryInvDetail&generation=V2&invTerm=%s&invDate=&encrypt=&sellerID=&UUID=a&randomNumber=%s&appID=EINV9201511136249";
		return String.format(url, invNum, invTerm, randomNumber);
	}

	/**
	 * 將 財政部 傳回的資料轉為 Invoice Entity
	 * @param node response 資料
	 * @param invoiceId
	 * @return
	 * @throws ParseException 
	 */
	private InvoiceEntity parseInvoice(ObjectNode node) throws ParseException{
		InvoiceEntity parseNodeToInvoice = parseNodeToInvoice(new InvoiceEntity(), node);
		List<InvoiceDetailEntity> invDetails = new ArrayList<>();
		parseNodeToInvoice.setInvoiceDetail(parseNodeToInvoiceDetail(invDetails, node.get("details")));
		return parseNodeToInvoice;
	}
	
	/**
	 * 將 ObjectNode 轉為 Invoice
	 * @param invoice 
	 * @param node 
	 * @return
	 * @throws ParseException
	 */
	private InvoiceEntity parseNodeToInvoice(InvoiceEntity invoice, ObjectNode node) throws ParseException {
		invoice.setInvNum(getText(node, INV_NUM));
		if (StringUtils.isNoneBlank(getText(node, "invDate")) && StringUtils.isNoneBlank(getText(node, "invoiceTime"))) {
			String invDate = getText(node, "invDate") + " " + getText(node, "invoiceTime");
			invoice.setInvDate(DateTimeModel.getTime(df.parse(invDate)));
		}
		invoice.setSellerName(getText(node, "sellerName"));
		invoice.setInvStatus(getText(node, "invStatus"));
		invoice.setInvPeriod(getText(node, "invPeriod"));
		invoice.setSellerBan(getText(node, "sellerBan"));
		invoice.setSellerAddress(getText(node, "sellerAddress"));
		invoice.setV(getText(node, "v"));
		invoice.setCode(getText(node, "code"));
		invoice.setMsg(getText(node, "msg"));
		invoice.setUploadTime(DateTimeModel.getTime());
		return invoice;
	}
	
	/**
	 * 將 JsonNode 轉為 InvoiceDetail 
	 * @param invDetails 一個 list
	 * @param details invoiceDetail 資料
	 * @return
	 */
	private List<InvoiceDetailEntity> parseNodeToInvoiceDetail(List<InvoiceDetailEntity> invDetails, JsonNode details) {
		if (details != null) {
			for (JsonNode detail : details) {
				InvoiceDetailEntity invDetail = new InvoiceDetailEntity();
				Double amount = detail.get("amount") != null ? detail.get("amount").asDouble() : null;
				invDetail.setAmount(amount);
				invDetail.setDescription(getText(detail, "description"));
				Double unitPrice = detail.get("unitPrice") != null ? detail.get("unitPrice").asDouble() : null;
				invDetail.setUnitPrice(unitPrice);
				Integer quantity = detail.get("quantity") != null ? detail.get("quantity").asInt() : null;
				invDetail.setQuantity(quantity);
				invDetails.add(invDetail);
			}
		}
		return invDetails;
	}
	
	/**
	 * 驗證 財政部傳回來的Response
	 * @param node response 資料
	 * @return 返回 自定義狀態數字
	 */
	private int validateInvoiceResp(ObjectNode node) {
		try {
			Integer statusCode = node.get("code").asInt();
			if (200 == statusCode) {
				return this.validateInvoinceRespHttpStatus200(node);
			} else if (903 == statusCode) {
				return MSG_INVOICE_PARAMETER_ERROR;
			}
			logger.error("statusCode = {}", statusCode);
		} catch (Exception e) {
			logger.error("validateInvoiceResp Error: {}", e);
		}
		return MSG_INVOICE_INTERNAL_ERROR;
	}
	
	/**
	 * 驗證 財政部傳回來的 Http Status = 200 
	 * @param node response 資料
	 * @return 返回 自定義狀態數字
	 */
	private int validateInvoinceRespHttpStatus200(ObjectNode node) {
		String invStatus = getText(node, "invStatus");
		if ("已確認".equals(invStatus)) {
			return MSG_INVOICE_SUCCESS;
		} else {
			return MSG_INVOICE_NOT_FOUND;
		}
	}
}
