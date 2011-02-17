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
package de.uniol.inf.is.odysseus.parser.pql.test;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MuhOperator extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7770543328602025880L;
	
	SDFAttributeList outputSchema = null;
	
	public MuhOperator() {
		
	}
	
	public MuhOperator(MuhOperator muhOperator) {
		this.outputSchema = muhOperator.outputSchema.clone();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		// The Sum of all InputSchema
		if (outputSchema == null || recalcOutputSchemata){
			outputSchema = new SDFAttributeList();
			for (LogicalSubscription l:getSubscribedToSource()){
				outputSchema.addAttributes(l.getSchema());
			}
			recalcOutputSchemata = false;
		}
		return outputSchema;
	}
	
	@Override
	public boolean isAllPhysicalInputSet() {
		boolean set = true;
		for (int i=0;i<getNumberOfInputs() && set;i++){
			set = set && (getPhysSubscriptionTo(i) != null);
		}
		return set;
	}
	
	@Override
	public MuhOperator clone() {
		return new MuhOperator(this);
	}
	
}
