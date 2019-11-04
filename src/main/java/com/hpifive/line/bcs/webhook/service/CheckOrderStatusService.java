package com.hpifive.line.bcs.webhook.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.akka.body.OrderListMsg;
import com.hpifive.line.bcs.webhook.akka.body.ReceivingTextMsg;
import com.hpifive.line.bcs.webhook.common.HttpClientUtil;
import com.hpifive.line.bcs.webhook.common.ResponseDTO;
import com.hpifive.line.bcs.webhook.config.DominosProperties;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.entities.LineUserEntity;

@Service
@Component
public class CheckOrderStatusService {

	private static final Logger logger = LoggerFactory.getLogger(CheckOrderStatusService.class);
	
	@Autowired
	private DominosProperties dominosProperties;
	
	private static String CHECK_ORDER = "查詢訂單";
	
	private static Map<String, String> mapOrderStatus = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("訂單成立", "已成立");
			put("製作中", "放配料中");
			put("烘培中", "在爐子烘烤中");
			put("可外帶", "可以來外帶嘍");
			put("外送中", "準備外送出門了");
			put("訂單取消", "訂單已取消");
		}
	};

	@Autowired
	private UserDao userDao;

	public OrderListMsg onTextMsgReceived(ReceivingTextMsg msg) {
		LineUserEntity userEntity = this.userDao.getByUid(msg.getUid());
		logger.info("userEntity = " + userEntity);
		if (userEntity == null) {
			return null;
		}
		
		logger.info("userEntity.getUid() = " + userEntity.getUid());
		logger.info("userEntity.getStatus() = " + userEntity.getStatus());

		logger.info("msg.getText() = " + msg.getText());
		if (!msg.getText().equals(CHECK_ORDER)) {
			return null;
		}
		

		logger.info("msg.getUid() = " + msg.getUid());
		
		// Call Dominos API to check order status
		String payload = String.format("{\"uuid\": \"%s\"}", msg.getUid());

		StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

		logger.info("dominosOrderlistUrl = " + dominosProperties.getOrderlist_url());
		
		try {
			ResponseDTO dto = HttpClientUtil.post(dominosProperties.getOrderlist_url(), null, entity);
			logger.error("status : {}, body : {}", dto.getStatus(), dto.getResponseBody());
			
			if (dto.getStatus() != HttpStatus.SC_OK) {
				return null;
			}
			
			String strResponseContent = dto.getResponseBody();
			
			Object responseData = new JSONTokener(strResponseContent).nextValue();
			logger.info("responseData = " + responseData);
			
			JSONObject jsonObject = null;
			
			String orderContent = null;
			
			if (responseData instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray(strResponseContent);

				jsonObject = jsonArray.getJSONObject(0);

				if (jsonObject.has("id") 
					&& jsonObject.has("status") 
					&& jsonObject.has("expdate") 
					&& jsonObject.has("store") 
					&& jsonObject.has("store_addr") 
					&& jsonObject.has("pickup")
					&& jsonObject.has("address") 
					&& jsonObject.has("payway")) {

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

					String strOrderExpDate = jsonObject.getString("expdate");
					logger.info("strOrderExpDate = " + strOrderExpDate);

					Date today = new Date(System.currentTimeMillis());
					Date orderExpDate = simpleDateFormat.parse(strOrderExpDate);

					Calendar cal_Today = Calendar.getInstance();
					cal_Today.setTime(today);
					logger.info("cal_Today = " + cal_Today);

					Calendar cal_ExpDate = Calendar.getInstance();
					cal_ExpDate.setTime(orderExpDate);
					logger.info("cal_ExpDate = " + cal_ExpDate);

					boolean isOrderExpired = cal_Today.get(Calendar.YEAR) >= cal_ExpDate.get(Calendar.YEAR) && cal_Today.get(Calendar.DAY_OF_YEAR) > cal_ExpDate.get(Calendar.DAY_OF_YEAR);
					logger.info("isOrderExpired = " + isOrderExpired);

					if (isOrderExpired) {
						orderContent = String.format("經查詢，訂單並未成立，如有任何問題請洽客服0800-291-252 (服務時間10:00-22:00)");
					} else {
						String strOrderId = jsonObject.getString("id");
						String strOrderStatus = mapOrderStatus.get(jsonObject.getString("status"));
						String strOrderStore = jsonObject.getString("store");
						String strOrderStoreAddr = jsonObject.getString("store_addr");
						String strOrderPickup = jsonObject.getString("pickup");
						String strOrderAddress = jsonObject.getString("address");
						String strOrderPayway = jsonObject.getString("payway");

						strOrderAddress = (strOrderAddress == null || strOrderAddress.equals("")) ? "無" : strOrderAddress;

						orderContent = String.format("訂單狀態：%s\r\n" + "訂單編號：%s\r\n" + "取餐日期：%s\r\n" + "取餐方式：%s-%s\r\n" + "門市地址：%s\r\n" + "外送地址：%s\r\n" + "付款方式：%s"
													, strOrderStatus
													, strOrderId
													, strOrderExpDate
													, strOrderStore
													, strOrderPickup
													, strOrderStoreAddr
													, strOrderAddress
													, strOrderPayway);
					}
				}
			}
			else if (responseData instanceof JSONObject) {
				jsonObject = new JSONObject(strResponseContent);

				if (jsonObject.has("code") && jsonObject.has("text")) {

					logger.debug(String.format("Response Data : Code = %s, text = %s", jsonObject.getInt("code"), jsonObject.getString("text")));

					orderContent = String.format("經查詢，訂單並未成立，如有任何問題請洽客服0800-291-252 (服務時間10:00-22:00)");
				}
			}

			logger.info("orderContent = " + orderContent);
			
			return new OrderListMsg(msg.getReplyToken(), orderContent, msg.getUid());
			
		} catch (Exception e) {
			logger.error("Exception : {}", e);
		}
		
		return null;
	}
}
