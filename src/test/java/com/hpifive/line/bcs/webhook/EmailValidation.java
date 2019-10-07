package com.hpifive.line.bcs.webhook;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.hpifive.line.bcs.webhook.common.ValidPatternModel;
import com.hpifive.line.bcs.webhook.entities.config.ApplyDataFormatType;
import com.hpifive.line.bcs.webhook.exception.CustomException;

public class EmailValidation {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws CustomException {
		String[] testCase = {
				"fkym08@gmail.com",
				"fkym_08_ym@gmail.com",
				"arnor_0000@hpicorp.com.tw"
		};
		for(int i=0; i<testCase.length; i+=1) {
			boolean result = ValidPatternModel.verifyByFormatTypeAndText(ApplyDataFormatType.EMAIL, testCase[i]);
			assertEquals(true, result);
			System.out.println(String.format("TestCase: %s valid result %s", testCase[i], result));
		}
	}

}
