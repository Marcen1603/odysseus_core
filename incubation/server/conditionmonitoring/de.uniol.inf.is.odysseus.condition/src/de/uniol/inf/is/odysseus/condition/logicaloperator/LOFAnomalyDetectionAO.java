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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "LOFANOMALYDETECTION", doc = "Searches for anomalies on the base of the local outlier factor algorithm.", category = { LogicalOperatorCategory.PROCESSING })
public class LOFAnomalyDetectionAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2933366059214152576L;

	// Number of neighbors
	private int k;
	private double lofAnomalyValue;
	private String nameOfValue;
	private List<SDFAttribute> groupingAttributes;
	private boolean fastGrouping;

	public LOFAnomalyDetectionAO() {
		this.k = 10;
		this.lofAnomalyValue = 1.5;
		this.nameOfValue = "value";
	}

	public LOFAnomalyDetectionAO(LOFAnomalyDetectionAO ao) {
		this.k = ao.getNumberOfNeighbors();
		this.lofAnomalyValue = ao.getLOFAnomalyValue();
		this.nameOfValue = ao.getNameOfValue();
		this.groupingAttributes = ao.getGroupingAttributes();
		this.fastGrouping = ao.isFastGrouping();
	}

	@Parameter(type = IntegerParameter.class, name = "NEIGHBORS", optional = true, doc = "The number of neighbors used, sometimes called k")
	public void setNumberOfNeighbors(int number) {
		this.k = number;
	}

	@Parameter(type = DoubleParameter.class, name = "LOFVALUE", optional = true, doc = "The value from which the tuples are declared as anomalies. Values near 1 are normal, higher values are anomalies. Standard is 1.5.")
	public void setLOFAnomalyValue(double value) {
		this.lofAnomalyValue = value;
	}
	
	@Parameter(type = StringParameter.class, name = "nameOfParameter", optional = true, doc = "Name of the attribute which should be analysed")
	public void setNameOfValue(String nameOfValue) {
		this.nameOfValue = nameOfValue;
	}
	
	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true, doc = "Group by the given attribute, e.g. if you have a context like 'on' and 'off' you want to analyse separately.")
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}

	@Parameter(name = "fastGrouping", type = BooleanParameter.class, optional = true, doc = "Use hash code instead of tuple compare to create group. Potentially unsafe!")
	public void setFastGrouping(boolean fastGrouping) {
		this.fastGrouping = fastGrouping;
	}

	public int getNumberOfNeighbors() {
		return this.k;
	}

	public double getLOFAnomalyValue() {
		return this.lofAnomalyValue;
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
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add the anomaly-score to the attributes and keep the old attributes
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute lofValue = new SDFAttribute(null, "LOF", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute anomalyScore = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(lofValue);
		outputAttributes.add(anomalyScore);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new LOFAnomalyDetectionAO(this);
	}

}
