package de.uniol.inf.is.odysseus.condition.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "CURVECOUNTER", doc = "Counts the tuples in every curve. Port 0: Normal values. Port 1: Start / Stop messages.", category = { LogicalOperatorCategory.PROCESSING })
public class CurveCounterAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 7194867265040245699L;
	
	private String stateAttributeName;
	private String startMessage;
	
	public CurveCounterAO() {
		stateAttributeName = "state";
		startMessage = "start";
	}

	public CurveCounterAO(CurveCounterAO ao) {
		this.stateAttributeName = ao.getStateAttributeName();
		this.startMessage = ao.getStartMessage();
	}

	public String getStateAttributeName() {
		return stateAttributeName;
	}

	@Parameter(type = StringParameter.class, name = "stateAttribute", optional = true, doc = "The atrribute on port 1 that has the state.")
	public void setStateAttributeName(String stateAttributeName) {
		this.stateAttributeName = stateAttributeName;
	}

	public String getStartMessage() {
		return startMessage;
	}

	@Parameter(type = StringParameter.class, name = "startMessage", optional = true, doc = "The message on port 1 that is in the state attribute when the curve starts.")
	public void setStartMessage(String startMessage) {
		this.startMessage = startMessage;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CurveCounterAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add the counter to the schema we get from the input in port 0
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute counter = new SDFAttribute(null, "counter", SDFDatatype.INTEGER, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(counter);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

}
