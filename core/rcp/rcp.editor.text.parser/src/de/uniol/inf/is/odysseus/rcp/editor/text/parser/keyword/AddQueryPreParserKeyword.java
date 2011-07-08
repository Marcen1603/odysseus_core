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
package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class AddQueryPreParserKeyword extends AbstractQueryPreParserKeyword {

	// Query selbst ausführen...
	@Override
	protected Object exec(String parserID, String transCfg, String queryText, User caller, IDataDictionary dd) throws PlanManagementException {
		parserID = parserID.trim();
		transCfg = transCfg.trim();
		queryText = queryText.trim();

		final List<IQueryBuildSetting<?>> cfg = ExecutorHandler.getExecutor().getQueryBuildConfiguration(transCfg);
		return ExecutorHandler.getExecutor().addQuery(queryText, parserID, caller, dd, cfg.toArray(new IQueryBuildSetting[0]) );
	}

}
