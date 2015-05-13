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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * 
 * This operator searches for rare patterns in data. It's especially designed to
 * find rare state sequences. E.g. if state "b" nearly always follows state "a",
 * but very seldom a "c" follows a. This operator should find the seldom pattern
 * "a" -> "c".
 * 
 * @author Tobias Brandt
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "RarePattern", doc = "Searches for rare pattern in a stream of (discrete) states", category = { LogicalOperatorCategory.PROCESSING })
public class RarePatternAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -7990417520941357348L;

	private int depth;
	private double minRelativeFrequency;

	public RarePatternAO() {
		this.depth = 2;
		this.minRelativeFrequency = 0.3;
	}

	public RarePatternAO(RarePatternAO ao) {
		this.depth = ao.getDepth();
		this.minRelativeFrequency = ao.getMinRelativeFrequency();
	}

	@Parameter(type = IntegerParameter.class, name = "treeDepth", optional = true, doc = "The depth of the tree. Default is 2.")
	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Parameter(type = DoubleParameter.class, name = "minRelativeFrequency", optional = true, doc = "The minimal relative frequency of that path. The default is 0.3 (which means 30%).")
	public void setMinRelativeFrequency(double minRelativeFrequency) {
		this.minRelativeFrequency = minRelativeFrequency;
	}

	public int getDepth() {
		return depth;
	}

	public double getMinRelativeFrequency() {
		return minRelativeFrequency;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RarePatternAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add the anomaly-score to the attributes and keep the old attributes
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute anomalyScore = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute path = new SDFAttribute(null, "path", SDFDatatype.DOUBLE, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(anomalyScore);
		outputAttributes.add(path);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

}
