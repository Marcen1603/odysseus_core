package de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.impl;

import java.util.Collection;

import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.ITypeOperators;

public class CollectionOperators implements ITypeOperators {
		
	public static <T extends Collection<Object>> T plus(T col, Object operand) {
		col.add(operand);
		return col;
	}
	
	public static <T extends Collection<Object>> T plus(T col, Collection<Object> operand) {
		col.addAll(operand);
		return col;
	}
	
	public static <T extends Collection<Object>> T minus(T col, Object operand) {
		col.remove(operand);
		return col;
	}
	
	public static <T extends Collection<Object>> T minus(T col, Collection<Object> operand) {
		col.removeAll(operand);
		return col;
	}
	
	@Override
	public Class<?> getType() {
		return Collection.class;
	}

	@Override
	public boolean plusSupported() {
		return true;
	}

	@Override
	public boolean minusSupported() {
		return true;
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
		return false;
	}


	@Override
	public boolean hasExtensionMethod(String name, int args) {
		return false;
	}
}
