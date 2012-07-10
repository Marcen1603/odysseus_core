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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, name = "LEFTJOIN")
public class LeftJoinAO extends JoinAO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8336532431695971478L;

	public LeftJoinAO(){
		super();
	}
	
	public LeftJoinAO(IPredicate<?> joinPredicate) {
		super(joinPredicate);
	}

	public LeftJoinAO(LeftJoinAO joinPO) {
		super(joinPO);
	}
	
	@Override
	public LeftJoinAO clone(){
		return new LeftJoinAO(this);
	}

}
