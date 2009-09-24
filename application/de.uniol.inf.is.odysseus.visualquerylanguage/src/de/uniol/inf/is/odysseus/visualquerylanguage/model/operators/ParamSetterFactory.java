package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;


public class ParamSetterFactory {
	
	private static final ParamSetterFactory fac = new ParamSetterFactory();
	
	public static ParamSetterFactory getInstance() {
		return fac;
	}
	
	private ParamSetterFactory() {
		
	}
	
	public DefaultParamSetter<?> createParam(String type, String setter, String name) {
		DefaultParamSetter<Object> sParam = new DefaultParamSetter<Object>(type, setter, name);
		return sParam;
	}
	
}
