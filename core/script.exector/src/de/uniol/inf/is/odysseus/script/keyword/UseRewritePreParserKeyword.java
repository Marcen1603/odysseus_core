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
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParseException;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class UseRewritePreParserKeyword extends AbstractPreParserExecutorKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter, User caller)
			throws OdysseusScriptParseException {
		
		if (getExecutor() == null)
			throw new OdysseusScriptParseException("No executor found");
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, User caller)
			throws OdysseusScriptParseException {
		IExecutor executor = getExecutor();
		if (executor == null)
			throw new OdysseusScriptParseException("No executor found");
		IQueryBuildConfiguration config = executor.getQueryBuildConfiguration((String)
						variables.get("TRANSCFG"));
		Iterator<IQueryBuildSetting<?>> iter = config.getConfiguration().iterator();
		if (iter != null){
			while (iter.hasNext()) {
				IQueryBuildSetting<?> sett = iter.next();
				if (sett instanceof ParameterDoRewrite) {
					iter.remove();
					break;
				}
			}
			if ("TRUE".equals(parameter.toUpperCase())){
				config.getConfiguration().add(ParameterDoRewrite.TRUE);
			}else{
				config.getConfiguration().add(ParameterDoRewrite.FALSE);
			}
		}
		return null;
	}

}
