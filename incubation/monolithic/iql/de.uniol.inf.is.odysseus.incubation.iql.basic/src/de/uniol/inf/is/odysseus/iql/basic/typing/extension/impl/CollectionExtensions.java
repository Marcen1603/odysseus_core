package de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl;

import java.util.Collection;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class CollectionExtensions implements IIQLTypeExtensions {
		
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
}
