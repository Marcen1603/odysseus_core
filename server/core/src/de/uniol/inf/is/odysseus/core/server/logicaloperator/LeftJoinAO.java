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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(minInputPorts = 2, maxInputPorts = 2, doc = "Operator to combine two datastreams based on the predicate. All attributes from the first (left) source remain. If an element from the first source has no join partner, it will also be part of the output stream and the output schema contains null values for the missing fields.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/LeftJoin+operator", name = "LEFTJOIN", category = {
		LogicalOperatorCategory.BASE })
public class LeftJoinAO extends JoinAO {

	private static final long serialVersionUID = 8336532431695971478L;

	public LeftJoinAO() {
		super();
	}

	public LeftJoinAO(LeftJoinAO joinPO) {
		super(joinPO);
	}

	@Override
	public LeftJoinAO clone() {
		return new LeftJoinAO(this);
	}

}