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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public class QuerySharingPreParserKeyword extends AbstractPreParserExecutorKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller)
			throws OdysseusScriptException {
		IExecutor executor = getExecutor();
		if (executor == null)
			throw new OdysseusScriptException("No executor found");
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller)
			throws OdysseusScriptException {
		IExecutor executor = getExecutor();
		if (executor == null)
			throw new OdysseusScriptException("No executor found");
		List<IQueryBuildSetting<?>> config = executor.getQueryBuildConfiguration((String)
						variables.get("TRANSCFG")).getConfiguration();
		Iterator<IQueryBuildSetting<?>> iter = config.iterator();
		if (iter != null){
			while (iter.hasNext()) {
				IQueryBuildSetting<?> sett = iter.next();
				if (sett instanceof ParameterPerformQuerySharing) {
					iter.remove();
					break;
				}
			}
			if ("TRUE".equals(parameter.toUpperCase())){
				config.add(ParameterPerformQuerySharing.TRUE);
			}else{
				config.add(ParameterPerformQuerySharing.FALSE);
			}
		}
		return null;
	}

}
