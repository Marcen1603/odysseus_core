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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(name="EXISTENCE", minInputPorts=2, maxInputPorts=2)
public class ExistenceAO extends BinaryLogicalOp implements Cloneable {

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
//		ExistenceAO other = (ExistenceAO) obj;
//		if (type == null) {
//			if (other.type != null)
//				return false;
//		} else if (!type.equals(other.type))
//			return false;
//		return true;
//	}

	private static final long serialVersionUID = -7471367531511438478L;

	public static enum Type {
		EXISTS, NOT_EXISTS
	}

	private Type type;

	public ExistenceAO() {
	}

	public ExistenceAO(ExistenceAO ao) {
		super(ao);
		this.type = ao.type;
	}

	@Override
	public ExistenceAO clone() {
		return new ExistenceAO(this);
	}

	@Override
	@Parameter(type=PredicateParameter.class)
	public void setPredicate(@SuppressWarnings("rawtypes") IPredicate predicate) {
		super.setPredicate(predicate);
	}

	@Parameter(type=EnumParameter.class)
	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getInputSchema(LEFT);
	}
	
}
