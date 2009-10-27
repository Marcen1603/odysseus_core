package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;


public class ParamSetterFactory {
	
	private static final ParamSetterFactory fac = new ParamSetterFactory();
	
	public static ParamSetterFactory getInstance() {
		return fac;
	}
	
	private ParamSetterFactory() {
		
	}
	
	public DefaultParamSetter<?> createParam(String type, Collection<String> typeList, String setter, String name) {
		DefaultParamSetter<Object> sParam = new DefaultParamSetter<Object>(type, typeList, setter, name);
		return sParam;
	}
	
}
