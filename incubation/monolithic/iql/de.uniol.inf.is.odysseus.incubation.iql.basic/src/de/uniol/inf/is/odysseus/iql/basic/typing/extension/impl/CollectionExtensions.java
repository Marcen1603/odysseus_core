package de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl;

import java.util.Collection;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class CollectionExtensions implements IIQLTypeExtensions {
	
	public static <T> Collection<T> plus(Collection<T> col, T operand) {
		col.add(operand);
		return col;
	}
	
	public static <T> Collection<T> plus(Collection<T> col, Collection<T> operand) {
		col.addAll(operand);
		return col;
	}
	
	public static <T> Collection<T> minus(Collection<T> col, T operand) {
		col.remove(operand);
		return col;
	}
	
	public static <T> Collection<T> minus(Collection<T> col, Collection<T> operand) {
		col.removeAll(operand);
		return col;
	}
	
	
	
	@Override
	public Class<?> getType() {
		return Collection.class;
	}
}
