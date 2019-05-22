package com.hpifive.line.bcs.webhook.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.common.HttpClientUtil;
import com.hpifive.line.bcs.webhook.common.ResponseDTO;
import com.hpifive.line.bcs.webhook.config.InvoiceHttpServiceStatus;
import com.hpifive.line.bcs.webhook.entities.InvoiceDetailEntity;
import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;
import com.hpifive.line.bcs.webhook.entities.config.InvoiceStatus;
import com.hpifive.line.bcs.webhook.exception.CustomException;

@Service
@Component
public class InvoiceHttpService {
	
	private static final Logger logger = LoggerFactory.getLogger(InvoiceHttpService.class);
	
	public InvoiceEntity getInvoiceFromGov(InvoiceEntity inv) {
		try {
			return this.getInvoiceFromGov(inv.getInvNum(), inv.getInvTerm(), inv.getInvRandom());
		} catch (Exception e) {
			logger.info("Error Get From InvoiceEntity {}", e);
			return null;
		}
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
			logger.info("call 財政部source");
			logger.info("invoiceJson {}", invoiceJson);
			if (invoiceJson != null) {
				InvoiceEntity invoice = parseInvoice(invoiceJson);
				logger.info("invoice JSON Entity: {}", invoice);
				invoice.setInvNum(invNum);
				invoice.setInvTerm(invTerm);
				invoice.setInvRandom(randomNumber);
				InvoiceHttpServiceStatus validateResult = validateInvoiceResp(invoiceJson);
				if (InvoiceHttpServiceStatus.SUCCESS != validateResult) {
					invoice.setStatus(InvoiceStatus.NOT_FOUND.toString());
				}
				logger.info("call 財政部結果 {}", invoice);
				return invoice;
			}
		}
		return null;
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
		parseNodeToInvoice.setInvoiceDetail(parseNodeToInvoiceDetail(new ArrayList<>(), node.get("details")));
		logger.info("detail {}", parseNodeToInvoice.getInvoiceDetail());
		return parseNodeToInvoice;
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
	 * 將 ObjectNode 轉為 Invoice
	 * @param invoice 
	 * @param node 
	 * @return
	 * @throws ParseException
	 */
	private InvoiceEntity parseNodeToInvoice(InvoiceEntity invoice, ObjectNode node) throws ParseException {
		invoice.setInvNum(getText(node, "invNum"));
		if (StringUtils.isNoneBlank(getText(node, "invDate")) && StringUtils.isNoneBlank(getText(node, "invoiceTime"))) {
			String invDate = getText(node, "invDate") + " " + getText(node, "invoiceTime");
			invoice.setInvDate(DateTimeModel.getTime(new SimpleDateFormat("yyyyMMdd hh:mm:ss").parse(invDate)));
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
			ResponseDTO dto = HttpClientUtil.execute(new HttpGet(uri));
			logger.info("status= {}", dto.getStatus());
			return dto.getResponseBody();
		} catch (Exception e) {
			throw new CustomException(e);
		}
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
		return new SimpleDateFormat("yyyMM").format(cal.getTime());
	}
	/**
	 * 將 發票圖片 解析完的日期作轉換，轉為 Date 型態
	 * @param dateStr 日期字串
	 * @return 
	 */
	private Date parseOrginalalInvTermToDate(String dateStr) {
		try {
			if (dateStr.length() == 5) {
				return new SimpleDateFormat("yyyMM").parse(dateStr);
			} else if (dateStr.length() == 7) {
				return new SimpleDateFormat("yyyMMdd").parse(dateStr);
			}
		} catch (ParseException e) {
			logger.error("parseOrginalalInvTermToDate", e);
		}
		return null; 
	}
	
	/**
	 * 驗證 財政部傳回來的Response
	 * @param node response 資料
	 * @return 返回 自定義狀態數字
	 */
	private InvoiceHttpServiceStatus validateInvoiceResp(ObjectNode node) {
		try {
			Integer statusCode = node.get("code").asInt();
			if (200 == statusCode) {
				return this.validateInvoinceRespHttpStatus200(node);
			} else if (903 == statusCode) {
				return InvoiceHttpServiceStatus.PARAMETER_ERROR;
			}
			logger.error("Unknown statusCode = {}", statusCode);
		} catch (Exception e) {
			logger.error("validateInvoiceResp", e);
		}
		return InvoiceHttpServiceStatus.INTERNAL_ERROR;
	}
	
	/**
	 * 驗證 財政部傳回來的 Http Status = 200 
	 * @param node response 資料
	 * @return 返回 自定義狀態數字
	 */
	private InvoiceHttpServiceStatus validateInvoinceRespHttpStatus200(ObjectNode node) {
		String invStatus = getText(node, "invStatus");
		if (InvoiceStatus.VALID == InvoiceStatus.fromChinese(invStatus)) {
			return InvoiceHttpServiceStatus.SUCCESS;
		}
		return InvoiceHttpServiceStatus.FAILURE;
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
	
}
