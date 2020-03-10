package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;

/**
 * TODO
 * 
 * @author Marco Grawunder
 *
 */
@LogicalOperator(minInputPorts = 0, maxInputPorts = 1, name = "Heartbeat2", category = {
		LogicalOperatorCategory.PROCESSING }, doc = "TODO")
public class HeartbeatAO2 extends UnaryLogicalOp {

	private static final long serialVersionUID = -5715561730592890314L;

	private boolean createOnHeartbeat;
	private SDFExpression timeExpression;
	//private IPredicate createWhen;
	
	public HeartbeatAO2() {
	}

	public HeartbeatAO2(HeartbeatAO2 other) {
		super(other);
		this.createOnHeartbeat = other.createOnHeartbeat;
		this.timeExpression = other.timeExpression.clone();
		//this.createWhen = other.createWhen.clone();
	}

	@Parameter(type = BooleanParameter.class, optional = true, doc ="If set to true, for every new incomming heartbeat the will be a new heartbeat with the value of the current time parameter.")
	public void setCreateOnHeartbeat(boolean createOnHeartbeat) {
		this.createOnHeartbeat = createOnHeartbeat;
	}
	
	public boolean isCreateOnHeartbeat() {
		return createOnHeartbeat;
	}

	@Parameter(name = "TIME", type = NamedExpressionParameter.class, optional = false, doc = "Calculate the time value for the heartbeat that is created.")
	public void setTimeExpression2(NamedExpression timeExpression) {
		this.timeExpression = timeExpression.expression;
	}
	
	public SDFExpression getTimeExpression() {
		return timeExpression;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new HeartbeatAO2(this);
	}

	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.NONE;
	}



}
