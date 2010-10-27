package de.uniol.inf.is.odysseus.parser.pql;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImpl;
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class PQLParser implements IQueryParser {

	private PQLParserImpl parser;

	private User user;

	private static Map<String, IParameter<?>> queryParameters = new HashMap<String, IParameter<?>>();

	@Override
	public String getLanguage() {
		return "PQL";
	}

	@Override
	public synchronized List<IQuery> parse(String query, User user)
			throws QueryParseException {
		this.user = user;
		PQLParserImpl.setUser(user);
		return parse(new StringReader(query),user);
	}

	@Override
	public synchronized List<IQuery> parse(Reader reader, User user)
			throws QueryParseException {
		this.user = user;
		PQLParserImpl.setUser(user);
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

//	public static void addOperatorBuilder(String identifier,
//			IOperatorBuilder builder) {
//		PQLParserImpl.addOperatorBuilder(identifier, builder);
//	}
//
//	public static Set<String> getOperatorBuilderNames() {
//		return PQLParserImpl.getOperatorBuilderNames();
//	}
//	
//	public static IOperatorBuilder getOperatorBuilder(String name) {
//		return PQLParserImpl.getOperatorBuilder(name);
//	}
//
//	public static void removeOperatorBuilder(String identifier) {
//		PQLParserImpl.removeOperatorBuilder(identifier);
//	}

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
				parameter.setInputValue(null);
				if (parameter.isMandatory()) {
					throw new IllegalArgumentException(
							"missing mandatory parameter: " + parameterName);
				}
			} else {
				Object value = parameterValues.get(parameterName);
				parameter.setInputValue(value);
				tmpParameters.remove(parameterName);
			}
		}
		if (!tmpParameters.isEmpty()) {
			throw new IllegalArgumentException("unsupported parameters: "
					+ tmpParameters.keySet());
		}
	}

}
