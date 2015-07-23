/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Logical operator for shuffle fragmentation
 * 
 * @author ChrisToenjesDeye
 *
 */
@LogicalOperator(name = "SHUFFLEFRAGMENT", minInputPorts = 1, maxInputPorts = 1, doc="Can be used to fragment incoming streams",category={LogicalOperatorCategory.PROCESSING})
public class ShuffleFragmentAO extends AbstractStaticFragmentAO {

	private static final long serialVersionUID = 6061095387594392868L;

	
	public ShuffleFragmentAO() {
		super();
	}
	
	public ShuffleFragmentAO(ShuffleFragmentAO shuffleFragmentAO){
		super(shuffleFragmentAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ShuffleFragmentAO(this);
	}

}
