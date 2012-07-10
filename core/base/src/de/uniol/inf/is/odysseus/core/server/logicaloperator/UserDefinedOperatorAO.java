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

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;

@LogicalOperator(maxInputPorts = Integer.MAX_VALUE, minInputPorts = 1, name = "UDO")
public class UserDefinedOperatorAO extends AbstractLogicalOperator{

	private static final long serialVersionUID = 837012993098327414L;
	private String operatorClass = null;
	private String initString = null;
	@SuppressWarnings("rawtypes")
	private IUserDefinedFunction udf = null;

	public UserDefinedOperatorAO() {
		super();
	}

	public UserDefinedOperatorAO(UserDefinedOperatorAO userDefinedOperatorAO) {
		super(userDefinedOperatorAO);
		this.operatorClass = userDefinedOperatorAO.operatorClass;
		this.initString = userDefinedOperatorAO.initString;
		this.udf = userDefinedOperatorAO.udf;
	}

	@Parameter(type = StringParameter.class, name = "class", optional = false)
	public void setOperatorClass(String operatorClass) {
		this.operatorClass = operatorClass;
		try {
			udf = OperatorBuilderFactory.getUDf(operatorClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getOperatorClass() {
		return operatorClass;
	}

	@Parameter(type = StringParameter.class, name = "init", optional = true)
	public void setInitString(String initString) {
		this.initString = initString;
	}

	public String getInitString() {
		return initString;
	}

	// Must be another name than setOutputSchema, else this method is not found!
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = true)
	public void setOutputSchemaWithList(List<SDFAttribute> outputSchema) {
		setOutputSchema(new SDFSchema("", outputSchema));
	}


	@Override
	public UserDefinedOperatorAO clone() {
		return new UserDefinedOperatorAO(this);
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public IUserDefinedFunction getUdf() {
		return udf;
	}

}
