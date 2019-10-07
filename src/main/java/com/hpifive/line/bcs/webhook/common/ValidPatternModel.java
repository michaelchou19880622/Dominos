package com.hpifive.line.bcs.webhook.common;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.entities.config.ApplyDataFormatType;
import com.hpifive.line.bcs.webhook.exception.CustomException;

public class ValidPatternModel {
	private static final Logger logger = LoggerFactory.getLogger(ValidPatternModel.class);
	
	private static List<String> genders = Arrays.asList("男", "女", "FEMALE", "MALE", "F", "M", "男生", "女生");
	
	private ValidPatternModel() {}
	
	public static boolean verifyStringByFormatTypeAndLength(String str, ApplyDataFormatType format, Integer length) throws CustomException  {
		if (str.length() > length) {
			logger.info("長度驗證失敗 keyword: {} length: {}", str, length);
			return false;
		}
		return ValidPatternModel.verifyByFormatTypeAndText(format, str);
	}
	public static boolean verifyByFormatTypeAndText(ApplyDataFormatType format, String text) throws CustomException {
		if (ApplyDataFormatType.ENGLISH == format) {
			return ValidPatternModel.isEnglish(text);
		} else if (ApplyDataFormatType.NUMBER == format) {
			return ValidPatternModel.isNumber(text);
		} else if (ApplyDataFormatType.ENGNUM == format) {
			return ValidPatternModel.isEngAndNum(text);
		} else if (ApplyDataFormatType.NONCHINESE == format) {
			return ValidPatternModel.isNotChinese(text);
		} else if (ApplyDataFormatType.CHINESE == format) {
			return ValidPatternModel.isChinese(text);
		} else if (ApplyDataFormatType.CHIENGNUM == format){
			return ValidPatternModel.isCHIENGNUM(text);
		} else if (ApplyDataFormatType.EMAIL == format) {
			return ValidPatternModel.isEmail(text);
		} else if (ApplyDataFormatType.CELLPHONE == format) {
			return ValidPatternModel.isCellPhone(text);
		} else if (ApplyDataFormatType.DATE == format) {
			return ValidPatternModel.isDate(text);
		} else if (ApplyDataFormatType.GENDER == format) {
			return ValidPatternModel.isGender(text);
		} else if (ApplyDataFormatType.INVOICE_NUM == format) {
			return isInvoiceNumber(text);
		} else if (ApplyDataFormatType.INVOICE_RAND == format) {
			return isInvoiceRandNumber(text);
		} else if (ApplyDataFormatType.INVOICE_TERM == format) {
			return isInvoiceTerm(text);
		}
		throw CustomException.message("Can't Match with Format Type: "+format.toString());
	}
	
	public static boolean isCHIENGNUM(String str) {
		return ValidPatternModel.match(str, "^[A-Za-z0-9\u4e00-\u9fa5]+$");
	}
	
	public static boolean isInvoiceNumber(String str) {
        return ValidPatternModel.match(str, "[A-Z]{2}\\d{8}");
	}
	
	public static boolean isInvoiceRandNumber(String str) {
		return ValidPatternModel.match(str, "\\d{4}");
	}
	
	public static boolean isInvoiceTerm(String str) {
		return ValidPatternModel.match(str, "\\d{5}");
	}
	
	public static boolean isDate(String str) {
		if (str == null) {
			return false;
		}
		if (! ValidPatternModel.isNumber(str)) {
			return false;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		if (str.trim().length() != dateFormat.toPattern().length()) {
			return false;
		}
		dateFormat.setLenient(false);
		try {
			Date date = dateFormat.parse(str.trim());
			return date.before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean match(String str, String regex) {
		if (str == null) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public static boolean isNumber(String str) {
		return ValidPatternModel.match(str, "^[0-9]+$");
	}
	
	public static boolean isEngAndNum(String str) {
		return ValidPatternModel.match(str, "^[a-zA-Z0-9]+$");
	}
	
	public static boolean isEnglish(String str) {
		return ValidPatternModel.match(str, "^[a-zA-Z]+$");
	}
	
	public static boolean isEmail(String str) {
		return EmailValidator.getInstance().isValid(str);
	}
	
	public static boolean isChinese(String str) {
		return ValidPatternModel.match(str, "^[\\u4e00-\\u9fa5]{0,}$");
	}
	
	public static boolean isNotChinese(String str) {
		return !ValidPatternModel.match(str, "[\\u4e00-\\u9fa5]+");
	}
	
	public static boolean isCellPhone(String str) {
		return ValidPatternModel.match(str, "09[0-9]{2}[0-9]{3}[0-9]{3}");
	}
	
	public static boolean isGender(String str) {
		return ValidPatternModel.genders.contains(str);
	}
	
}
