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
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.rest2.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 *
 * @author Dennis Geesen Created at: 09.08.2011
 */
public class ExecutorServiceBinding {
	
	Logger LOG = LoggerFactory.getLogger(ExecutorServiceBinding.class);

	private static IServerExecutor executor = null;
	private static ISession superUser;

	public static IServerExecutor getExecutor() {
		return executor;
	}

	public void bindExecutor(IExecutor ex) {
		LOG.debug("Executor bound "+ex);
		if (ex instanceof IServerExecutor) {
			executor = (IServerExecutor) ex;
			superUser = SessionManagement.instance.loginSuperUser(null);
			executor.startExecution(superUser);
		} else {
			throw new IllegalArgumentException("Only serverbased Executor can be bound!");
		}

	}

	public void unbindExecutor(IExecutor ex) {
		LOG.debug("Executor unbound "+ex);
		executor.stopExecution(superUser);
		executor = null;
	}
}
