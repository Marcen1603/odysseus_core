package de.uniol.inf.is.odysseus.iql.odl.typing.typeoperators;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators.ITypeOperators;

public class TupleOperators implements ITypeOperators {

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

	
	@Override
	public Class<?> getType() {
		return Tuple.class;
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
		return true;
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
