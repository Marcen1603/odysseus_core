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
package de.uniol.inf.is.odysseus.script.keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanManager;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterQueryName;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.executor.ExecutorHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.keyword.QueryNamePreParserKeyword;

public abstract class AbstractQueryPreParserKeyword extends AbstractPreParserExecutorKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		try {
			IExecutor executor = ExecutorHandler.getServerExecutor();
			if (executor == null) {
				throw new OdysseusScriptException("No executor found");
			}

			if (parameter.length() == 0) {
				throw new OdysseusScriptException("Encountered empty query");
			}

			String parserID = (String) variables.get(ParserPreParserKeyword.PARSER);
			if (parserID == null) {
				throw new OdysseusScriptException("Parser not set");
			}

			String transCfg = (String) variables.get(TransCfgPreParserKeyword.TRANSCFG);
			if (transCfg == null) {
				throw new OdysseusScriptException("TransformationConfiguration not set");
			}

		} catch (Exception ex) {
			throw new OdysseusScriptException("Exception in query validation ", ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		String parserID = (String) variables.get(ParserPreParserKeyword.PARSER);
		String transCfgName = (String) variables.get(TransCfgPreParserKeyword.TRANSCFG);
		List<IQueryBuildSetting<?>> addSettings = (List<IQueryBuildSetting<?>>) variables.get(TransCfgPreParserKeyword.ADD_TRANS_PARAMS);

		ISession queryCaller = (ISession) variables.get("USER");
		String queryName = (String) variables.get(QueryNamePreParserKeyword.QNAME);
		if (queryName != null && queryName.trim().length() > 0) {
			if (addSettings == null) {
				addSettings = new ArrayList<>();
			}
			addSettings.add(new ParameterQueryName(queryName));
		}
		if (queryCaller == null) {
			queryCaller = caller;
		}

		try {
			parserID = parserID.trim();
			transCfgName = transCfgName.trim();
			String queryText = parameter.trim();

			IExecutor executor = getServerExecutor();

			Collection<Integer> queriesToStart = null;
			
			// TODO: Create context!
			Context context = null;
			
			if (addSettings != null) {
				if (!(executor instanceof IServerExecutor)) {
					throw new QueryParseException("Additional transformation parameter currently not supported on clients");
				}
				queriesToStart = ((IServerExecutor) executor).addQuery(queryText, parserID, queryCaller, transCfgName, context, addSettings);

			} else {
				queriesToStart = executor.addQuery(queryText, parserID, queryCaller, transCfgName, context);
			}

			ISink defaultSink = variables.containsKey("_defaultSink") ? (ISink) variables.get("_defaultSink") : null;
			if (defaultSink != null && executor instanceof IPlanManager) {
				appendSinkToQueries(defaultSink, queriesToStart, (IPlanManager) executor);
			}

			if (startQuery()) {
				for (Integer q : queriesToStart) {
					try {
						executor.startQuery(q, queryCaller);
					} catch (Throwable t) {
						throw new OdysseusScriptException("Query could not be started!", t);
					}
				}
			}

			return queriesToStart;
		} catch (QueryParseException ex) {
			throw new OdysseusScriptException("Query could not be parsed!", ex);
		} catch (PlanManagementException ex) {
			throw new OdysseusScriptException("Adding new query failed!", ex);
		} catch (Exception ex) {
			throw new OdysseusScriptException("Error while executing Odysseus script during executing a query!", ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void appendSinkToQueries(ISink<?> defaultSink, Iterable<Integer> queriesToStart, IPlanManager planManager) throws PlanManagementException {
		List<IPhysicalOperator> roots = Lists.<IPhysicalOperator> newArrayList(defaultSink);

		for (Integer queryId : queriesToStart) {
			IPhysicalQuery physicalQuery = planManager.getExecutionPlan().getQueryById(queryId);
			for (IPhysicalOperator operator : physicalQuery.getRoots()) {
				((ISource) operator).subscribeSink(defaultSink, 0, 0, operator.getOutputSchema());
			}
			physicalQuery.setRoots(roots);
		}
	}

	protected abstract boolean startQuery();

}
