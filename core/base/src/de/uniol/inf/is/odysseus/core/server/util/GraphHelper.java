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
package de.uniol.inf.is.odysseus.core.server.util;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

public class GraphHelper {

	/**
	 * Find all connected Operators from this root
	 * @param root
	 * @return
	 */
	static public ArrayList<IPhysicalOperator> getChildren(IPhysicalOperator root) {
		IGraphNodeVisitor<IPhysicalOperator, ArrayList<IPhysicalOperator>> visitor = new CollectChildOperatorsVisitor<IPhysicalOperator>();
		GenericGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, ?> walker = new GenericGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, LogicalSubscription>();
		walker.prefixWalkPhysical(root, visitor);
		return visitor.getResult();
	}
	
}
