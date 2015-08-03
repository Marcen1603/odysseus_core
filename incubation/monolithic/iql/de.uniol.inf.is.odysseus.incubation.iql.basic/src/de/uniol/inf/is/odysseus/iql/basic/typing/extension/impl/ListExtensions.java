package de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.iql.basic.types.Range;

public class ListExtensions extends CollectionExtensions {
	
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
	
	public static <T> T set(List<T> col, T element, int index) {
		return col.set(index, element);
	}
	
	public static <T> List<T> set(List<T> col, Collection<T> elements, Object ... indices) {
		Iterator<T> it = elements.iterator();
		for (Object index : indices) {
			if (index instanceof Range) {
				Range range = (Range) index;
				int counter = range.getFrom();
				while (counter<= range.getTo()) {
					col.set(counter, it.next());
					counter++;
				}
			} else {
				col.set((int) index, it.next());
			}
		}
		return col;
	}

	
	public static <T> List<T> set(List<T> col, Object ... indices) {
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
}
