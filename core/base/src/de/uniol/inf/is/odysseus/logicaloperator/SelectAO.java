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
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="SELECT")
public class SelectAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 3215936185841514846L;

	public SelectAO() {
		super();
	}

	public SelectAO(SelectAO po) {
		super(po);
	}

	public SelectAO(IPredicate<?> predicate) {
		setPredicate(predicate);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Parameter(type=PredicateParameter.class)
	public void setPredicate(IPredicate predicate) {
			super.setPredicate(predicate);
	}
	
	@Override
	public SelectAO clone() {
		return new SelectAO(this);
	}
	
	@Override
	public SDFSchema getOutputSchema() {
		return getInputSchema();
	}

}
