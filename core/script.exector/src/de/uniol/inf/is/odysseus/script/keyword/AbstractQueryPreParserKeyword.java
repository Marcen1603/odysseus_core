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

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.script.executor.ExecutorHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

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

			String parserID = (String) variables.get("PARSER");
			if (parserID == null)
				throw new OdysseusScriptException("Parser not set");

			String transCfg = (String) variables.get("TRANSCFG");
			if (transCfg == null)
				throw new OdysseusScriptException(
						"TransformationConfiguration not set");

		} catch (Exception ex) {
			throw new OdysseusScriptException(
					"Exception in query validation ", ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {
		String parserID = (String) variables.get("PARSER");
		String transCfg = (String) variables.get("TRANSCFG");
		ISink defaultSink = variables.get("_defaultSink") != null?(ISink)variables.get("_defaultSink"):null; 
		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		roots.add(defaultSink);
		
		try {
			parserID = parserID.trim();
			transCfg = transCfg.trim();
			String queryText = parameter.trim();
			IDataDictionary dd = getDataDictionary();

			Collection<IQuery> queries = ExecutorHandler.getExecutor().addQuery(
					queryText, parserID, caller, dd, transCfg);
			
			// Append defaultSink to all queries
			// and make it query root
			if (defaultSink != null){
				for (IQuery q: queries){
					for (IPhysicalOperator p: q.getRoots()){
						((ISource)p).subscribeSink(defaultSink, 0, 0, p.getOutputSchema());
					}
					q.setRoots(roots);
				}
				
			}
			
			if (startQuery()) {
				for (IQuery q : queries) {
					ExecutorHandler.getExecutor().startQuery(q.getID(), caller);
				}
			}
			
			return queries;
		} catch (Exception ex) {
			throw new OdysseusScriptException("Query Execution Error",
					ex);
		}
	}
	
	protected abstract boolean startQuery();

}
