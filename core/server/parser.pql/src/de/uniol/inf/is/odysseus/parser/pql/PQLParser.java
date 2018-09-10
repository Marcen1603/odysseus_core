/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.event.error.ParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateStreamCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateViewCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImpl;
import de.uniol.inf.is.odysseus.parser.pql.impl.ParseException;

public class PQLParser implements IQueryParser {

	private PQLParserImpl parser;

	private static Map<String, IParameter<?>> queryParameters = new HashMap<String, IParameter<?>>();

	@Override
	public String getLanguage() {
		return "PQL";
	}

	@Override
	public synchronized List<IExecutorCommand> parse(String query,
			ISession user, IDataDictionary dd, Context context, IMetaAttribute metaAttribute, IServerExecutor executor)
			throws QueryParseException {
		return parse(new StringReader(query), user, dd, context, metaAttribute, executor);
	}

	private synchronized List<IExecutorCommand> parse(Reader reader,
			ISession user, IDataDictionary dd, Context context, IMetaAttribute metaAttribute, IServerExecutor executor)
			throws QueryParseException {
		// this.user = user;
		// this.dataDictionary = dd;
		PQLParserImpl.setUser(user);
		PQLParserImpl.setDataDictionary(dd);
		PQLParserImpl.setContext(context);
		PQLParserImpl.setMetaAttribute(metaAttribute);
		PQLParserImpl.setServerExecutor(executor);

		boolean updateQueryId = true;
		if (context != null && context.containsKey("tempQuery")) {
			updateQueryId = !(Boolean) context.get("tempQuery");
		}
		PQLParserImpl.setUpdateQueryID(updateQueryId);
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

			List<IExecutorCommand> queries = PQLParserImpl.query();
			String foundViewCommand = "";

			for (IExecutorCommand cmd : queries) {
				if (cmd instanceof CreateQueryCommand) {
					((CreateQueryCommand) cmd).getQuery().setParserId(
							getLanguage());
					((CreateQueryCommand) cmd).getQuery().setUser(user);
				} else if (cmd instanceof CreateViewCommand
						|| cmd instanceof CreateStreamCommand) {
					String found = getName(cmd);
					if (foundViewCommand.length() > 0) {
						throw new QueryParseException(
								"In PQL only one view/stream can be defined per query. After "
										+ foundViewCommand + " found "+ found);
					}
					foundViewCommand = getName(cmd);
				}
			}
			return queries;
		} catch (ParseException e) {
			String message = "PQL could not be correctly parsed!";
			message = message + System.lineSeparator();
			message = message + "This is, because PQL found \""
					+ e.currentToken.next + "\" after \"" + e.currentToken
					+ "\" and \"" + e.currentToken.next
					+ "\" is not an allowed value here!";
			message = message + System.lineSeparator();
			message = message
					+ "However, PQL exspects one of the following tokens after \""
					+ e.currentToken + "\": ";
			int line = e.currentToken.next.beginLine;
			int column = e.currentToken.next.beginColumn;
			String sep = "";
			for (int[] expectedSequence : e.expectedTokenSequences) {
				for (int token : expectedSequence) {
					message = message + sep + e.tokenImage[token];
					sep = ",";
				}
			}
			QueryParseException qpe = new QueryParseException(message);
			qpe.setColumn(column);
			qpe.setLine(line);
			throw qpe;
		} catch (QueryParseException ex) {
			throw ex;
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}

	private String getName(IExecutorCommand cmd) {
		if (cmd instanceof CreateStreamCommand){
			return ((CreateStreamCommand)cmd).getName();
		}
		if (cmd instanceof CreateViewCommand){
			return ((CreateViewCommand)cmd).getName();
		}
		return "";
	}

	// public static void addOperatorBuilder(String identifier,
	// IOperatorBuilder builder) {
	// PQLParserImpl.addOperatorBuilder(identifier, builder);
	// }
	//
	// public static Set<String> getOperatorBuilderNames() {
	// return PQLParserImpl.getOperatorBuilderNames();
	// }
	//
	// public static IOperatorBuilder getOperatorBuilder(String name) {
	// return PQLParserImpl.getOperatorBuilder(name);
	// }
	//
	// public static void removeOperatorBuilder(String identifier) {
	// PQLParserImpl.removeOperatorBuilder(identifier);
	// }

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
		initParameters(" unknown ", queryParameters.values(), parameterValues);
	}

	public static IParameter<?> getQueryParameter(String name) {
		return queryParameters.get(name);
	}

	public static void initParameters(String operatorName,
			Collection<IParameter<?>> parameterObjects,
			Map<String, Object> parameterValues) {
		Map<String, Object> tmpParameters = new HashMap<String, Object>(
				parameterValues);
		List<String> errors = new LinkedList<String>();
		for (IParameter<?> parameter : parameterObjects) {
			String parameterName = parameter.getName();
			boolean hasParameter = tmpParameters.containsKey(parameterName);
			// check for alias parameter
			if (!hasParameter){
				parameterName = parameter.getAliasName();
				hasParameter = tmpParameters.containsKey(parameterName);
			}

			if (!hasParameter) {
				parameter.setInputValue(null);
				if (parameter.isMandatory()) {
					errors.add("missing mandatory parameter: " + parameterName
							+ " for " + operatorName);
				}
			} else {
				Object value = parameterValues.get(parameterName);
				parameter.setInputValue(value);
				tmpParameters.remove(parameterName);
			}
		}
		if (!tmpParameters.isEmpty()) {
			errors.add("unsupported parameters: " + tmpParameters.keySet());
		}
		if (!errors.isEmpty()) {
			StringBuffer eText = new StringBuffer();
			for (String e : errors) {
				eText.append(e).append("\n");
			}
			throw new ParameterException("Parameter error " + eText);
		}
	}

	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		Map<String, List<String>> tokens = new HashMap<>();
		return tokens;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		return new ArrayList<>();
	}
}
