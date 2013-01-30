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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Marco Grawunder
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "STATEMAP")
public class StateMapAO extends MapAO {

	private static final long serialVersionUID = 1695948732660010522L;


	public StateMapAO() {
		super();
	}

	public StateMapAO(StateMapAO ao) {
		super(ao);
	}

	
	@Override
	public StateMapAO clone() {
		return new StateMapAO(this);
	}

}
