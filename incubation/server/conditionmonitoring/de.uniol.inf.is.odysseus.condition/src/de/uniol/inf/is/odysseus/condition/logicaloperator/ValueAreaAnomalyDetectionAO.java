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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "VALUEANOMALYDETECTION", doc = "Todo", category = { LogicalOperatorCategory.PROCESSING })
public class ValueAreaAnomalyDetectionAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -6202091035427561666L;

	private double minValue;
	private double maxValue;

	public ValueAreaAnomalyDetectionAO() {

	}

	public ValueAreaAnomalyDetectionAO(double minValue, double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Parameter(type = DoubleParameter.class, name = "minValue", optional = false)
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	@Parameter(type = DoubleParameter.class, name = "maxValue", optional = false)
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// We always have the json info string
		SDFSchema inSchema = getInputSchema(0);

		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		SDFAttribute info = new SDFAttribute(null, "info", SDFDatatype.STRING, null, null, null);
		outputAttributes.add(info);

		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);

		return outSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ValueAreaAnomalyDetectionAO(minValue, maxValue);
	}

}
