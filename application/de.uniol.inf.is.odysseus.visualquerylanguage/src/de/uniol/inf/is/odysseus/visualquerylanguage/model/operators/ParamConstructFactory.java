package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

public class ParamConstructFactory {
	
	public DefaultParamConstruct<?> createParam(String type, int position, String name) {
		if(type.equals("String")) {
			DefaultParamConstruct<String> cParam = new DefaultParamConstruct<String>(type, position, name);
			return cParam;
		}else if(type.equals("Integer")) {
			DefaultParamConstruct<Integer> cParam = new DefaultParamConstruct<Integer>(type, position, name);
			return cParam;
		}
		return null;
	}
	
}
