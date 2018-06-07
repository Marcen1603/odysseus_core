package de.uniol.inf.is.odysseus.anomalydetection.logicaloperator;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DEVIATIONSEQUENCELEARN", doc = "Learns deviation of (each point of a) sequence", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationSequenceLearnAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4139317638350922577L;

	public static final int DATA_OUT = 0;
	public static final int LEARN_OUT = 1;

	private String valueAttributeName;
	private int sequencesToLearn;

	private List<SDFAttribute> groupingAttributes;
	private boolean fastGrouping;

	public DeviationSequenceLearnAO() {
		this.valueAttributeName = "value";
		this.sequencesToLearn = 0;
	}

	public DeviationSequenceLearnAO(DeviationSequenceLearnAO ao) {
		super(ao);
		this.valueAttributeName = ao.getValueAttributeName();
		this.sequencesToLearn = ao.getCurvesToLearn();
		this.fastGrouping = ao.isFastGrouping();
		this.groupingAttributes = ao.getGroupingAttributes();
	}

	@Parameter(type = StringParameter.class, name = "attribute", optional = true, doc = "Name of the attribute which should be analysed")
	public void setValueAttributeName(String valueAttributeName) {
		this.valueAttributeName = valueAttributeName;
	}

	public String getValueAttributeName() {
		return valueAttributeName;
	}

	@Parameter(type = IntegerParameter.class, name = "sequencesToLearn", optional = true, doc = "The number of (correct) sequences to learn from. The first x sequences will define the perfect sequence the others are compared to. If set to 0, the operator will not stop to learn (learn infinity sequences). Default is 0.")
	public void setSequencesToLearn(int sequencesToLearn) {
		this.sequencesToLearn = sequencesToLearn;
	}

	public int getCurvesToLearn() {
		return sequencesToLearn;
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

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true, doc = "To group the tuples into the single parts of the sequence.")
	public void setGroupingAttributes(List<SDFAttribute> groupingAttributes) {
		this.groupingAttributes = groupingAttributes;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DeviationSequenceLearnAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int port) {
		if (port == DATA_OUT) {
			SDFSchema inSchema = getInputSchema(0);
			SDFAttribute groupId = new SDFAttribute(null, "group", SDFDatatype.LONG, null, null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(groupId);
			outputAttributes.addAll(inSchema.getAttributes());
			SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema());
			this.setOutputSchema(DATA_OUT, outputSchema);
			return getOutputSchema(DATA_OUT);
		} else {
			// Transfer the mean and the standard deviation to the next operator

			// The number of the tuple in the sequence
			SDFAttribute groupId = new SDFAttribute(null, "group", SDFDatatype.LONG, null, null, null);
			SDFAttribute meanValue = new SDFAttribute(null, "mean", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute standardDeviation = new SDFAttribute(null, "standardDeviation", SDFDatatype.DOUBLE, null,
					null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(groupId);
			outputAttributes.add(meanValue);
			outputAttributes.add(standardDeviation);

			SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema());
			this.setOutputSchema(LEARN_OUT, outputSchema);

			return getOutputSchema(LEARN_OUT);
		}
	}

}
