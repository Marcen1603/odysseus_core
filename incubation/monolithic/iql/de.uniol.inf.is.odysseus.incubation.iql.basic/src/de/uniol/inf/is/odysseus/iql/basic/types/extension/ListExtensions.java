package de.uniol.inf.is.odysseus.iql.basic.types.extension;

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
	
	public static <T> List<T> get(List<T> col, List<Object> indices) {
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
	
	public static <T> List<T> get(List<T> col, Range range) {
		List<T> result = new ArrayList<>();
		int counter = range.getFrom();
		while (counter<= range.getTo()) {
			result.add(col.get(counter));
			counter++;
		}
		return result;
	}
	
	public static <T> void set(List<T> col, T element, int index) {
		try {
			col.set(index, element);
		} catch (Exception e) {
			col.add(index, element);
		}
	}
	
	public static <T> void set(List<T> col, Collection<T> elements, Range range) {
		Iterator<T> it = elements.iterator();
		int counter = range.getFrom();
		while (counter<= range.getTo()) {
			try {
				col.set(counter, it.next());
			} catch (Exception e) {
				col.add(counter, it.next());
			}
			counter++;
		}
	}
	
	public static <T> void set(List<T> col, Collection<T> elements, List<Object> indices) {
		Iterator<T> it = elements.iterator();
		for (Object index : indices) {
			if (index instanceof Range) {
				Range range = (Range) index;
				int counter = range.getFrom();
				while (counter<= range.getTo()) {
					try {
						col.set(counter, it.next());
					} catch (Exception e) {
						col.add(counter, it.next());
					}
					counter++;
				}
			} else {
				try {
					col.set((int) index, it.next());
				} catch (Exception e) {
					col.add((int) index, it.next());
				}
			}
		}
	}
	
	public static <T> int length(List<T> list) {
		return list.size();
	}

}
