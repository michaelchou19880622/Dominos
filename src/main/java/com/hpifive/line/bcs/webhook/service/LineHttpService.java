package com.hpifive.line.bcs.webhook.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpifive.line.bcs.webhook.common.CryptGenerator;
import com.hpifive.line.bcs.webhook.common.HttpClientUtil;
import com.hpifive.line.bcs.webhook.config.CustomChannelTokenSupplier;
import com.hpifive.line.bcs.webhook.config.LineBotProperties;
import com.hpifive.line.bcs.webhook.config.LineURL;
import com.hpifive.line.bcs.webhook.exception.CustomException;

@Service
@Component
public class LineHttpService {
	
	@Autowired
	private LineBotProperties property;
	
	@Autowired
	private CustomChannelTokenSupplier supplier;
	
	private static final Logger logger = LoggerFactory.getLogger(LineHttpService.class);
	private ExecutorService exe = null;
	
	private LineHttpService() {
	}
	
	@PostConstruct
	private void init() {
		if (exe == null) {
			exe = Executors.newFixedThreadPool(300);
		}
	}
	
	public CompletableFuture<BufferedImage> readImageFromLine(String messageId) {
		CompletableFuture<BufferedImage> result = new CompletableFuture<>();
		CompletableFuture.runAsync(() -> {
			String url = String.format(LineURL.CONTENT.toString(), messageId);
			try {
				
				String fqdn = this.property.getEndpoint()+url;
				logger.info("Read Image {}", fqdn);
				HttpResponse response = getContent(fqdn, this.supplier.get());
				HttpEntity entity = response.getEntity();
				BufferedImage image = ImageIO.read(entity.getContent());
				EntityUtils.consume(entity);
				result.complete(image);
			}  catch (Exception e) {
				result.completeExceptionally(e);
			}
		}, exe);
		return result;
	}
	
	public CompletableFuture<HttpResponse> replyMsgToLine(String url, Object msg) {
		logger.debug("Executor: {}", exe);
		String fqdn = this.property.getEndpoint()+url;
	    CompletableFuture<HttpResponse> result = new CompletableFuture<>();
	    CompletableFuture.runAsync(() -> {
	    	try {
	    		HttpResponse response = postMsg(fqdn, msg, this.supplier.get());
	    		result.complete(response);
	    	 } catch (Exception e) {
	    		result.completeExceptionally(e);
	    	 }
	    }, exe);
	    
	    return result;
	}
	
	public CompletableFuture<HttpResponse> postMsgToWebcomm(Object msg) {
		logger.debug("Executor: {}", exe);
	    CompletableFuture<HttpResponse> result = new CompletableFuture<>();
	    CompletableFuture.runAsync(() -> {
	    	try {
	    		HttpResponse response = postMsg("https://pgline.friendo.com.tw:443/pgoaweb/line/bot/api/receiving/PG", msg, "");
	    		result.complete(response);
	    	 } catch (Exception e) {
	    		result.completeExceptionally(e);
	    	 }
	    }, exe);
	    
	    return result;
	}
	public HttpResponse postMsgs(String url, Object msg, String token) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		return postMsg(url, msg, token);
	}
	private HttpResponse postMsg(String url, Object msg, String token) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		HttpClient client = HttpClientUtil.generateClient();
		HttpPost requestPost = new HttpPost(url);
		String requestBody = msg instanceof String ? msg.toString() : new ObjectMapper().writeValueAsString(msg);		
		logger.debug(requestBody);
		String signature = CryptGenerator.sha256(this.property.getChannelSecret(), requestBody);
		StringEntity entity = new StringEntity(requestBody, "utf-8");
		requestPost.setEntity(entity);
		requestPost.setHeader("Content-Type", "application/json");
		requestPost.setHeader("Authorization", token);
		requestPost.setHeader("X-Line-Signature", signature);
		HttpResponse response =  client.execute(requestPost);
		requestPost.releaseConnection();
		return response;
	}
	
	public static HttpResponse getToken(String url, String id, String secret) throws KeyManagementException, NoSuchAlgorithmException, IOException  {
		HttpClient client = HttpClientUtil.generateClient();
		HttpPost requestPost = new HttpPost(url);
		List <NameValuePair> nvps = new ArrayList <>();
		nvps.add(new BasicNameValuePair("grant_type", "client_credentials"));
		nvps.add(new BasicNameValuePair("client_id", id));
		nvps.add(new BasicNameValuePair("client_secret", secret));
		requestPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
		HttpResponse response =  client.execute(requestPost);
		requestPost.releaseConnection();
		return response;
	}
	
	public static HttpResponse getContent(String url, String token) throws CustomException {
		HttpClient client = null;
		HttpResponse response = null;
		HttpGet requestGet = null;
		try {
			client = HttpClientUtil.generateClient();
			requestGet = new HttpGet(url);
			requestGet.setHeader("Authorization", "Bearer "+token);
			response =  client.execute(requestGet);
			return response;
		} catch (Exception e) {
			throw new CustomException(e);
		}
	}
	
}
