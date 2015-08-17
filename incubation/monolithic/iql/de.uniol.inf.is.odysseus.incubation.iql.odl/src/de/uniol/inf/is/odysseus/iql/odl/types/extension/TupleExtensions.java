package de.uniol.inf.is.odysseus.iql.odl.types.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class TupleExtensions implements IIQLTypeExtensions {

	public static <T extends IMetaAttribute> Tuple<T> listToType(Object ... attributes) {
		return new Tuple<>(attributes, false);
	}
	
	public static <T extends IMetaAttribute> Tuple<T> rangeToType(int from, int to) {
		List<Object> attributes = new ArrayList<>();
		int counter = from;
		while (counter<= to) {
			attributes.add(counter);
			counter++;
		}
		return new Tuple<>(attributes.toArray(new Object[attributes.size()]), false);
	}
	
	public static <T extends IMetaAttribute> Tuple<T> booleanToType(boolean value) {
		return new Tuple<>(value, false);
	}
	
	public static <T extends IMetaAttribute> Tuple<T> stringToType(String value) {
		return new Tuple<>(value, false);
	}
	
	public static <T extends IMetaAttribute> Tuple<T> intToType(int value) {
		return new Tuple<>(value, false);
	}
	
	public static <T extends IMetaAttribute> Tuple<T> doubleToType(double value) {
		return new Tuple<>(value, false);
	}	
	
	public static <T extends IMetaAttribute> Object get(Tuple<T> tuple, int index) {
		return tuple.getAttribute(index);
	}
	
	public static <T extends IMetaAttribute> List<Object> get(Tuple<T> tuple, Object ... indices) {
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
	
	public static <T extends IMetaAttribute> void set(Tuple<T> tuple, Object value, int index) {
		tuple.setAttribute(index, value);
	}
	
	public static <T extends IMetaAttribute> void set(Tuple<T> tuple, Collection<Object> values,  Object ... indices) {
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
