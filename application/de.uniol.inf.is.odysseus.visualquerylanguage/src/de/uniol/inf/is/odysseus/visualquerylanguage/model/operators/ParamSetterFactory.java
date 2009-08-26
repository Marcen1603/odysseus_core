package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

public class ParamSetterFactory {
	
	public DefaultParamSetter<?> createParam(String type, String setter, String name) {
		if(type.equals("String")) {
			DefaultParamSetter<String> sParam = new DefaultParamSetter<String>(type, setter, name);
			return sParam;
		}else if(type.equals("Integer")) {
			DefaultParamSetter<Integer> sParam = new DefaultParamSetter<Integer>(type, setter, name);
			return sParam;
		}
		return null;
	}
	
}
