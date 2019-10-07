package com.hpifive.line.bcs.webhook.common;

import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static final Object INIT_FLAG = "INIT_FLAG";
	
	private static CloseableHttpClient httpClient;
	
	private static int timeout = 3;

	private HttpClientUtil() {}
	
	public static HttpClient generateClient() throws KeyManagementException, NoSuchAlgorithmException {
		synchronized (INIT_FLAG) {
			if(httpClient == null){
				SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

				// set up a TrustManager that trusts everything
				sslContext.init(null, new TrustManager[] { new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						logger.info("getAcceptedIssuers => {}", Calendar.getInstance().getTime());
						return null;
					}
					
					public void checkClientTrusted(X509Certificate[] certs, String authType) {
						logger.info("checkClientTrusted => {}", Calendar.getInstance().getTime());
					}
					
					public void checkServerTrusted(X509Certificate[] certs, String authType) {
						logger.info("checkServerTrusted => {}", Calendar.getInstance().getTime());
					}
				} }, new SecureRandom());
				
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
				
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					    .register("http", PlainConnectionSocketFactory.getSocketFactory())
					    .register("https", sslsf)
					    .build();
				
				// PoolingHttpClientConnectionManager
				PoolingHttpClientConnectionManager pm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
				pm.setMaxTotal(1200);
				pm.setDefaultMaxPerRoute(300);
				
				RequestConfig defaultRequestConfig = RequestConfig.custom()
					    .setSocketTimeout(timeout  * 1000)
					    .setConnectTimeout(timeout  * 1000)
					    .setConnectionRequestTimeout(timeout  * 1000)
					    .build();
				
				httpClient = HttpClients.custom().setConnectionManager(pm).setDefaultRequestConfig(defaultRequestConfig).build();
			}
		}
		
		return httpClient; 
	}
	
	public static ResponseDTO execute(HttpUriRequest request) {
		Integer statusCode = 400;
		InputStream inputStream = null;
		StringWriter writer = new StringWriter();
		try {
			HttpClient client = generateClient();
			HttpResponse rsp =  client.execute(request);
			statusCode = rsp.getStatusLine().getStatusCode();
			HttpEntity rspEntity = rsp.getEntity();
			inputStream = rspEntity.getContent();
			IOUtils.copy(inputStream, writer, "utf-8");
			EntityUtils.consume(rspEntity);
		} catch (Exception e) {
			try {
				HttpClient client = generateClient();
				HttpResponse rsp =  client.execute(request);
				statusCode = rsp.getStatusLine().getStatusCode();
				HttpEntity rspEntity = rsp.getEntity();
				inputStream = rspEntity.getContent();
				IOUtils.copy(inputStream, writer, "utf-8");
				EntityUtils.consume(rspEntity);
			} catch (Exception e1) {
				logger.error("Execute Error twice => ", e1);
			}
		}
		return new ResponseDTO(statusCode, writer.toString());
	}
	
	public static ResponseDTO post(String url, Header[] headers, HttpEntity body) {
		logger.info("準備發送的 url => {}, 發送時間為 => {}", url, Calendar.getInstance().getTime());
		Integer statusCode = 400;
		try {
			HttpPost requestPost = new HttpPost(url);
			requestPost.setHeaders(headers);
			requestPost.setEntity(body);
			return execute(requestPost);
		} catch (Exception e) {
			logger.error("Execute Error", e);
		}
		return new ResponseDTO(statusCode, ""); 
	}
	
	public static HttpEntity toUrlEncodeForm(Map<String, String> map) {
		try {
			List <NameValuePair> nvps = new ArrayList <>();
			for(Entry<String, String> entry: map.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			return new UrlEncodedFormEntity(nvps, "utf-8");
		} catch (Exception e) {
			logger.error("Convert To URL Encode Form Error", e);
		}
		return null;
	}
}
