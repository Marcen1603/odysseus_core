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

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "DEVIATIONSEQUENCEANOMALYDETECTION", doc = "Searches for anomalies in a sequence in comparison to the learned sequences. The data port is 0, the port with learn data is 1.", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationSequenceAnalysisAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 2247550004872347984L;

	private static final int DATA_IN_PORT = 0;
	// private static final int LEARN_IN_PORT = 1;

	private double interval;

	// Learn port
	private String tupleCountLearnAttributeName;
	private String meanAttributeName;
	private String standardDeviationAttributeName;

	// Data port
	private String valueAttributeName;

	private List<SDFAttribute> groupingAttributes;
	private boolean fastGrouping;

	public DeviationSequenceAnalysisAO() {
		this.interval = 3.0;
	}

	public DeviationSequenceAnalysisAO(DeviationSequenceAnalysisAO ao) {
		super(ao);
		this.interval = ao.getInterval();

		this.tupleCountLearnAttributeName = ao.getTupleGroupAttributeName();
		this.meanAttributeName = ao.getMeanAttributeName();
		this.standardDeviationAttributeName = ao.getStandardDeviationAttributeName();

		this.valueAttributeName = ao.getValueAttributeName();

		this.fastGrouping = ao.isFastGrouping();
		this.groupingAttributes = ao.getGroupingAttributes();
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

	public String getValueAttributeName() {
		return valueAttributeName;
	}

	@Parameter(type = StringParameter.class, name = "valueDataAttribute", optional = true, doc = "Name of the attribute which should be analysed")
	public void setValueAttributeName(String valueAttributeName) {
		this.valueAttributeName = valueAttributeName;
	}

	public boolean isFastGrouping() {
		return fastGrouping;
	}

	@Parameter(name = "fastGrouping", type = BooleanParameter.class, optional = true, doc = "Use hash code instead of tuple compare to create group. Potentially unsafe!")
	public void setFastGrouping(boolean fastGrouping) {
		this.fastGrouping = fastGrouping;
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true, doc = "If you use a deviationSequenceLearn operator, use 'group' as grouping attribute.")
	public void setGroupingAttributes(List<SDFAttribute> groupingAttributes) {
		this.groupingAttributes = groupingAttributes;
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
			SDFSchema inSchema = getInputSchema(DATA_IN_PORT);
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
			SDFSchema inSchema = getInputSchema(DATA_IN_PORT);
			SDFAttribute totalSum = new SDFAttribute(null, "totalSum", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute meanSum = new SDFAttribute(null, "meanSum", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute totalDifference = new SDFAttribute(null, "absoluteDifference", SDFDatatype.DOUBLE, null, null,
					null);
			SDFAttribute relativeDifference = new SDFAttribute(null, "relativeDifference", SDFDatatype.DOUBLE, null,
					null, null);
			SDFAttribute numberOfTuples = new SDFAttribute(null, "numberOfTuples", SDFDatatype.LONG, null, null, null);
			SDFAttribute averageTupleValue = new SDFAttribute(null, "averageTupleValue", SDFDatatype.INTEGER, null,
					null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(totalSum);
			outputAttributes.add(meanSum);
			outputAttributes.add(totalDifference);
			outputAttributes.add(relativeDifference);
			outputAttributes.add(numberOfTuples);
			outputAttributes.add(averageTupleValue);
			SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
			setOutputSchema(1, outSchema);

			return getOutputSchema(1);
		}
		return null;
	}

}
