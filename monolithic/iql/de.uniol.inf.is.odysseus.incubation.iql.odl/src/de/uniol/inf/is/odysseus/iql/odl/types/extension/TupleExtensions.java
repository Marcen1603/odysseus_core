package de.uniol.inf.is.odysseus.iql.odl.types.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.ExtensionMethod;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class TupleExtensions implements IIQLTypeExtensions {

	@SuppressWarnings("rawtypes")
	@ExtensionMethod(ignoreFirstParameter=false)
	public static <T extends IMetaAttribute> Tuple<T> listToType(List attributes) {
		return new Tuple<>(attributes.toArray(), false);
	}
	
	@SuppressWarnings("rawtypes")
	@ExtensionMethod(ignoreFirstParameter=false)
	public static <T extends IMetaAttribute> Tuple<T> mapToType(Map attributes) {
		return new Tuple<>(attributes, false);
	}
	
	@ExtensionMethod(ignoreFirstParameter=false)
	public static <T extends IMetaAttribute> Tuple<T> rangeToType(Range range) {
		List<Object> attributes = new ArrayList<>();
		int counter = range.getFrom();
		while (counter<= range.getTo()) {
			attributes.add(counter);
			counter++;
		}
		return new Tuple<>(attributes.toArray(new Object[attributes.size()]), false);
	}
	
	@ExtensionMethod(ignoreFirstParameter=false)
	public static <T extends IMetaAttribute> Tuple<T> booleanToType(boolean value) {
		return new Tuple<>(value, false);
	}
	
	@ExtensionMethod(ignoreFirstParameter=false)
	public static <T extends IMetaAttribute> Tuple<T> stringToType(String value) {
		return new Tuple<>(value, false);
	}
	
	@ExtensionMethod(ignoreFirstParameter=false)
	public static <T extends IMetaAttribute> Tuple<T> intToType(int value) {
		return new Tuple<>(value, false);
	}
	
	@ExtensionMethod(ignoreFirstParameter=false)
	public static <T extends IMetaAttribute> Tuple<T> doubleToType(double value) {
		return new Tuple<>(value, false);
	}	
	
	public static <T extends IMetaAttribute> Object get(Tuple<T> tuple, int index) {
		return tuple.getAttribute(index);
	}
	
	public static <T extends IMetaAttribute> Tuple<T> get(Tuple<T> tuple, Range range) {
		int[] attrList = new int[range.getTo()-range.getFrom()+1];
		int counter = range.getFrom();
		int i = 0;
		while (counter<= range.getTo()) {
			attrList[i++] = counter;
			counter++;
		}
		return tuple.restrict(attrList, true);
	}
	
	
	public static <T extends IMetaAttribute> Tuple<T> get(Tuple<T> tuple, List<Object> indices) {
		List<Integer> attrList = new ArrayList<>();
		for (Object index : indices) {
			if (index instanceof Range) {
				Range range = (Range) index;
				int counter = range.getFrom();
				while (counter<= range.getTo()) {
					attrList.add(counter);
					counter++;
				}
			} else {
				attrList.add((Integer) index);
			}
		}
		
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
		int[] attributes = new int[attrList.size()];
		for (int i = 0 ; i< attrList.size(); i++) {
			attributes[i] = attrList.get(i);
		}
		return tuple.restrict(attributes, true);
	}
	
	public static <T extends IMetaAttribute> void set(Tuple<T> tuple, Object value, int index) {
		tuple.setAttribute(index, value);
	}
	
	public static <T extends IMetaAttribute> void set(Tuple<T> tuple, Collection<Object> values,  List<Object> indices) {
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
	
	public static <T extends IMetaAttribute> void plus(Tuple<T> tuple, List<Object> values) {
		for (Object value : values) {
			tuple.append(value);
		}
	}
	
	public static <T extends IMetaAttribute> void plus(Tuple<T> tuple, Object value) {
		tuple.append(value);
	}


	
	@Override
	public Class<?> getType() {
		return Tuple.class;
	}

}
