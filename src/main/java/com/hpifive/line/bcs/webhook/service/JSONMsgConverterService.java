package com.hpifive.line.bcs.webhook.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hpifive.line.bcs.webhook.exception.CustomException;
import com.linecorp.bot.model.message.Message;

@Service
public class JSONMsgConverterService {
	
	private ObjectMapper mapper;
	
	public JSONMsgConverterService() {
		super();
		this.mapper = new ObjectMapper();
		mapper.registerModule(new ParameterNamesModule());
	}
	public Optional<Message> convertBy(Message obj) throws CustomException, IOException {
		if (obj == null) {
			throw CustomException.message("obj can not be null");
		}
		String jsonString = this.mapper.writeValueAsString(obj);
		return this.convert(jsonString);
	}
	
	public String convert(Message msg) throws JsonProcessingException {
		return this.mapper.writeValueAsString(msg);
	}
	
	public Optional<Message> convert(String json) throws IOException, CustomException {
		JsonNode rootNode = this.mapper.readTree(json);
		Message message = this.convertToObject(rootNode);
		return Optional.ofNullable(message);
	}
	
	private Message convertToObject(JsonNode node) throws JsonProcessingException, CustomException {
		JsonNode typeNode = node.path("type");
		if (typeNode != null) {
			return this.convertByType(node);
		}
		throw CustomException.message("type node isn't defined");
	}
	
	private Message convertByType(JsonNode rootNode) throws JsonProcessingException {
		return this.mapper.treeToValue(rootNode, Message.class);
	}
}
