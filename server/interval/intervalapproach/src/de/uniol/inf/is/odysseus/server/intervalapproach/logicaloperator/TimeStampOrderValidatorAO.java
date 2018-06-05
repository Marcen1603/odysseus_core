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
package de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="TimeStampOrderValidate", doc="Assure that all elements are ordered by start timestamp and eliminate out of order elements.", category = {LogicalOperatorCategory.PROCESSING})
public class TimeStampOrderValidatorAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9204585315520513917L;
	private int debugMode;

	public TimeStampOrderValidatorAO(){}
	
	public TimeStampOrderValidatorAO(
			TimeStampOrderValidatorAO timeStampOrderValidatorAO) {
		super(timeStampOrderValidatorAO);
		this.debugMode = timeStampOrderValidatorAO.debugMode;
	}
	
	@Parameter(optional = true, type = IntegerParameter.class, doc ="Set output mode: 0 = minimal, 1 = medium, 2 = maximum")
	public void setDebugMode(int debugMode) {
		this.debugMode = debugMode;
	}
	
	public int getDebugMode() {
		return debugMode;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimeStampOrderValidatorAO(this);
	}	
	
	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.NONE;
	}
	
}
