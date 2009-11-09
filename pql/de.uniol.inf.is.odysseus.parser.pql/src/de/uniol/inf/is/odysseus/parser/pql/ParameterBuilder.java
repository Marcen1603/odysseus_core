package de.uniol.inf.is.odysseus.parser.pql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.parser.pql.Parameter.REQUIREMENT;

/**
 * @author Jonas Jacobi
 */
@SuppressWarnings("unchecked")
public class ParameterBuilder {
	private static class DirectFactory<T> implements IParameterFactory<T> {

		private Class<T> type;

		public DirectFactory(Class<T> type) {
			this.type = type;
		}

		@Override
		public Parameter<T> createParameter(String name, REQUIREMENT requirement) {
			return new DirectParameter<T>(name, type, requirement);
		}

	}

	static private Map<Class<?>, IParameterFactory<?>> factories = new HashMap<Class<?>, IParameterFactory<?>>();
	static {
		factories.put(String.class, new DirectFactory<String>(String.class));
		factories.put(Integer.class, new DirectFactory<Integer>(Integer.class));
		factories.put(Float.class, new DirectFactory<Float>(Float.class));
		factories.put(Double.class, new DirectFactory<Double>(Double.class));
		factories.put(Byte.class, new DirectFactory<Byte>(Byte.class));
		factories.put(Boolean.class, new DirectFactory<Boolean>(Boolean.class));
		factories.put(Character.class, new DirectFactory<Character>(Character.class));
		factories.put(List.class, new DirectFactory<List>(List.class));
	}

	public ParameterBuilder() {

	}

	public <T> Parameter<T> buildParameter(String name, Class<T> type,
			Parameter.REQUIREMENT requirement) {
		if(!factories.containsKey(type)){
			throw new IllegalArgumentException("no factory for type " + type.getName());
		}
		IParameterFactory<T> factory = (IParameterFactory<T>) factories.get(type);
		
		return factory.createParameter(name, requirement);
	}
}
