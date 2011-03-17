/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



/**
 * @author Marco Grawunder
 *
 */
@LogicalOperator(name="DIFFERENCE", minInputPorts=2, maxInputPorts=2)
public class DifferenceAO extends BinaryLogicalOp{
	
	private static final long serialVersionUID = 4518770628909423647L;

    public DifferenceAO(DifferenceAO differencePO) {
        super(differencePO);
    }

    public DifferenceAO() {
        super();
    }

	public @Override
	DifferenceAO clone() {	
		return new DifferenceAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(LEFT);
	}

  

}
