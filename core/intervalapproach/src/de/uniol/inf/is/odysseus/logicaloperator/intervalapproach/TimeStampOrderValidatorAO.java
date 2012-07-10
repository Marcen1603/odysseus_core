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
package de.uniol.inf.is.odysseus.logicaloperator.intervalapproach;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="TimeStampOrderValidate")
public class TimeStampOrderValidatorAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9204585315520513917L;

	public TimeStampOrderValidatorAO(){}
	
	public TimeStampOrderValidatorAO(
			TimeStampOrderValidatorAO timeStampOrderValidatorAO) {
		super(timeStampOrderValidatorAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimeStampOrderValidatorAO(this);
	}

}
