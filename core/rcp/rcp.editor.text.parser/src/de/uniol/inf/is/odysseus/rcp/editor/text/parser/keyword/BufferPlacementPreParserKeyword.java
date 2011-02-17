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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;

public class BufferPlacementPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		if (parameter != null && !parameter.equalsIgnoreCase("NONE")) {
			IBufferPlacementStrategy s = executor
					.getBufferPlacementStrategy(parameter);
			if (s == null) {
				throw new QueryTextParseException(
						"No Buffer Placement Strategy " + parameter + " loaded");
			}
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		List<IQueryBuildSetting<?>> config = executor
				.getQueryBuildConfiguration((String) variables.get("TRANSCFG"));
		Iterator<IQueryBuildSetting<?>> iter = config.iterator();
		IBufferPlacementStrategy s = null;
		if (iter != null) {
			while (iter.hasNext()) {
				IQueryBuildSetting<?> sett = iter.next();
				if (sett instanceof ParameterBufferPlacementStrategy) {
					iter.remove();
					break;
				}
			}
			if (parameter != null && !parameter.equalsIgnoreCase("NONE")) {
				s = executor.getBufferPlacementStrategy(parameter);
				config.add(new ParameterBufferPlacementStrategy(s));
			}
		}
		return s;
	}

}
