package de.uniol.inf.is.odysseus.iql.odl.typing.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class TupleExtensions implements IIQLTypeExtensions {

	@SuppressWarnings("rawtypes")
	public static Object get(Tuple tuple, int index) {
		return tuple.getAttribute(index);
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Object> get(Tuple tuple, Object ... indices) {
		List<Object> result = new ArrayList<>();
		for (Object index : indices) {
			if (index instanceof Range) {
				Range range = (Range) index;
				int counter = range.getFrom();
				while (counter<= range.getTo()) {
					result.add( tuple.getAttribute(counter));
					counter++;
				}
			} else {
				result.add(tuple.getAttribute((int) index));
			}
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static void set(Tuple tuple, Object value, int index) {
		tuple.setAttribute(index, value);
	}
	
	@SuppressWarnings("rawtypes")
	public static void set(Tuple tuple, Collection<Object> values,  Object ... indices) {
		Iterator<Object> it = values.iterator();
		for (Object index : indices) {
			if (index instanceof Range) {
				Range range = (Range) index;
				int counter = range.getFrom();
				while (counter<= range.getTo()) {
					tuple.setAttribute(counter, it.next());
					counter++;
				}
			} else {
				tuple.setAttribute((int) index, it.next());
			}
		}
	}


	
	@Override
	public Class<?> getType() {
		return Tuple.class;
	}

}
