package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamConstructFactory {
	
	private final Logger log = LoggerFactory.getLogger(ParamConstructFactory.class);
	private static final ParamConstructFactory fac = new ParamConstructFactory();
	
	public static ParamConstructFactory getInstance() {
		return fac;
	}
	
	private ParamConstructFactory() {
		
	}
	
	public DefaultParamConstruct<?> createParam(String type, int position, String name) {
		if(type.toUpperCase().equals("STRING")) {
			DefaultParamConstruct<String> cParam = new DefaultParamConstruct<String>(type, position, name);
			return cParam;
		}else if(type.toUpperCase().equals("INTEGER")) {
			DefaultParamConstruct<Integer> cParam = new DefaultParamConstruct<Integer>(type, position, name);
			return cParam;
		}
		return null;
	}
}
