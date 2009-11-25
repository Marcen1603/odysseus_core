package de.uniol.inf.is.odysseus.cep.metamodel;

public class CepVariable {
	
	public static String getSeperator(){
		return "_";
	}
	
	public static String getAttributeName(String varName) {
		String[] split = varName.split(getSeperator());
		return split[3];
	}
}
