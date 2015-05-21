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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "DEVIATIONANOMALYDETECTION", doc = "Searches for anomalies on the base of the standard-deviation. First port: data, Second port: deviation information.", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationAnomalyDetectionAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -7283600658977972140L;

	private double interval;
	private String nameOfValue;
	private List<SDFAttribute> groupingAttributes;
	private boolean fastGrouping;

	private boolean windowChecking;
	private boolean onlyOnChange;

	public DeviationAnomalyDetectionAO() {
		interval = 3.0;
		nameOfValue = "value";
	}

	public DeviationAnomalyDetectionAO(DeviationAnomalyDetectionAO ao) {
		this.setInterval(ao.getInterval());
		this.setNameOfValue(ao.getNameOfValue());
		if (ao.groupingAttributes != null) {
			this.groupingAttributes = new ArrayList<SDFAttribute>(ao.groupingAttributes);
		}
		this.fastGrouping = ao.isFastGrouping();
		this.windowChecking = ao.isWindowChecking();
		this.onlyOnChange = ao.isOnlyOnChange();
	}

	@Parameter(type = DoubleParameter.class, name = "interval", optional = true, doc = "Defines, how many standard deviations are allowed for a tuple to be different from the mean. 3.0 is the default value. Choose a smaller value to get more anomalies.")
	public void setInterval(double interval) {
		this.interval = interval;
	}

	@Parameter(type = StringParameter.class, name = "nameOfParameter", optional = true, doc = "Name of the attribute which should be analysed")
	public void setNameOfValue(String nameOfValue) {
		this.nameOfValue = nameOfValue;
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}

	@Parameter(name = "fastGrouping", type = BooleanParameter.class, optional = true, doc = "Use hash code instead of tuple compare to create group. Potentially unsafe!")
	public void setFastGrouping(boolean fastGrouping) {
		this.fastGrouping = fastGrouping;
	}

	@Parameter(name = "windowChecking", type = BooleanParameter.class, optional = true, doc = "If true, it's checked if the last (tumbling) window had an anomaly. Only sends anomaly-tuple once per window.")
	public void setWindowChecking(boolean windowChecking) {
		this.windowChecking = windowChecking;
	}

	@Parameter(name = "onlyOnChange", type = BooleanParameter.class, optional = true, doc = "If you use windowChecking and set this option true, then an anomaly tuple is only send, if the last window had no anomaly.")
	public void setOnlyOnChange(boolean onlyOnChange) {
		this.onlyOnChange = onlyOnChange;
	}

	public double getInterval() {
		return this.interval;
	}

	public String getNameOfValue() {
		return nameOfValue;
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	public boolean isFastGrouping() {
		return fastGrouping;
	}

	public boolean isWindowChecking() {
		return windowChecking;
	}

	public boolean isOnlyOnChange() {
		return onlyOnChange;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add the anomaly-score to the attributes and keep the old attributes
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute anomalyScore = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute meanValue = new SDFAttribute(null, "mean", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute standardDeviation = new SDFAttribute(null, "standardDeviation", SDFDatatype.DOUBLE, null, null,
				null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(anomalyScore);
		outputAttributes.add(meanValue);
		outputAttributes.add(standardDeviation);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DeviationAnomalyDetectionAO(this);
	}

}
