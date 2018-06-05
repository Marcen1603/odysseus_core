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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AddQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterMaxSheddingFactor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterQueryName;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterQueryParams;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ACQueryParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterPriority;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.keyword.MaxSheddingFactorPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.NoMetadataPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.QueryNamePreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.QueryParamPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.QueryPriorityPreParserKeyword;

@SuppressWarnings("deprecation")
public abstract class AbstractQueryPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		try {
			if (parameter.length() == 0) {
				throw new OdysseusScriptException("Encountered empty query");
			}

			String parserID = (String) variables.get(ParserPreParserKeyword.PARSER);
			if (parserID == null) {
				throw new OdysseusScriptException("Parser not set");
			}

			String transCfg = (String) variables.get(TransCfgPreParserKeyword.TRANSCFG);
			if (transCfg == null) {
				variables.put(TransCfgPreParserKeyword.TRANSCFG, "Standard");
				// throw new
				// OdysseusScriptException("TransformationConfiguration not set");
			}

		} catch (Exception ex) {
			throw new OdysseusScriptException("Exception in query validation ", ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		String parserID = (String) variables.get(ParserPreParserKeyword.PARSER);
		String transCfgName = (String) variables.get(TransCfgPreParserKeyword.TRANSCFG);
		if (transCfgName == null) {
			transCfgName = "Standard";
		}

		List<IQueryBuildSetting<?>> addSettings = (List<IQueryBuildSetting<?>>) variables.get(AbstractPreParserKeyword.ADD_TRANS_PARAMS);
		if (addSettings == null) {
			addSettings = new ArrayList<>();
		}

		// CALLER
		ISession queryCaller = (ISession) variables.get("USER");
		if (queryCaller == null) {
			queryCaller = caller;
		}

		// QNAME
		String queryName = (String) variables.get(QueryNamePreParserKeyword.QNAME);
		if (queryName != null && queryName.trim().length() > 0) {
			addSettings.add(new ParameterQueryName(new Resource(queryCaller.getUser().getName(), queryName)));
			// Only for this query
			variables.remove(QueryNamePreParserKeyword.QNAME);
		}

		// QPRIORITY
		// TODO: Query Prio?

		// QPARAMS
		Map<String, String> queryParams = (Map<String, String>) variables.get(QueryParamPreParserKeyword.NAME);
		if (queryParams != null){
			addSettings.add(new ParameterQueryParams(queryParams));
			// Only for this query
			variables.remove(QueryParamPreParserKeyword.NAME);
		}

		// MAX SHEDDING FACTOR
		Integer maxSheddingFactor = (Integer)variables.get(MaxSheddingFactorPreParserKeyword.SHEDDING_FACTOR_NAME);
		if( maxSheddingFactor == null ) {
			maxSheddingFactor = 1;
		}
		addSettings.add(new ParameterMaxSheddingFactor(maxSheddingFactor));

		// PRIO
		if (variables.containsKey(QueryPriorityPreParserKeyword.NAME)) {
			try {
				int priority = Integer.parseInt((String) variables.get(QueryPriorityPreParserKeyword.NAME));

				addSettings.add(new ParameterPriority(priority));
			} catch (Exception e) {
				throw new OdysseusScriptException("Error reading priority " + variables.get(QueryPriorityPreParserKeyword.NAME));
			}
			// Only for this query
			variables.remove(QueryPriorityPreParserKeyword.NAME);

		}



		// AC-QUERIES
		if (isACQuery()){
			addSettings.add(new ACQueryParameter(isACQuery()));
		}

		try {
			parserID = parserID.trim();
			transCfgName = transCfgName.trim();
			String queryText = parameter.trim();

			List<IExecutorCommand> commands = new LinkedList<>();

			// Add rules to use to context
			if (variables.containsKey(ActivateRewriteRulePreParserKeyword.ACTIVATEREWRITERULE)) {
				context.put(ActivateRewriteRulePreParserKeyword.ACTIVATEREWRITERULE, new ArrayList<String>((ArrayList<String>) variables.get(ActivateRewriteRulePreParserKeyword.ACTIVATEREWRITERULE)));
			}
			if (variables.containsKey(DeActivateRewriteRulePreParserKeyword.DEACTIVATEREWRITERULE)) {
				context.put(DeActivateRewriteRulePreParserKeyword.DEACTIVATEREWRITERULE, new ArrayList<String>((ArrayList<String>) variables.get(DeActivateRewriteRulePreParserKeyword.DEACTIVATEREWRITERULE)));
			}

			// Metadata
			if (variables.containsKey(NoMetadataPreParserKeyword.NO_METADATA)){
				context.put(NoMetadataPreParserKeyword.NO_METADATA,"true");
			}


			AddQueryCommand cmd = new AddQueryCommand(queryText, parserID, queryCaller, transCfgName, context, addSettings, startQuery());
			commands.add(cmd);

			ISink defaultSink = variables.containsKey("_defaultSink") ? (ISink) variables.get("_defaultSink") : null;

			if (defaultSink != null) {
				throw new IllegalArgumentException("_Default Sink currently not supported!");
				// appendSinkToQueries(defaultSink, queriesToStart,
				// (IPlanManager) executor);
			}

			return commands;

		} catch (QueryParseException ex) {
			throw new OdysseusScriptException("Query could not be parsed!", ex);
		} catch (PlanManagementException ex) {
			throw new OdysseusScriptException("Adding new query failed!", ex);
		} catch (Exception ex) {
			throw new OdysseusScriptException("Error while executing Odysseus script during executing a query!", ex);
		}
	}

	protected boolean isACQuery() {
		return false;
	}

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// private static void appendSinkToQueries(ISink<?> defaultSink,
	// Iterable<Integer> queriesToStart, IPlanManager planManager) throws
	// PlanManagementException {
	// List<IPhysicalOperator> roots = Lists.<IPhysicalOperator>
	// newArrayList(defaultSink);
	//
	// for (Integer queryId : queriesToStart) {
	// IPhysicalQuery physicalQuery =
	// planManager.getExecutionPlan().getQueryById(queryId);
	// for (IPhysicalOperator operator : physicalQuery.getRoots()) {
	// ((ISource) operator).subscribeSink(defaultSink, 0, 0,
	// operator.getOutputSchema());
	// }
	// physicalQuery.setRoots(roots);
	// }
	// }

	protected abstract boolean startQuery();

}
