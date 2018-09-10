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
package de.uniol.inf.is.odysseus.script.keyword.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.scheduler.SetSchedulerCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class SchedulerPreParserKeyword extends AbstractPreParserKeyword {

	public static final String SCHEDULER = "SCHEDULER";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		parameter.split("\"*\"");
		List<String> params = splitParams(parameter);
		if (params.size() != 2){
			throw new OdysseusScriptException("Illegal Scheduler Definition "+parameter);
		}
		if (!(executor.getRegisteredSchedulers(caller).contains(params.get(0)))){
			throw new OdysseusScriptException("Scheduler "+params.get(0)+" not found");
		}
		if (!(executor.getRegisteredSchedulingStrategies(caller).contains(params.get(1)))){
			throw new OdysseusScriptException("Schedulingstrategy "+params.get(1)+" not found");
		}
	}

	@Override
	public List<IExecutorCommand>  execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		parameter.split("\"*\"");
		List<String> params = splitParams(parameter);
		List<IExecutorCommand>  ret = new ArrayList<>();
		SetSchedulerCommand cmd = new SetSchedulerCommand(params.get(0), params.get(1), caller);
		ret.add(cmd);
		return ret;
	}

	private static List<String> splitParams(String input){
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
