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
package de.uniol.inf.is.odysseus.rewrite.engine;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlow;
import de.uniol.inf.is.odysseus.ruleengine.system.AbstractWorkingEnvironment;
import de.uniol.inf.is.odysseus.ruleengine.system.WorkingMemory;

public class RewriteEnvironment extends
		AbstractWorkingEnvironment<RewriteConfiguration> {

	private RewriteWorkingMemory workingMemory;

	public RewriteEnvironment(RewriteConfiguration config, IRuleFlow ruleflow,
			ISession caller, IDataDictionary dd) {
		super(config, ruleflow);
		this.workingMemory = new RewriteWorkingMemory(this, caller, dd);
	}

	@Override
	public WorkingMemory getWorkingMemory() {
		return workingMemory;
	}

}
