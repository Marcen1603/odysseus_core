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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "DEVIATIONSEQUENCEANOMALYDETECTION", doc = "Searches for anomalies in a sequence in comparison to the learned sequences. The data port is 0, the port with learn data is 1.", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationSequenceAnalysisAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 2247550004872347984L;

	private static final int DATA_PORT = 0;

	private double interval;

	// Learn port
	private String tupleCountLearnAttributeName;
	private String meanAttributeName;
	private String standardDeviationAttributeName;

	// Data port
	private String tupleCountDataAttribute;
	private String valueAttributeName;

	public DeviationSequenceAnalysisAO() {
		this.interval = 3.0;
	}

	public DeviationSequenceAnalysisAO(DeviationSequenceAnalysisAO ao) {
		this.interval = ao.getInterval();

		this.tupleCountLearnAttributeName = ao.getTupleGroupAttributeName();
		this.meanAttributeName = ao.getMeanAttributeName();
		this.standardDeviationAttributeName = ao.getStandardDeviationAttributeName();

		this.tupleCountDataAttribute = ao.getTupleCountAttribute();
		this.valueAttributeName = ao.getValueAttributeName();
	}

	public double getInterval() {
		return interval;
	}

	@Parameter(type = DoubleParameter.class, name = "interval", optional = true, doc = "Defines, how many standard deviations are allowed for a tuple to be different from the mean. 3.0 is the default value. Choose a smaller value to get more anomalies")
	public void setInterval(double interval) {
		this.interval = interval;
	}

	public String getTupleGroupAttributeName() {
		return tupleCountLearnAttributeName;
	}

	@Parameter(type = StringParameter.class, name = "tupleCountLearnAttribute", optional = true, doc = "The attribute name on the learn port that gives the group count (the counter that gives each tuple in the sequence a number)")
	public void setTupleGroupAttributeName(String tupleGroupAttributeName) {
		this.tupleCountLearnAttributeName = tupleGroupAttributeName;
	}

	public String getMeanAttributeName() {
		return meanAttributeName;
	}

	@Parameter(type = StringParameter.class, name = "meanLearnAttribute", optional = true, doc = "The attribute name on the learn port that has the mean")
	public void setMeanAttributeName(String meanAttributeName) {
		this.meanAttributeName = meanAttributeName;
	}

	public String getStandardDeviationAttributeName() {
		return standardDeviationAttributeName;
	}

	@Parameter(type = StringParameter.class, name = "standardDeviationLearnAttribute", optional = true, doc = "The attribute name on the learn port that has the standard deviation")
	public void setStandardDeviationAttributeName(String standardDeviationAttributeName) {
		this.standardDeviationAttributeName = standardDeviationAttributeName;
	}

	public String getTupleCountAttribute() {
		return tupleCountDataAttribute;
	}

	@Parameter(type = StringParameter.class, name = "tupleCountDataAttribute", optional = true, doc = "The attribute name on the data port that gives the group count (the counter that gives each tuple in the sequence a number)")
	public void setTupleCountAttribute(String tupleCountAttribute) {
		this.tupleCountDataAttribute = tupleCountAttribute;
	}

	public String getValueAttributeName() {
		return valueAttributeName;
	}

	@Parameter(type = StringParameter.class, name = "valueDataAttribute", optional = true, doc = "Name of the attribute which should be analysed")
	public void setValueAttributeName(String valueAttributeName) {
		this.valueAttributeName = valueAttributeName;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DeviationSequenceAnalysisAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int port) {
		if (port == 0) {
			// The port for each tuple

			// add the anomaly-score to the attributes and keep the old
			// attributes
			SDFSchema inSchema = getInputSchema(DATA_PORT);
			SDFAttribute anomalyScore = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute meanValue = new SDFAttribute(null, "mean", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute standardDeviation = new SDFAttribute(null, "standardDeviation", SDFDatatype.DOUBLE, null,
					null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.addAll(inSchema.getAttributes());
			outputAttributes.add(anomalyScore);
			outputAttributes.add(meanValue);
			outputAttributes.add(standardDeviation);
			SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
			setOutputSchema(0, outSchema);

			return getOutputSchema(0);
		} else if (port == 1) {
			// The port for information about a whole sequence
			// add the anomaly-score to the attributes and keep the old
			// attributes
			SDFSchema inSchema = getInputSchema(DATA_PORT);
			SDFAttribute totalDifference = new SDFAttribute(null, "totalDifference", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute totalSum = new SDFAttribute(null, "totalSum", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute relativeDifference = new SDFAttribute(null, "relativeDifference", SDFDatatype.DOUBLE, null, null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(totalDifference);
			outputAttributes.add(totalSum);
			outputAttributes.add(relativeDifference);
			SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
			setOutputSchema(1, outSchema);

			return getOutputSchema(1);
		}
		return null;
	}

}
