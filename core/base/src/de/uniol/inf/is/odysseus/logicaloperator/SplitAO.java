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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

@LogicalOperator(name="SPLIT", minInputPorts=1, maxInputPorts=1)
public class SplitAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8015847502104587689L;
	List<IPredicate<?>> predicates = new ArrayList<IPredicate<?>>();
	
	public SplitAO(){
		super();
	}
	
	public SplitAO(SplitAO splitAO){
		super(splitAO);
		predicates = new ArrayList<IPredicate<?>>(splitAO.predicates);
	}

	public List<IPredicate<?>> getPredicates() {
		return Collections.unmodifiableList(predicates);
	}

	@Parameter(type=PredicateParameter.class, isList=true)
	public void setPredicates(List<IPredicate<?>> predicates) {
		this.predicates = new ArrayList<IPredicate<?>>(predicates);
	}
	
	public void addPredicate(IPredicate<?> predicate) {
		this.predicates.add(predicate);
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SplitAO(this);
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result
//				+ ((predicates == null) ? 0 : predicates.hashCode());
//		return result;
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		SplitAO other = (SplitAO) obj;
//		if (predicates == null) {
//			if (other.predicates != null)
//				return false;
//		} else if (!predicates.equals(other.predicates))
//			return false;
//		return true;
//	}
	
}
