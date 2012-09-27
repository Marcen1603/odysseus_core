/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.script.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class StartSchedulerPreParserKeyword extends AbstractPreParserExecutorKeyword {

	public static final String KEYWORD = "STARTSCHEDULER";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		IServerExecutor serverExecutor = getServerExecutor();
		if ( serverExecutor == null ) {
			throw new OdysseusScriptException("No executor found");
		}
		
		ISchedulerManager manager = serverExecutor.getSchedulerManager();
		if( manager == null ) {
			throw new OdysseusScriptException("No schedulermanager found");
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		try {
			ISchedulerManager manager = getServerExecutor().getSchedulerManager();
			manager.startScheduling();
			return true;
		} catch (OpenFailedException | NoSchedulerLoadedException ex) {
			throw new OdysseusScriptException("Could not start scheduler", ex);
		} 
	}

}
