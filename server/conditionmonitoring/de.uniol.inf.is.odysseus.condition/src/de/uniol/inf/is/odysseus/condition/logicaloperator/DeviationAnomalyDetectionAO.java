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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "DEVIATIONANOMALYDETECTION", doc = "Searches for anomalies on the base of the standard-deviation. First port: data, Second port: deviation information.", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationAnomalyDetectionAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -7283600658977972140L;

	private double interval;
	private String nameOfValue;
	private List<SDFAttribute> groupingAttributes;
	private boolean fastGrouping;
	
	private int tuplesToWait;
	private double maxRelativeChange;
	
	private boolean windowChecking;
	private boolean onlyFirstAnomaly;
	private boolean reportEndOfAnomalies;
	private boolean deliverUnlearnedTuples;
	
	private boolean isTimeSensitive;

	public DeviationAnomalyDetectionAO() {
		this.interval = 3.0;
		this.nameOfValue = "value";
		this.windowChecking = false;
		this.reportEndOfAnomalies = false;
		this.deliverUnlearnedTuples = false;
		this.tuplesToWait = 0;
		this.maxRelativeChange = 0;
		this.isTimeSensitive = false;
	}

	public DeviationAnomalyDetectionAO(DeviationAnomalyDetectionAO ao) {
		super(ao);
		this.setInterval(ao.getInterval());
		this.setNameOfValue(ao.getNameOfValue());
		if (ao.groupingAttributes != null) {
			this.groupingAttributes = new ArrayList<SDFAttribute>(ao.groupingAttributes);
		}
		this.fastGrouping = ao.isFastGrouping();
		this.windowChecking = ao.isWindowChecking();
		this.onlyFirstAnomaly = ao.isOnlyOnChange();
		this.reportEndOfAnomalies = ao.isReportEndOfAnomalyWindows();
		this.deliverUnlearnedTuples = ao.isDeliverUnlearnedTuples();
		this.tuplesToWait = ao.getTuplesToWait();
		this.maxRelativeChange = ao.getMaxRelativeChange();
		this.isTimeSensitive = ao.isTimeSensitive();
	}

	@Parameter(type = DoubleParameter.class, name = "interval", optional = true, doc = "Defines, how many standard deviations are allowed for a tuple to be different from the mean. 3.0 is the default value. Choose a smaller value to get more anomalies.")
	public void setInterval(double interval) {
		this.interval = interval;
	}

	@Parameter(type = StringParameter.class, name = "attribute", optional = false, doc = "Name of the attribute which should be analysed")
	public void setNameOfValue(String nameOfValue) {
		this.nameOfValue = nameOfValue;
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true, doc = "To group the tuples and learn a unique deviation for each group. If used with a deviationLearn operator, use the group attribute it produces as group_by in this operator.")
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}

	@Parameter(name = "fastGrouping", type = BooleanParameter.class, optional = true, doc = "Use hash code instead of tuple compare to create group. Potentially unsafe!")
	public void setFastGrouping(boolean fastGrouping) {
		this.fastGrouping = fastGrouping;
	}

	@Parameter(name = "windowChecking", type = BooleanParameter.class, optional = true, doc = "If true, it's checked if the last (tumbling) window had an anomaly. If two following windows have at least one anomaly per window, all non-anomaly tuples between the two anomalies will be send, too. AnomalyScore for the non-anomaly tuples between the anomalies is Double.MINVALUE.")
	public void setWindowChecking(boolean windowChecking) {
		this.windowChecking = windowChecking;
	}

	@Parameter(name = "onlyFirstAnomaly", type = BooleanParameter.class, optional = true, doc = "If you use windowChecking and set this option true, then an anomaly tuple is only send, if the last window had no anomaly. You get exalctly one tuple at the beginning of an anomal phase.")
	public void setOnlyOnChange(boolean onlyOnChange) {
		this.onlyFirstAnomaly = onlyOnChange;
	}

	@Parameter(name = "reportEndOfAnomalies", type = BooleanParameter.class, optional = true, doc = "If you use windowChecking and set this option true, then a tuple with anomaly score = 0 is send, if the last window had an anomaly, but the current has not.")
	public void setReportEndOfAnomalyWindows(boolean reportEndOfAnomalyWindows) {
		this.reportEndOfAnomalies = reportEndOfAnomalyWindows;
	}
	
	@Parameter(name = "deliverUnlearnedElements", type = BooleanParameter.class, optional = true, doc = "If you want to get tuples for which no deviation information is available (e.g. the first tuple), you can set this to true. Default is false.")
	public void setDeliverUnlearnedTuples(boolean deliverUnlearnedTuples) {
		this.deliverUnlearnedTuples = deliverUnlearnedTuples;
	}

	@Parameter(name = "tuplesToWait", type = IntegerParameter.class, optional = true, doc = "If you want the operator to learn for a while before it starts to analyse, you can set that the operator has to wait x tuples (each group has its own counter) before it starts to analyse. Default is 0.")
	public void setTuplesToWait(int tuplesToWait) {
		this.tuplesToWait = tuplesToWait;
	}
	
	@Parameter(name = "maxRelativeChange", type = DoubleParameter.class, optional = true, doc = "If you want the operator to learn for a while before it starts to analyse, you can set that the operator has to wait until the relative change between the last mean and the new mean from the operator before this one is lower than the given value. This avoids early false-positives. Can be used together with 'tuplesToWait'. Default is 0.")
	public void setMaxRelativeChange(double maxRelativeChange) {
		this.maxRelativeChange = maxRelativeChange;
	}

	@Parameter(name = "timeSensitive", type = BooleanParameter.class, optional = true, doc = "If you do an interval analysis and want to check if the duration since the last tuple when a punctuation arrives, you can use this option. Assumes, that the attribute which is analysed is the time between two tuples. Only sends a tuple, if the time between the last sent anomaly-tuple based on punctuations and the next anomaly-tuple based on punctuations is an anomaly itself. Does the check for all groups. Default is false.")
	public void setTimeSensitive(boolean isTimeSensitive) {
		this.isTimeSensitive = isTimeSensitive;
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
		return onlyFirstAnomaly;
	}
	
	public boolean isReportEndOfAnomalyWindows() {
		return reportEndOfAnomalies;
	}
	
	public boolean isDeliverUnlearnedTuples() {
		return deliverUnlearnedTuples;
	}
	
	public int getTuplesToWait() {
		return tuplesToWait;
	}
	
	public double getMaxRelativeChange() {
		return maxRelativeChange;
	}
	
	public boolean isTimeSensitive() {
		return isTimeSensitive;
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
		if (this.isTimeSensitive) {
			SDFAttribute punctuationDuration = new SDFAttribute(null, "punctuationDuration", SDFDatatype.DOUBLE, null, null, null);
			outputAttributes.add(punctuationDuration);
		}
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
