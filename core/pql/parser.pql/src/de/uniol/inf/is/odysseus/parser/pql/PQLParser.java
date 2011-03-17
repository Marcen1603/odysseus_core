/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.parser.pql;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImpl;
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class PQLParser implements IQueryParser {

	private PQLParserImpl parser;

	private User user;

	private IDataDictionary dataDictionary;

	private static Map<String, IParameter<?>> queryParameters = new HashMap<String, IParameter<?>>();

	@Override
	public String getLanguage() {
		return "PQL";
	}

	@Override
	public synchronized List<IQuery> parse(String query, User user, IDataDictionary dd)
			throws QueryParseException {
		this.user = user;
		this.dataDictionary = dd;
		PQLParserImpl.setUser(user);
		PQLParserImpl.setDataDictionary(dd);
		return parse(new StringReader(query),user, dd);
	}

	@Override
	public synchronized List<IQuery> parse(Reader reader, User user, IDataDictionary dd)
			throws QueryParseException {
		this.user = user;
		this.dataDictionary = dd;
		PQLParserImpl.setUser(user);
		PQLParserImpl.setDataDictionary(dd);
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
