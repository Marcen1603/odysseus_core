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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "VALUEANOMALYDETECTION", doc = "Searches for anomalies on the base of a predefined value-area.", category = { LogicalOperatorCategory.PROCESSING })
public class ValueAreaAnomalyDetectionAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -6202091035427561666L;

	private double minValue;
	private double maxValue;
	private boolean sendAllAnomalies;
	private String nameOfValue;

	public ValueAreaAnomalyDetectionAO() {
		sendAllAnomalies = true;
		nameOfValue = "value";
	}

	public ValueAreaAnomalyDetectionAO(ValueAreaAnomalyDetectionAO ao) {
		this.minValue = ao.getMinValue();
		this.maxValue = ao.getMaxValue();
		this.sendAllAnomalies = ao.sendAllAnomalies();
		this.nameOfValue = ao.getNameOfValue();
	}

	@Parameter(type = DoubleParameter.class, name = "minValue", optional = false)
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	@Parameter(type = DoubleParameter.class, name = "maxValue", optional = false)
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	@Parameter(type = BooleanParameter.class, name = "sendAllAnomalies", optional = true, doc = "If true, all tuples which have the wrong value will be send further. If false, only tuples which show a changing situation will be send further.")
	public void setSendAllAnomalies(boolean sendAllAnomalies) {
		this.sendAllAnomalies = sendAllAnomalies;
	}

	@Parameter(type = StringParameter.class, name = "nameOfParameter", optional = true, doc = "Name of the attribute which should be analysed")
	public void setNameOfValue(String nameOfValue) {
		this.nameOfValue = nameOfValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public boolean sendAllAnomalies() {
		return sendAllAnomalies;
	}

	public String getNameOfValue() {
		return nameOfValue;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add the anomaly-score to the attributes and keep the old attributes
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute map = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(map);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ValueAreaAnomalyDetectionAO(this);
	}

}
