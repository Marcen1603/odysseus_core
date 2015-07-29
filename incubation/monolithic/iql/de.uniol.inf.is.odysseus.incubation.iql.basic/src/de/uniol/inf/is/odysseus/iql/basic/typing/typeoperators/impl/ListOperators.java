package de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iql.basic.types.Range;

public class ListOperators extends CollectionOperators {
	
	public static String NAME = "";

	@Override
	public Class<?> getType() {
		return List.class;
	}

	public static <T> T get(List<T> col, int index) {
		return col.get(index);
	}

	
	public static <T> List<T> get(List<T> col, Object ... indices) {
		List<T> result = new ArrayList<>();
		for (Object index : indices) {
			if (index instanceof Range) {
				Range range = (Range) index;
				int counter = range.getFrom();
				while (counter<= range.getTo()) {
					result.add(col.get(counter));
					counter++;
				}
			} else {
				result.add(col.get((int) index));
			}
		}
		return result;
	}
	
	@Override
	public boolean getSupported() {
		return true;
	}


	public static int length(List<?> list) {
		return list.size();
	}
	
	@Override
	public boolean hasExtensionAttribute(String name) {
		if (name.equalsIgnoreCase("NAME")) {
			return true;
		}
		return false;	
	}
	
	@Override
	public boolean hasExtensionMethod(String name, int args) {
		if (name.equalsIgnoreCase("length")) {
			return true;
		}
		return false;
	}
}
