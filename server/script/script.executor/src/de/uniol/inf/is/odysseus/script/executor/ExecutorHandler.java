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
package de.uniol.inf.is.odysseus.script.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class ExecutorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExecutorHandler.class);
	private static IServerExecutor executor;

	public void bindExecutor(IExecutor e) {
		if( e instanceof IServerExecutor ) {
			executor =  (IServerExecutor)e;
		} else {
			LOG.error("Bound executor {} is not a ServerExecutor!", e);
		}
	}

	public void unbindExecutor(IExecutor e) {
		if( executor == e ) {
			executor = null;
			LOG.debug("Unbound executor {}", e);
		}
	}

	public static IServerExecutor getServerExecutor() {
		return executor;
	}
	
}
