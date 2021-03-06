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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class InputOperatorItem {
	public InputOperatorItem(ILogicalOperator operator, int outputPort) {
		this.operator = operator;
		this.outputPort = outputPort;
	}

	public final ILogicalOperator operator;
	public final int outputPort;
	
	@Override
	public String toString() {
		return operator+" ("+outputPort+")";
	}
}
