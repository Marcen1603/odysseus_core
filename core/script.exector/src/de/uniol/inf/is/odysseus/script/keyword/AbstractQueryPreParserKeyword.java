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
package de.uniol.inf.is.odysseus.script.keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.executor.ExecutorHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.keyword.ParserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.keyword.TransCfgPreParserKeyword;

public abstract class AbstractQueryPreParserKeyword extends
		AbstractPreParserExecutorKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {
		try {
			IExecutor executor = ExecutorHandler.getExecutor();
			if (executor == null)
				throw new OdysseusScriptException("No executor found");

			if (parameter.length() == 0)
				throw new OdysseusScriptException("Encountered empty query");

			String parserID = (String) variables.get(ParserPreParserKeyword.PARSER);
			if (parserID == null)
				throw new OdysseusScriptException("Parser not set");

			String transCfg = (String) variables.get(TransCfgPreParserKeyword.TRANSCFG);
			if (transCfg == null)
				throw new OdysseusScriptException(
						"TransformationConfiguration not set");

		} catch (Exception ex) {
			throw new OdysseusScriptException("Exception in query validation ",
					ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {
		String parserID = (String) variables.get(ParserPreParserKeyword.PARSER);
		String transCfgName = (String) variables.get(TransCfgPreParserKeyword.TRANSCFG);
		List<IQueryBuildSetting<?>> addSettings = (List<IQueryBuildSetting<?>>) variables
				.get(TransCfgPreParserKeyword.ADD_TRANS_PARAMS);

		ISink defaultSink = variables.get("_defaultSink") != null ? (ISink) variables
				.get("_defaultSink") : null;
		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		roots.add(defaultSink);

		try {
			parserID = parserID.trim();
			transCfgName = transCfgName.trim();
			String queryText = parameter.trim();

			IExecutor executor = getExecutor();
			IServerExecutor serverExec = null;
			if (executor instanceof IServerExecutor) {
				serverExec = (IServerExecutor) executor;
			}

			Collection<Integer> queriesToStart = null;

			if (addSettings != null) {
				if (serverExec == null) {
					throw new QueryParseException(
							"Additional transformation parameter currently not supported on clients");
				} else {
					queriesToStart = serverExec.addQuery(queryText, parserID, caller,
							transCfgName,
							addSettings);
					// Append defaultSink to all queries
					// and make it query root
					if (defaultSink != null) {
						
						throw new QueryParseException("Default Sink Currently not supported!!");
						
//						for (ILogicalQuery lq : queries) {
//							if (lq instanceof IPhysicalQuery) {
//								IPhysicalQuery q = (IPhysicalQuery) lq;
//								for (IPhysicalOperator p : q.getRoots()) {
//									((ISource) p).subscribeSink(defaultSink, 0, 0,
//											p.getOutputSchema());
//								}
//								q.setRoots(roots);
//							}
//						}

					}
				}
			} else {
				executor.addQuery(queryText, parserID, caller,
						transCfgName);
			}



			if (startQuery()) {
				for (Integer q : queriesToStart) {
					executor.startQuery(q, caller);
				}
			}

			return queriesToStart;
		} catch (Exception ex) {
			throw new OdysseusScriptException("Query Execution Error", ex);
		}
	}

	protected abstract boolean startQuery();

}
