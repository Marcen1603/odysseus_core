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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Marco Grawunder
 * 
 */
@LogicalOperator(minInputPorts = 2, maxInputPorts = Integer.MAX_VALUE, name = "UNION")
public class UnionAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 2828756253165671692L;

	/**
	 * @param unionPO
	 */
	public UnionAO(UnionAO unionPO) {
		super(unionPO);
	}

	public UnionAO() {
		super();
	}

	public @Override
	UnionAO clone() {
		return new UnionAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		return getInputSchema(LEFT);
	}

//	@Override
//	public boolean isValid() {
//		SDFSchema firstSchema = this.getInputSchema(0);
//		for (int i = 1; i < getNumberOfInputs(); ++i) {
//			if (!firstSchema.compatibleTo(getInputSchema(i))) {
//				addError(new IllegalArgumentException(
//						"incompatible schemas for union"));
//				return false;
//			}
//		}
//		return true;
//	}

}
