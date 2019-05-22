package com.hpifive.line.bcs.webhook.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnumExtension {
	
	private static final Logger logger = LoggerFactory.getLogger(EnumExtension.class);
	
	private EnumExtension() {}
	
	public static <E extends Enum<E>> E valueOf(Class<E> e, String id) {
		E result = null;
		try {
			result = Enum.valueOf(e, id);
		} catch (Exception exception) {
			logger.warn("Invalid value for enum {} : {}", e.getSimpleName(), id);
		}
		return result;
	}
}
