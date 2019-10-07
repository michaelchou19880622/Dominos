package com.hpifive.line.bcs.webhook.config;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.controller.body.ChannelTokenResponseBody;
import com.hpifive.line.bcs.webhook.dao.SystemConfigDao;
import com.hpifive.line.bcs.webhook.entities.SystemConfigEntity;
import com.hpifive.line.bcs.webhook.entities.config.SystemConfigKeys;
import com.hpifive.line.bcs.webhook.exception.CustomException;
import com.hpifive.line.bcs.webhook.service.LineHttpService;
import com.hpifive.line.bcs.webhook.service.SystemConfigService;
import com.linecorp.bot.client.ChannelTokenSupplier;

@Service
@Component
public class CustomChannelTokenSupplier implements ChannelTokenSupplier {
	private static final Logger logger = LoggerFactory.getLogger(CustomChannelTokenSupplier.class);
	
	private volatile String channelToken;
	private volatile Date expireTime;
	
	@Autowired
	private SystemConfigDao configDao;
	@Autowired
	private SystemConfigService configService;
    
	@Autowired
	private LineBotProperties properties;
	
    @Override
    public String get() {
        return channelToken;
    }

    private boolean compareIsExpire(Date date) {
    	long millisecond = (long) (24 * 60 * 60 * 1000);
    	Long temp = this.configService.getLongByKey(SystemConfigKeys.TOKENEXPIRETIMES);
    	if (temp != null) {
    		millisecond = temp*1000;
    	}
    	long expireTimestamp = date.getTime();
        long currentTimestamp = new Date().getTime();
        return  (currentTimestamp+millisecond >= expireTimestamp);
    }
    
    @PostConstruct
    public void loadLastIssuedChannelToken() {
    	String key = this.properties.getChannelTokenKey();
    	try {
	    	SystemConfigEntity entity = this.configDao.getByKey(key);
	    	if (entity == null) {
	    		logger.info("資料庫TOKEN不存在 -- 取得並新增中");
	    		entity = this.renewChannelToken();
	    		this.configDao.save(entity);
	    	}
    		if (this.compareIsExpire(entity.getModifyTime())) {
    			logger.info("TOKEN驗證過期 -- 更新中");
    			SystemConfigEntity tempEntity = this.renewChannelToken();
    			entity.setValue(tempEntity.getValue());
    			entity.setModifyTime(tempEntity.getModifyTime());
    			this.configDao.save(entity);
    		}
    		logger.info("從資料庫的TOKEN取得正常");
    		this.channelToken = entity.getValue();
    		this.expireTime = entity.getModifyTime();
    	} catch (Exception e) {
    		logger.error("loadLastIssuedChannelToken", e);
		}
    }

    private ChannelTokenResponseBody getChannelTokenFromLine() {
    	try {
    		String channelId = properties.getChannelId();
    		String secret = properties.getChannelSecret();
    		String url = properties.getEndpoint()+LineURL.ACCESSTOKEN.toString();
    		HttpResponse response = LineHttpService.getToken(url, channelId, secret);
    		ResponseHandler<String> handler = new BasicResponseHandler();
			String body = handler.handleResponse(response);
			ObjectMapper objectMapper = new ObjectMapper();
			logger.info("BODY {}", body);
			return objectMapper.readValue(body, ChannelTokenResponseBody.class);
    	} catch (Exception e) {
			logger.error("getChannelTokenFromLine", e);
		}
    	return null;
    }
    
    private SystemConfigEntity renewChannelToken() throws CustomException {
    	String key = this.properties.getChannelTokenKey();
    	ChannelTokenResponseBody body = this.getChannelTokenFromLine();
    	if (body == null) {
    		throw CustomException.message("renewChannelToken 無法從 ChannelTokenResponseBody 取得新的Token");
    	}
    	String token = body.getAccessToken();
		long expireTimes = body.getExpiresIn().longValue()*1000;
		Date expireDate = DateTimeModel.plusMillisecond(new Date(), expireTimes);
		return new SystemConfigEntity(null, key, token, expireDate);
    }
    
    public void refreshChannelTokenViaAPIIfExpiresSoon() {
    	logger.info("TOKEN 可用性 定時檢查中");
    	if (this.channelToken == null || this.expireTime == null) {
    		logger.info("沒有TOKEN 進行 TOKEN索取");
    		this.loadLastIssuedChannelToken();
    		return;
    	}
    	if (this.compareIsExpire(this.expireTime)) {
    		logger.info("TOKEN過期 進行 TOKEN索取");
    		this.loadLastIssuedChannelToken();
    		return;
    	}
    	logger.info("TOKEN例行檢查正常");
    }
}
