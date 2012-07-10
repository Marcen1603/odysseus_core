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
package de.uniol.inf.is.odysseus.core.server.sla.scope;

import de.uniol.inf.is.odysseus.core.server.sla.Scope;

/**
 * A scope for sla. This scope means that the average value of a metric must not
 * exceed the thrshold defined in the service level. otherwise the service level
 * will be violated.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Average extends Scope {

	@Override
	public boolean thresholdIsMin() {
		return false;
	}

}
