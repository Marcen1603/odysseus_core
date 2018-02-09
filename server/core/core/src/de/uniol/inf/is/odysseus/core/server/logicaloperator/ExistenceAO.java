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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(name = "EXISTENCE", minInputPorts = 2, maxInputPorts = 2, doc = "This operator tests an existence predicate and can be used with the type EXISTS (semi join) and NOT_EXISTS (anti semi join). The predicates can be evaluated against the element from the first input and the second input. Semi join: All elements in the first input for which there are elements in the second input that fulfills the predicate are sent. Semi anti join: All elements in the first input for which there is no element in the second input that fulfills the predicate are sent.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Existence+operator", category = {
		LogicalOperatorCategory.BASE, LogicalOperatorCategory.SET })
public class ExistenceAO extends BinaryLogicalOp implements Cloneable,
		IHasPredicate {

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = super.hashCode();
	// result = prime * result + ((type == null) ? 0 : type.hashCode());
	// return result;
	// }

	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (!super.equals(obj))
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// ExistenceAO other = (ExistenceAO) obj;
	// if (type == null) {
	// if (other.type != null)
	// return false;
	// } else if (!type.equals(other.type))
	// return false;
	// return true;
	// }

	private static final long serialVersionUID = -7471367531511438478L;

	public static enum Type {
		EXISTS, NOT_EXISTS
	}

	private Type type;
	private IPredicate<?> predicate;

	public ExistenceAO() {
	}

	public ExistenceAO(ExistenceAO ao) {
		super(ao);
		this.type = ao.type;
		if (ao.predicate != null) {
			this.predicate = ao.predicate.clone();
		}
	}

	@Override
	public ExistenceAO clone() {
		return new ExistenceAO(this);
	}

	@Parameter(type = PredicateParameter.class)
	public void setPredicate(@SuppressWarnings("rawtypes") IPredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
	}

	@Parameter(type = EnumParameter.class)
	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		return getInputSchema(LEFT);
	}

}
