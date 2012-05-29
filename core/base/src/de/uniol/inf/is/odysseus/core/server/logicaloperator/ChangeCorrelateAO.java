/** Copyright 2012 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

/**
 * 
 * @author Dennis Geesen
 * Created at: 29.05.2012
 */
@LogicalOperator(maxInputPorts=2, minInputPorts=2, name="CHANGECORRELATE")
public class ChangeCorrelateAO extends BinaryLogicalOp{

	private IPredicate<?> leftHighPredicate;	
	private IPredicate<?> leftLowPredicate;
	private IPredicate<?> rightHighPredicate;
	private IPredicate<?> rightLowPredicate;
	
	private static final long serialVersionUID = 2341933392781838294L;

	public ChangeCorrelateAO(){
		super();
	}
	
	
	public ChangeCorrelateAO(ChangeCorrelateAO changeCorrelateAO) {
		super(changeCorrelateAO);
		this.leftHighPredicate = changeCorrelateAO.leftHighPredicate.clone();
		this.leftLowPredicate = changeCorrelateAO.leftLowPredicate.clone();
		this.rightHighPredicate = changeCorrelateAO.rightHighPredicate.clone();
		this.rightLowPredicate = changeCorrelateAO.rightLowPredicate.clone();
	}



	public IPredicate<?> getLeftHighPredicate() {
		return leftHighPredicate;
	}


	@Override
	public synchronized SDFSchema getOutputSchemaIntern(int pos) {
		// The Sum of all InputSchema
		SDFSchema outputSchema = null;
		for (LogicalSubscription l : getSubscribedToSource()) {
			outputSchema = SDFSchema.union(outputSchema, l.getSchema());
		}
		setOutputSchema(outputSchema);
		return outputSchema;
	}

	@Parameter(type=PredicateParameter.class, name="lefthighpredicate")
	public void setLeftHighPredicate(IPredicate<?> leftHighPredicate) {
		this.leftHighPredicate = leftHighPredicate;
	}


	public IPredicate<?> getLeftLowPredicate() {
		return leftLowPredicate;
	}



	@Parameter(type=PredicateParameter.class, name="leftlowpredicate")
	public void setLeftLowPredicate(IPredicate<?> leftLowPredicate) {
		this.leftLowPredicate = leftLowPredicate;
	}




	public IPredicate<?> getRightHighPredicate() {
		return rightHighPredicate;
	}



	@Parameter(type=PredicateParameter.class, name="righthighpredicate")
	public void setRightHighPredicate(IPredicate<?> rightHighPredicate) {
		this.rightHighPredicate = rightHighPredicate;
	}




	public IPredicate<?> getRightLowPredicate() {
		return rightLowPredicate;
	}



	@Parameter(type=PredicateParameter.class, name="rightlowpredicate")
	public void setRightLowPredicate(IPredicate<?> rightLowPredicate) {
		this.rightLowPredicate = rightLowPredicate;
	}




	@Override
	public AbstractLogicalOperator clone() {	
		return new ChangeCorrelateAO(this);
	}

}
