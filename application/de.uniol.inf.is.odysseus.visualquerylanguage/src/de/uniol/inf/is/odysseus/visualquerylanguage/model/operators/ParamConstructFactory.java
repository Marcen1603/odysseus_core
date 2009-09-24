package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;


public class ParamConstructFactory {
	
	private static final ParamConstructFactory fac = new ParamConstructFactory();
	
	public static ParamConstructFactory getInstance() {
		return fac;
	}
	
	private ParamConstructFactory() {
		
	}
	
	public DefaultParamConstruct<?> createParam(String type, int position, String name) {
			DefaultParamConstruct<Object> cParam = new DefaultParamConstruct<Object>(type, position, name);
			return cParam;
	}
}
