package de.uniol.inf.is.odysseus.anomalydetection.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.anomalydetection.enums.TrainingMode;
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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 1, name = "DEVIATIONLEARN", doc = "Learns the mean and the standard deviation of a single value and writes it into the output.", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationLearnAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 4859491963759169252L;

	// Ports
	private static final int DATA_PORT = 0;
	private static final int OUT_INPUT_WITH_GROUP = 1;
	private static final int BACKUP_PORT_OUT = 2;
	private static final int BACKUP_PORT_IN = 1;

	private static final int DATA_INPUT_PORT = 0;
	
	private double manualMean;
	private double manualStandardDeviation;
	private List<SDFAttribute> groupingAttributes;
	private boolean fastGrouping;
	private boolean exactCalculation;
	private long tuplesToLearn;
	private TrainingMode trainingMode;
	private String nameOfValue;
	private String uniqueBackupId;

	public DeviationLearnAO() {
		this.trainingMode = TrainingMode.ONLINE;
		this.nameOfValue = "value";
		this.exactCalculation = true;
		this.uniqueBackupId = "deviationLearn_" + this.nameOfValue + "_" + this.trainingMode;
	}

	public DeviationLearnAO(DeviationLearnAO ao) {
		super(ao);
		this.manualMean = ao.getManualMean();
		this.manualStandardDeviation = ao.getManualStandardDeviation();
		this.groupingAttributes = ao.getGroupingAttributes();
		this.fastGrouping = ao.isFastGrouping();
		this.exactCalculation = ao.isExactCalculation();
		this.tuplesToLearn = ao.getTuplesToLearn();
		this.trainingMode = ao.getTrainingMode();
		this.nameOfValue = ao.getNameOfValue();
		this.uniqueBackupId = ao.getUniqueBackupId();
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true, doc = "To group the tuples and learn a unique deviation for each group")
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

	@Parameter(type = LongParameter.class, name = "tuplesToLearn", optional = true, doc = "The number of tuples that will be used to learn the operator if 'tupleBased' is the choosen trainingMode.")
	public void setTuplesToLearn(long tuplesToLearn) {
		this.tuplesToLearn = tuplesToLearn;
	}

	public TrainingMode getTrainingMode() {
		return trainingMode;
	}

	@Parameter(type = EnumParameter.class, name = "trainingMode", optional = true, doc = "The training mode for this operator")
	public void setTrainingMode(TrainingMode trainingMode) {
		this.trainingMode = trainingMode;
	}

	public double getManualMean() {
		return manualMean;
	}

	@Parameter(type = DoubleParameter.class, name = "mean", optional = true, doc = "If you want to set the mean manually (with trainingMode = manual) you can do this here.")
	public void setManualMean(double manualMean) {
		this.manualMean = manualMean;
	}

	public double getManualStandardDeviation() {
		return manualStandardDeviation;
	}

	@Parameter(type = DoubleParameter.class, name = "standardDeviation", optional = true, doc = "If you want to set the standard deviation manually (with trainingMode = manual) you can do this here.")
	public void setManualStandardDeviation(double manualStandardDeviation) {
		this.manualStandardDeviation = manualStandardDeviation;
	}

	public String getNameOfValue() {
		return nameOfValue;
	}

	@Parameter(type = StringParameter.class, name = "attribute", optional = false, doc = "Name of the attribute which should be analysed")
	public void setNameOfValue(String nameOfValue) {
		this.nameOfValue = nameOfValue;
	}

	public String getUniqueBackupId() {
		return uniqueBackupId;
	}

	@Parameter(type = StringParameter.class, name = "uniqueBackupId", optional = true, doc = "A unique ID for this operator to save and read backup data.")
	public void setUniqueBackupId(String uniqueBackupId) {
		this.uniqueBackupId = uniqueBackupId;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DeviationLearnAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int port) {

		if (port == DATA_PORT) {
			// Transfer the mean and the standard deviation to the next operator
			SDFAttribute groupId = new SDFAttribute(null, "group", SDFDatatype.LONG, null, null, null);
			SDFAttribute meanValue = new SDFAttribute(null, "mean", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute standardDeviation = new SDFAttribute(null, "standardDeviation", SDFDatatype.DOUBLE, null,
					null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(groupId);
			outputAttributes.add(meanValue);
			outputAttributes.add(standardDeviation);

			SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(DATA_PORT));
			this.setOutputSchema(port, outputSchema);
			return getOutputSchema(DATA_PORT);
		} else if (port == OUT_INPUT_WITH_GROUP) {
			SDFSchema inSchema = getInputSchema(DATA_INPUT_PORT);
			SDFAttribute groupId = new SDFAttribute(null, "group", SDFDatatype.LONG, null, null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(groupId);
			outputAttributes.addAll(inSchema.getAttributes());
			SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
			this.setOutputSchema(OUT_INPUT_WITH_GROUP, outputSchema);
			return getOutputSchema(OUT_INPUT_WITH_GROUP);
		} else if (port == BACKUP_PORT_OUT) {
			// Backup-Information
			SDFAttribute groupId = new SDFAttribute(null, "group", SDFDatatype.LONG, null, null, null);
			SDFAttribute uuid = new SDFAttribute(null, "backupId", SDFDatatype.STRING, null, null, null);
			
			SDFAttribute meanValue = new SDFAttribute(null, "mean", SDFDatatype.LONG, null, null, null);
			SDFAttribute stdDeviationValue = new SDFAttribute(null, "standardDeviation", SDFDatatype.LONG, null, null, null);
			
			SDFAttribute nValue = new SDFAttribute(null, "n", SDFDatatype.LONG, null, null, null);
			SDFAttribute m2Value = new SDFAttribute(null, "m2", SDFDatatype.DOUBLE, null, null, null);
			
			SDFAttribute kValue = new SDFAttribute(null, "k", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute sumWindowValue = new SDFAttribute(null, "sumWindow", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute sumWindowSqrValue = new SDFAttribute(null, "sumWindowSqr", SDFDatatype.DOUBLE, null, null, null);
			
			SDFAttribute sum1Value = new SDFAttribute(null, "sum1", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute sum2Value = new SDFAttribute(null, "sum2", SDFDatatype.DOUBLE, null, null, null);
			
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(groupId);
			outputAttributes.add(uuid);
			
			outputAttributes.add(meanValue);
			outputAttributes.add(stdDeviationValue);
			
			outputAttributes.add(nValue);
			outputAttributes.add(m2Value);

			outputAttributes.add(kValue);
			outputAttributes.add(sumWindowValue);
			outputAttributes.add(sumWindowSqrValue);
			
			outputAttributes.add(sum1Value);
			outputAttributes.add(sum2Value);
			
			SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(BACKUP_PORT_IN));
			this.setOutputSchema(port, outputSchema);
			return getOutputSchema(BACKUP_PORT_OUT);
		}

		return getOutputSchema();
	}

}
