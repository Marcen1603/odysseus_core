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

import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Operator ensures the order of the outgoing tuples by timestamp. Collects all
 * incoming tuples and transfers them in the correct order when a Punctuation is
 * processed.
 *
 * @author Merlin Wasmann
 *
 */
@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "REORDER", category={LogicalOperatorCategory.PROCESSING, LogicalOperatorCategory.ORDER}, doc = "Operator which ensures the order of tuples based on punctuations. Requires heartbeats.")
public class ReOrderAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1242934768166736002L;

	public ReOrderAO() {
		super();
	}

	public ReOrderAO(ReOrderAO ao) {
		super(ao);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (getInputSchema() != null) {
			return SDFSchemaFactory.createNewWithOutOfOrder(false, getInputSchema());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new ReOrderAO(this);
	}
	
	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.SELFHANDLED;
	}

}
