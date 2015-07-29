package de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators;

public interface ITypeOperators {
	Class<?> getType();
	
	boolean plusSupported();	
	boolean minusSupported();
	boolean multiplySupported();
	boolean divideSupported();
	boolean moduloSupported();
	boolean plusPrefixSupported();
	boolean minusPrefixSupported();
	boolean plusPostfixSupported();
	boolean minusPostfixSupported();
	boolean getSupported();

	boolean hasExtensionAttribute(String name);
	boolean hasExtensionMethod(String name, int args);

}
