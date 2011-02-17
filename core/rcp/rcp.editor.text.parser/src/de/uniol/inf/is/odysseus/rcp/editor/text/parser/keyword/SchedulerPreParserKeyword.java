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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;

public class SchedulerPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		parameter.split("\"*\"");
		List<String> params = splitParams(parameter);
		if (params.size() != 2){
			throw new QueryTextParseException("Illegal Scheduler Definition "+parameter);
		}
		if (!(executor.getRegisteredSchedulers().contains(params.get(0)))){
			throw new QueryTextParseException("Scheduler "+params.get(0)+" not found");			
		}
		if (!(executor.getRegisteredSchedulingStrategies().contains(params.get(1)))){
			throw new QueryTextParseException("Schedulingstrategy "+params.get(1)+" not found");			
		}
		
			
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		parameter.split("\"*\"");
		List<String> params = splitParams(parameter);
		executor.setScheduler(params.get(0), params.get(1));
		return null;
	}

	private List<String> splitParams(String input){
		String[] s = input.split("\"*\"");
		List<String> ret = new ArrayList<String>(s.length);
		for (String str : s) {
			if (!isEmpty(str)){
				ret.add(str);
			}
		}
		return ret;
	}
	
	public static boolean isEmpty(String str) {
		if (str != null && str.trim().length() > 0) {
			return false;
		}
		return true;
	}

}
