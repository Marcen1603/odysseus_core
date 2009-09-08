package de.uniol.inf.is.odysseus.visualquerylanguage.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Validator {
	
	private final Logger log = LoggerFactory.getLogger(Validator.class);
	private static final Validator validator = new Validator();
	
	public static Validator getInstance() {
		return validator;
	}
	
	public boolean validateInteger(String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (Exception e) {
			log.error("Could not parse value to Integer");
			return false;
		}
	}
	
	public boolean validateInteger(String value, int min, int max) {
		try {
			int intValue = Integer.parseInt(value);
			if(intValue >= min && intValue <= max) {
				return true;
			}
		} catch (Exception e) {
			log.error("Could not parse value to Integer");
			return false;
		}
		
		return false;
	}
	
	public boolean validateString(String value, String regex) {
		return true;
	}
	
}
