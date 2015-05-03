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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DEVIATIONANOMALYDETECTION", doc = "Searches for anomalies on the base of the standard-deviation.", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationAnomalyDetectionAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -7283600658977972140L;
	
	
	private double interval;
	private TrainingMode trainingMode;
	private double mean;
	private double standardDeviation;
	private long tuplesToLearn;
	private String nameOfValue;
	
	public DeviationAnomalyDetectionAO() {
		interval = 3.0;
		trainingMode = TrainingMode.ONLINE;
		nameOfValue = "value";
	}
	
	public DeviationAnomalyDetectionAO(DeviationAnomalyDetectionAO ao) {
		this.setInterval(ao.getInterval());
		this.setTrainingMode(ao.getTrainingMode());
		this.setMean(ao.getMean());
		this.setStandardDeviation(ao.getStandardDeviation());
		this.setTuplesToLearn(ao.getTuplesToLearn());
		this.setNameOfValue(ao.getNameOfValue());
	}
	
	@Parameter(type = DoubleParameter.class, name = "interval", optional = true, doc = "Defines, how many standard deviations are allowed for a tuple to be different from the mean. 3.0 is the default value. Choose a smaller value to get more anomalies.")
	public void setInterval(double interval) {
		this.interval = interval;
	}
	
	@Parameter(type = EnumParameter.class, name = "trainingMode", optional = true)
	public void setTrainingMode(TrainingMode trainingMode) {
		this.trainingMode = trainingMode;
	}
	
	@Parameter(type = DoubleParameter.class, name = "mean", optional = true)
	public void setMean(double mean) {
		this.mean = mean;
	}
	
	@Parameter(type = DoubleParameter.class, name = "standardDeviation", optional = true)
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	
	@Parameter(type = LongParameter.class, name = "tuplesToLearn", optional = true)
	public void setTuplesToLearn(long tuplesToLearn) {
		this.tuplesToLearn = tuplesToLearn;
	}
	
	@Parameter(type = StringParameter.class, name = "nameOfParameter", optional = true, doc = "Name of the attribute which should be analysed")
	public void setNameOfValue(String nameOfValue) {
		this.nameOfValue = nameOfValue;
	}
	
	public double getInterval() {
		return this.interval;
	}
	
	public TrainingMode getTrainingMode() {
		return this.trainingMode;
	}
	
	public double getMean() {
		return this.mean;
	}
	
	public double getStandardDeviation() {
		return this.standardDeviation;
	}
	
	public long getTuplesToLearn() {
		return this.tuplesToLearn;
	}
	
	public String getNameOfValue() {
		return nameOfValue;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add the anomaly-score to the attributes and keep the old attributes
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute anomalyScore = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute meanValue = new SDFAttribute(null, "mean", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute standardDeviation = new SDFAttribute(null, "standardDeviation", SDFDatatype.DOUBLE, null, null, null);
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
