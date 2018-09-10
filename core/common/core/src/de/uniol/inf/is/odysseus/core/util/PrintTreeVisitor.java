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
package de.uniol.inf.is.odysseus.core.util;

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;

public class PrintTreeVisitor implements INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, Void> {
	
	@Override
	public void ascendAction(
			ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		
	}

	@Override
	public void descendAction(
			ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		
	}

	@Override
	public void nodeAction(ISubscriber<ILogicalOperator, LogicalSubscription> op) {
		System.out.println(op);
	}

	@Override
	public Void getResult() {
		return null;
	}

}
