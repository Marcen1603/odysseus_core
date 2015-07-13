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
package de.uniol.inf.is.odysseus.script.parser;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.script.parser.activator.Activator;

public abstract class AbstractPreParserExecutorKeyword extends AbstractPreParserKeyword {

	private static final long MAX_WAIT_TIME_MILLIS = 60 * 1000;

	protected IServerExecutor getServerExecutor() throws OdysseusScriptException {
		IServerExecutor executor = (IServerExecutor) Activator.getExecutor();
		long startTime = System.currentTimeMillis();

		while (executor == null) {
			tryWait();
			executor = (IServerExecutor) Activator.getExecutor();

			if (executor == null && System.currentTimeMillis() - startTime > MAX_WAIT_TIME_MILLIS) {
				throw new OdysseusScriptException("No executor bound after " + MAX_WAIT_TIME_MILLIS + " milliseconds");
			}
		}

		return executor;
	}

	private static void tryWait() {
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
		}
	}

}
