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
package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;

public class PrioIdJoinAO extends AbstractLogicalOperator implements
		ILogicalOperator {
	private static final long serialVersionUID = 7562422981222202288L;

	private int leftPos = -1;
	private int rightPos = -1;

	public PrioIdJoinAO() {
	}

	public PrioIdJoinAO(PrioIdJoinAO prioIdJoinAO) {
		this.leftPos = prioIdJoinAO.leftPos;
		this.rightPos = prioIdJoinAO.rightPos;
	}

	public void setLeftPos(int leftPos) {
		this.leftPos = leftPos;
	}

	public void setRightPos(int rightPos) {
		this.rightPos = rightPos;
	}

	public int getLeftPos() {
		return leftPos;
	}

	public int getRightPos() {
		return rightPos;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new PrioIdJoinAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema outputSchema = null;
		for (LogicalSubscription l : getSubscribedToSource()) {
			outputSchema = SDFSchema.union(outputSchema, l.getSchema());
			// TODO: Warum wurde das schema doppelt hinzugefügt?? (MG)
			//			outputSchema.addAttributes(l.getSchema());
		}
		return outputSchema;
	}

}
