package de.uniol.inf.is.odysseus.sensormanagement.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;

@LogicalOperator(name="SensorManagementAction", maxInputPorts=1, minInputPorts=1, doc="Executes sensor management commands based on receiving tuple data", category = {LogicalOperatorCategory.PLAN})
public class SensorManagementActionAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -578154679444642283L;

	private SDFExpression commandExpression;
	private SDFExpression sensorIdExpression;
	
	public SensorManagementActionAO() {
		super();
	}
	
	public SensorManagementActionAO(SensorManagementActionAO copy) {
		super(copy);
		
		commandExpression = copy.commandExpression.clone();
		sensorIdExpression = copy.sensorIdExpression.clone();
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SensorManagementActionAO(this);
	}

	@Parameter(name="CommandExpression", type = SDFExpressionParameter.class, doc="Expression for the sensor management commands, e.g. an attribute or a string")
	public void setCommandExpression2(NamedExpression commandExpression) {
		this.commandExpression = commandExpression.expression;
	}
	
	public SDFExpression getCommandExpression() {
		return commandExpression;
	}

	@Parameter(name="SensorIdExpression", type = SDFExpressionParameter.class, doc="Exprssion to calculate the sensor id to execute the commands on")
	public void setSensorIdExpression2(NamedExpression attribute) {
		sensorIdExpression = attribute.expression;
	}
	
	public SDFExpression getSensorIdExpression() {
		return sensorIdExpression;
	}
	
	@Override
	public boolean isValid() {
		clearErrors();
		
		if( !commandExpression.getType().isString()) {
			addError("CommandExpression must have return type STRING, not " + commandExpression.getType());
		}
		
		if( !sensorIdExpression.getType().isString()) {
			addError("SensorIdExpression must have return type STRING, not " + sensorIdExpression.getType());
		}
		
		return getErrors().isEmpty();
	}
}
