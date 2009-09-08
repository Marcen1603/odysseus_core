package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamSetterFactory {
	
	private final Logger log = LoggerFactory.getLogger(ParamSetterFactory.class);
	private static final ParamSetterFactory fac = new ParamSetterFactory();
	
	public static ParamSetterFactory getInstance() {
		return fac;
	}
	
	private ParamSetterFactory() {
		
	}
	
	public DefaultParamSetter<?> createParam(String type, String setter, String name) {
		if(type.toUpperCase().equals("STRING")) {
			DefaultParamSetter<String> sParam = new DefaultParamSetter<String>(type, setter, name);
			return sParam;
		}else if(type.toUpperCase().equals("INTEGER")) {
			DefaultParamSetter<Integer> sParam = new DefaultParamSetter<Integer>(type, setter, name);
			return sParam;
		}
		return null;
	}
	
}
