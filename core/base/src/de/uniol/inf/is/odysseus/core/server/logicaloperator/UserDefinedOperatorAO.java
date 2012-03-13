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
public class UserDefinedOperatorAO extends AbstractLogicalOperator implements
		OutputSchemaSettable {

	private static final long serialVersionUID = 837012993098327414L;
	private SDFSchema outputSchema = null;
	private String operatorClass = null;
	private String initString = null;
	@SuppressWarnings("rawtypes")
	private IUserDefinedFunction udf = null;

	public UserDefinedOperatorAO() {
		super();
	}

	public UserDefinedOperatorAO(UserDefinedOperatorAO userDefinedOperatorAO) {
		super(userDefinedOperatorAO);
		this.outputSchema = userDefinedOperatorAO.outputSchema;
		this.operatorClass = userDefinedOperatorAO.operatorClass;
		this.initString = userDefinedOperatorAO.initString;
		this.udf = userDefinedOperatorAO.udf;
	}

	@Override
	public SDFSchema getOutputSchema() {
		if (outputSchema != null) {
			return outputSchema;
		} else {
			return getInputSchema(0);
		}
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
		this.outputSchema = new SDFSchema("", outputSchema);
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
		this.outputSchema = outputSchema;
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema, int port) {
		if (port == 0) {
			setOutputSchema(outputSchema);
		} else {
			throw new IllegalArgumentException("no such port: " + port);
		}
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
