package com.hpifive.line.bcs.webhook.linepoint.model;

import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpifive.line.bcs.webhook.common.HttpClientUtil;
import com.hpifive.line.bcs.webhook.common.ResponseDTO;
import com.hpifive.line.bcs.webhook.linepoint.unit.EndPoint;
import com.hpifive.line.bcs.webhook.linepoint.unit.EndPointPath;

public class LinePointRequest {
	
	private static final Logger logger = LoggerFactory.getLogger(LinePointRequest.class);

	private static String token = null;
	private static ObjectMapper mapper = new ObjectMapper();
	
	public TransactionIssue issue(IssuePoint point) {
		try {
			String fqdn = String.format("%s%s", EndPoint.ENDPOINT.getUrl(), EndPointPath.ISSUE.getPath());
			ResponseDTO dto = HttpClientUtil.post(fqdn, getAuthHeader(), new StringEntity(mapper.writeValueAsString(point)));
			if (dto.getStatus() == 200) {
				return mapper.readValue(dto.getResponseBody(), TransactionIssue.class);
			}
			logger.error("Issue Line Point fail with code {}, body {}", dto.getStatus(), dto.getResponseBody());
		} catch (Exception e) {
			logger.error("Issue Line Point Error {}", e);
		}
		return null;
	}

	private static Header[] getAuthHeader() {
		List<Header> headers = Arrays.asList(
				new BasicHeader("Authorization", String.format("Bearer %s", token))
		);
		Header[] arg = new Header[headers.size()];
		return headers.toArray(arg);
	}
	
	public static void setToken(String token) {
		LinePointRequest.token = token;
	}

	public static String getToken() {
		return token;
	}
	
}
