package de.uniol.inf.is.odysseus.args.marshaller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.args.ArgsException;

/**
 * @author Jonas Jacobi
 */
public class NumberMarshaller<T extends Number> implements
		IParameterMarshaller {
	T value;
	private Class<T> type;

	public NumberMarshaller(Class<T> type) {
		this.type = type;
	}

	@Override
	public T getValue() {
		return this.value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void parse(ListIterator<String> args) throws ArgsException {
		if (!args.hasNext()) {
			throw new ArgsException("missing parameter");
		}

		String value = args.next();
		try {
			Method method = type.getMethod("valueOf", String.class);
			this.value = (T) method.invoke(null, value);
		} catch (InvocationTargetException e) {
			throw new ArgsException("illegal format for " + type.getSimpleName() + " parameter");
		} catch (Exception e) {
		}
	}
}
