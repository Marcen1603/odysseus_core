package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;


public class ParamConstructFactory {
	
	private static final ParamConstructFactory fac = new ParamConstructFactory();
	
	public static ParamConstructFactory getInstance() {
		return fac;
	}
	
	private ParamConstructFactory() {
		
	}
	
	public DefaultParamConstruct<?> createParam(String type, Collection<String> typeList, int position, String name) {
			DefaultParamConstruct<Object> cParam = new DefaultParamConstruct<Object>(type, typeList, position, name);
			return cParam;
	}
}
