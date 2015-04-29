package de.uniol.inf.is.odysseus.condition.conditionql.registry;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.condition.conditionql.parser.ConditionQLQuery;
import de.uniol.inf.is.odysseus.condition.conditionql.parser.IConditionQLParser;
import de.uniol.inf.is.odysseus.condition.conditionql.parser.annotations.ConditionQL;
import de.uniol.inf.is.odysseus.condition.conditionql.parser.enums.ConditionAlgorithm;

public class ConditionQLParserRegistry {

	private static Map<ConditionAlgorithm, IConditionQLParser> conditionQLParserMap = Maps.newHashMap();

	/**
	 * Add all the parsers to a map. This method is called by OSGi.
	 * 
	 * @param parser
	 *            The parser to add
	 */
	public static void registerConditionQLParser(IConditionQLParser parser) {
		ConditionQL conditionQLAnnotation = parser.getClass().getAnnotation(ConditionQL.class);
		Preconditions.checkNotNull(conditionQLAnnotation, "No ConditionQL annotation was set in "
				+ parser.getClass().getSimpleName());
		ConditionAlgorithm algorithm = conditionQLAnnotation.conditionAlgorithm();
		conditionQLParserMap.put(algorithm, parser);
	}

	public static void unregisterConditionQLParser(IConditionQLParser parser) {
		System.out.println("I was unregistered");
	}

	public static IConditionQLParser getConditionQLParser(ConditionAlgorithm conditionAlgorithm) {
		IConditionQLParser parser = conditionQLParserMap.get(conditionAlgorithm);
		Preconditions.checkNotNull(parser, "No ConditionQLParser was found for requestMessageType "
				+ conditionAlgorithm);
		return parser;
	}

	public static IConditionQLParser getConditionQLParser(String conditionAlgorithmString) {
		ConditionAlgorithm conditionAlgorithm;
		try {
			conditionAlgorithm = ConditionAlgorithm.valueOf(conditionAlgorithmString.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("No ConditionQLParser was found for requestMessageType " + conditionAlgorithmString);
		}

		return getConditionQLParser(conditionAlgorithm);
	}

	/**
	 * Converts a ConditionQL query to the corresponding object
	 * 
	 * @param conditionQL
	 *            The ConditionQL Query
	 * @return An ConditionQL-object
	 */
	public static ConditionQLQuery createConditionQLQuery(String conditionQL) {
		ConditionQLQuery conditionQLQuery = null;
		try {
			Gson gson = new Gson();
			conditionQLQuery = gson.fromJson(conditionQL, ConditionQLQuery.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conditionQLQuery;
	}

}
