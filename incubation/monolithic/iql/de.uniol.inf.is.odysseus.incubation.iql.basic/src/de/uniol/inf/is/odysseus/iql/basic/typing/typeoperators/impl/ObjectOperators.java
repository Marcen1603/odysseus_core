package de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.impl;

import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.ITypeOperators;

public class ObjectOperators implements ITypeOperators {
	
	public static String NAME = "";
	
	protected static void print(Object obj, String toPrint) {
		System.out.println(toPrint);
	}

	@Override
	public Class<?> getType() {
		return Object.class;
	}

	@Override
	public boolean plusSupported() {
		return false;
	}

	@Override
	public boolean minusSupported() {
		return false;
	}

	@Override
	public boolean multiplySupported() {
		return false;
	}

	@Override
	public boolean divideSupported() {
		return false;
	}

	@Override
	public boolean moduloSupported() {
		return false;
	}

	@Override
	public boolean plusPrefixSupported() {
		return false;
	}

	@Override
	public boolean minusPrefixSupported() {
		return false;
	}

	@Override
	public boolean plusPostfixSupported() {
		return false;
	}

	@Override
	public boolean minusPostfixSupported() {
		return false;
	}

	@Override
	public boolean getSupported() {
		return false;
	}

	@Override
	public boolean hasExtensionAttribute(String name) {
		if (name.equals("NAME")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasExtensionMethod(String name, int args) {
		if (name.equals("print")) {
			return true;
		}
		return false;
	}
		
	
}
