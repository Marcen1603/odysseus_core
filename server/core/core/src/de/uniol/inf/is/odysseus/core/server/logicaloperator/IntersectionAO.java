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
/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;



/**
 * @author Marco Grawunder, Christian Kuka
 *
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "INTERSECTION", doc = "This operator calculates the intersection between two input sets.", url = "http://odysseus.offis.uni-oldenburg.de:8090/display/ODYSSEUS/Intersection+operator", category = { LogicalOperatorCategory.BASE })
public class IntersectionAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -3649143474548582941L;

    public IntersectionAO(IntersectionAO operator) {
        super(operator);
    }

    public IntersectionAO(){
    	super();
    }
    
	@Override
	public IntersectionAO clone() {
		return new IntersectionAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		return getInputSchema(LEFT);
	}
	
}
