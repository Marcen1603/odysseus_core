package de.uniol.inf.is.odysseus.condition.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "RarePattern", doc = "Searches for rare pattern in a stream of (discrete) states", category = { LogicalOperatorCategory.PROCESSING })
public class RarePatternAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -7990417520941357348L;

	private String nameOfValue;
	
	public RarePatternAO() {
	}
	
	public RarePatternAO(RarePatternAO ao) {
		this.nameOfValue = ao.getNameOfValue();
	}
	
	@Parameter(type = StringParameter.class, name = "nameOfParameter", optional = true, doc = "Name of the attribute which should be analysed")
	public void setNameOfValue(String nameOfValue) {
		this.nameOfValue = nameOfValue;
	}
	
	public String getNameOfValue() {
		return nameOfValue;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new RarePatternAO(this);
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add the anomaly-score to the attributes and keep the old attributes
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute anomalyScore = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute path = new SDFAttribute(null, "path", SDFDatatype.DOUBLE, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(anomalyScore);
		outputAttributes.add(path);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

}
