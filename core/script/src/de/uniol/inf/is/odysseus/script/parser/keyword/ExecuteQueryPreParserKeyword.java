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
package de.uniol.inf.is.odysseus.script.parser.keyword;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.script.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class ExecuteQueryPreParserKeyword extends AbstractQueryPreParserKeyword {

	// Query selbst ausf�hren...
	@Override
	protected Object exec(String parserID, String transCfg, String queryText, User caller, IDataDictionary dd) throws PlanManagementException {
		parserID = parserID.trim();
		transCfg = transCfg.trim();
		queryText = queryText.trim();
		// Query aufsplitten ";"
		/* DGe: das sollte nicht unbedingt hier gemacht werden: das problem ist, dass
		 * z.B. auch andere identifier ein ; enthalten k�nnen und dann kommt nur die h�lfte an
		 * das wird auch eh vom Parser noch mal getrennt!
		 */
//		StringTokenizer queriesTokenizer = new StringTokenizer(queryText, ";");
//		Collection<IQuery> queries = new LinkedList<IQuery>();
//		while (queriesTokenizer.hasMoreElements()) {
//			String q = queriesTokenizer.nextToken();
//			if (q.length() > 0) {
//				queries.addAll(ExecutorHandler.getExecutor().addQuery(q,
//						parserID, caller, dd, transCfg));
//			}
//		}	
//		
//		for (IQuery q:queries){
//			ExecutorHandler.getExecutor().startQuery(q.getID(), caller);
//		}
//		return queries;
		
		return ExecutorHandler.getExecutor().addQuery(queryText, parserID, caller, dd, transCfg);		
	}

}
