package de.uniol.inf.is.odysseus.parser.pql;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImpl;

public class PQLParser implements IQueryParser {

	private PQLParserImpl parser;

	private static Map<String, IPredicateBuilder> predicateBuilder = new HashMap<String, IPredicateBuilder>();
	private static Map<String, IParameter<?>> queryParameters = new HashMap<String, IParameter<?>>();

	@Override
	public String getLanguage() {
		return "PQL";
	}

	@Override
	public synchronized List<IQuery> parse(String query)
			throws QueryParseException {
		return parse(new StringReader(query));
	}

	@Override
	public synchronized List<IQuery> parse(Reader reader)
			throws QueryParseException {
		try {
			if (this.parser == null) {
				try {
					this.parser = new PQLParserImpl(reader);
				} catch (Error e) {
					PQLParserImpl.ReInit(reader);
				}
			} else {
				PQLParserImpl.ReInit(reader);
			}

			List<IQuery> queries = PQLParserImpl.query();

			for (IQuery query : queries) {
				query.setParserId(getLanguage());
			}
			return queries;
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}

	public static void addOperatorBuilder(String identifier,
			IOperatorBuilder builder) {
		PQLParserImpl.addOperatorBuilder(identifier, builder);
	}

	public static void addPredicateBuilder(String identifier,
			IPredicateBuilder builder) {
		if (predicateBuilder.containsKey(identifier)) {
			throw new IllegalArgumentException(
					"multiple definitions of predicate builder: " + identifier);
		}

		predicateBuilder.put(identifier, builder);
	}

	public static void removeOperatorBuilder(String identifier) {
		predicateBuilder.remove(identifier);
	}

	public static IPredicateBuilder getPredicateBuilder(String predicateType) {
		return predicateBuilder.get(predicateType);
	}

	public static void addQueryParameter(IParameter<?> parameter) {
		String parameterName = parameter.getName();
		if (queryParameters.containsKey(parameterName)) {
			throw new IllegalArgumentException(
					"multiple definitions of query parameter: " + parameterName);
		}

		queryParameters.put(parameterName, parameter);
	}

	public static void removeQueryParameter(String identifier) {
		queryParameters.remove(identifier);
	}

	public static void initQueryParameters(Map<String, Object> parameterValues) {
		initParameters(queryParameters.values(), parameterValues);
	}

	public static IParameter<?> getQueryParameter(String name) {
		return queryParameters.get(name);
	}

	public static void initParameters(
			Collection<IParameter<?>> parameterObjects,
			Map<String, Object> parameterValues) {
		Map<String, Object> tmpParameters = new HashMap<String, Object>(
				parameterValues);
		for (IParameter<?> parameter : parameterObjects) {
			String parameterName = parameter.getName();
			boolean hasParameter = tmpParameters.containsKey(parameterName);
			if (!hasParameter) {
				parameter.setNoValueAvailable();
				if (parameter.isMandatory()) {
					throw new IllegalArgumentException(
							"missing mandatory parameter: " + parameterName);
				}
			} else {
				Object value = parameterValues.get(parameterName);
				parameter.setValueOf(value);
				tmpParameters.remove(parameterName);
			}
		}
		if (!tmpParameters.isEmpty()) {
			throw new IllegalArgumentException("unsupported parameters: "
					+ tmpParameters.keySet());
		}
	}

}
