package de.uniol.inf.is.odysseus.condition.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.enums.TrainingMode;
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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DEVIATIONLEARN", doc = "Learns the mean and the standard deviation of a single value and writes it into the output.", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationLearnAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4859491963759169252L;

	private double manualMean;
	private double manualStandardDeviation;
	private List<SDFAttribute> groupingAttributes;
	private boolean fastGrouping;
	private boolean exactCalculation;
	private long tuplesToLearn;
	private TrainingMode trainingMode;
	private String nameOfValue;

	public DeviationLearnAO() {
		this.trainingMode = TrainingMode.ONLINE;
		this.nameOfValue = "value";
	}

	public DeviationLearnAO(DeviationLearnAO ao) {
		this.manualMean = ao.getManualMean();
		this.manualStandardDeviation = ao.getManualStandardDeviation();
		this.groupingAttributes = ao.getGroupingAttributes();
		this.fastGrouping = ao.isFastGrouping();
		this.exactCalculation = ao.isExactCalculation();
		this.tuplesToLearn = ao.getTuplesToLearn();
		this.trainingMode = ao.getTrainingMode();
		this.nameOfValue = ao.getNameOfValue();
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> groupingAttributes) {
		this.groupingAttributes = groupingAttributes;
	}

	public boolean isFastGrouping() {
		return fastGrouping;
	}

	@Parameter(name = "fastGrouping", type = BooleanParameter.class, optional = true, doc = "Use hash code instead of tuple compare to create group. Potentially unsafe!")
	public void setFastGrouping(boolean fastGrouping) {
		this.fastGrouping = fastGrouping;
	}

	public boolean isExactCalculation() {
		return exactCalculation;
	}

	@Parameter(name = "exactCalculation", type = BooleanParameter.class, optional = true, doc = "If set to true, it uses exact calculation for window mode (recalc values every new tuple). This may be slower. Unexact calculation may be faster, but unaccurate, especially if the mean changes dramatically over time.")
	public void setExactCalculation(boolean exactCalculation) {
		this.exactCalculation = exactCalculation;
	}

	public long getTuplesToLearn() {
		return tuplesToLearn;
	}

	@Parameter(type = LongParameter.class, name = "tuplesToLearn", optional = true)
	public void setTuplesToLearn(long tuplesToLearn) {
		this.tuplesToLearn = tuplesToLearn;
	}

	public TrainingMode getTrainingMode() {
		return trainingMode;
	}

	@Parameter(type = EnumParameter.class, name = "trainingMode", optional = true)
	public void setTrainingMode(TrainingMode trainingMode) {
		this.trainingMode = trainingMode;
	}

	public double getManualMean() {
		return manualMean;
	}

	@Parameter(type = DoubleParameter.class, name = "mean", optional = true)
	public void setManualMean(double manualMean) {
		this.manualMean = manualMean;
	}

	public double getManualStandardDeviation() {
		return manualStandardDeviation;
	}

	@Parameter(type = DoubleParameter.class, name = "standardDeviation", optional = true)
	public void setManualStandardDeviation(double manualStandardDeviation) {
		this.manualStandardDeviation = manualStandardDeviation;
	}

	public String getNameOfValue() {
		return nameOfValue;
	}

	@Parameter(type = StringParameter.class, name = "nameOfParameter", optional = true, doc = "Name of the attribute which should be analysed")
	public void setNameOfValue(String nameOfValue) {
		this.nameOfValue = nameOfValue;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DeviationLearnAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {

		// Transfer the mean and the standard deviation to the next operator
		SDFAttribute groupId = new SDFAttribute(null, "group", SDFDatatype.LONG, null, null, null);
		SDFAttribute meanValue = new SDFAttribute(null, "mean", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute standardDeviation = new SDFAttribute(null, "standardDeviation", SDFDatatype.DOUBLE, null, null,
				null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.add(groupId);
		outputAttributes.add(meanValue);
		outputAttributes.add(standardDeviation);

		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema());
		this.setOutputSchema(outputSchema);

		return getOutputSchema();
	}

}
