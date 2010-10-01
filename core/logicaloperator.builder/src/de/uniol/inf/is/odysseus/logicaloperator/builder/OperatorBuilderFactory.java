package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.usermanagement.User;

public class OperatorBuilderFactory {
	private static Map<String, Class<? extends IOperatorBuilder>> operatorBuilders = new HashMap<String, Class<? extends IOperatorBuilder>>();
	private static Map<String, IPredicateBuilder> predicateBuilders = new HashMap<String, IPredicateBuilder>();

	@SuppressWarnings("unchecked")
	public static <T extends IOperatorBuilder> T createOperatorBuilder(
			String name, User caller) {
		name = name.toUpperCase();
		if (!operatorBuilders.containsKey(name)) {
			throw new IllegalArgumentException("no such operator builder: "
					+ name);
		}
		try {
			T builder = (T)operatorBuilders.get(name).newInstance();
			builder.setCaller(caller);
			return builder; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean containsOperatorBuilderType(String name) {
		return operatorBuilders.containsKey(name.toUpperCase());
	}

	public static Set<String> getOperatorBuilderNames() {
		return operatorBuilders.keySet();
	}

	public static void putOperatorBuilderType(String name,
			Class<? extends IOperatorBuilder> builder) {
		operatorBuilders.put(name.toUpperCase(), builder);
	}

	public static void removeOperatorBuilderType(String name) {
		operatorBuilders.remove(name.toUpperCase());
	}
	
	public static void putPredicateBuilder(String identifier,
			IPredicateBuilder builder) {
		if (predicateBuilders.containsKey(identifier)) {
			throw new IllegalArgumentException(
					"multiple definitions of predicate builder: " + identifier);
		}

		predicateBuilders.put(identifier, builder);
	}

	public static void removePredicateBuilder(String identifier) {
		predicateBuilders.remove(identifier);
	}

	public static IPredicateBuilder getPredicateBuilder(String predicateType) {
		return predicateBuilders.get(predicateType);
	}
}
