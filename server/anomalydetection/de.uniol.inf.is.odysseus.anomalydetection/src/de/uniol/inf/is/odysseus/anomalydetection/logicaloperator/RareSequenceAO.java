package de.uniol.inf.is.odysseus.anomalydetection.logicaloperator;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * This operator searches for rare sequences in data. It's especially designed to
 * find rare state sequences. E.g. if state "b" nearly always follows state "a",
 * but very seldom a "c" follows a. This operator should find the seldom pattern
 * "a" -> "c".
 * 
 * @author Tobias Brandt
 *
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 1, name = "RareSequence", doc = "Searches for rare sequences in a stream of (discrete) states", category = {
		LogicalOperatorCategory.PROCESSING })
public class RareSequenceAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -7990417520941357348L;

	private static final int DATA_PORT = 0;
	private static final int BACKUP_PORT = 1;

	private int depth;
	private double minRelativeFrequencyPath;
	private double minRelativeFrequencyNode;
	private boolean firstTupleIsRoot;
	private String uniqueBackupId;

	public RareSequenceAO() {
		this.depth = 2;
		this.minRelativeFrequencyPath = 0.3;
		this.minRelativeFrequencyNode = 0.3;
		this.firstTupleIsRoot = false;
		this.uniqueBackupId = "rarePattern_" + depth;
	}

	public RareSequenceAO(RareSequenceAO ao) {
		super(ao);
		this.depth = ao.getDepth();
		this.minRelativeFrequencyPath = ao.getMinRelativeFrequencyPath();
		this.minRelativeFrequencyNode = ao.getMinRelativeFrequencyNode();
		this.firstTupleIsRoot = ao.isFirstTupleIsRoot();
		this.uniqueBackupId = ao.getUniqueBackupId();
	}

	@Parameter(type = IntegerParameter.class, name = "treeDepth", optional = true, doc = "The maximum depth of the tree. Default is 2.")
	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Parameter(type = DoubleParameter.class, name = "minRelativeFrequencyPath", optional = true, doc = "The minimal relative frequency of that path. The default is 0.3 (which means 30%).")
	public void setMinRelativeFrequencyPath(double minRelativeFrequency) {
		this.minRelativeFrequencyPath = minRelativeFrequency;
	}

	@Parameter(type = DoubleParameter.class, name = "minRelativeFrequencyNode", optional = true, doc = "The minimal relative frequency of all single nodes of the path. The default is 0.3 (which means 30%)")
	public void setMinRelativeFrequencyNode(double minRelativeFrequencyNode) {
		this.minRelativeFrequencyNode = minRelativeFrequencyNode;
	}

	@Parameter(type = BooleanParameter.class, name = "firstTupleIsRoot", optional = true, doc = "If true, tuples which are equal to the first tuple are always the root. It could be good to set the maxdepth higher than the expected number of states between two occurences of the root state.")
	public void setFirstTupleIsRoot(boolean firstTupleIsRoot) {
		this.firstTupleIsRoot = firstTupleIsRoot;
	}

	@Parameter(type = StringParameter.class, name = "uniqueBackupId", optional = true, doc = "A unique ID for this operator to save and read backup data.")
	public void setUniqueBackupId(String uniqueBackupId) {
		this.uniqueBackupId = uniqueBackupId;
	}

	public int getDepth() {
		return depth;
	}

	public double getMinRelativeFrequencyPath() {
		return minRelativeFrequencyPath;
	}

	public boolean isFirstTupleIsRoot() {
		return firstTupleIsRoot;
	}

	public String getUniqueBackupId() {
		return uniqueBackupId;
	}

	public double getMinRelativeFrequencyNode() {
		return minRelativeFrequencyNode;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RareSequenceAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		if (pos == DATA_PORT) {
			// add the anomaly-score to the attributes and keep the old
			// attributes
			SDFSchema inSchema = getInputSchema(DATA_PORT);
			SDFAttribute anomalyScore = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
			SDFAttribute pathProbability = new SDFAttribute(null, "pathProbability", SDFDatatype.DOUBLE, null, null,
					null);
			SDFAttribute path = new SDFAttribute(null, "path", SDFDatatype.STRING, null, null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.addAll(inSchema.getAttributes());
			outputAttributes.add(anomalyScore);
			outputAttributes.add(pathProbability);
			outputAttributes.add(path);
			SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
			setOutputSchema(DATA_PORT, outSchema);
		} else if (pos == BACKUP_PORT) {
			SDFSchema inSchema = getInputSchema(DATA_PORT);
			SDFAttribute tree = new SDFAttribute(null, "tree", SDFDatatype.STRING, null, null, null);
			SDFAttribute backupId = new SDFAttribute(null, "backupId", SDFDatatype.DOUBLE, null, null, null);
			List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
			outputAttributes.add(tree);
			outputAttributes.add(backupId);
			SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
			setOutputSchema(BACKUP_PORT, outSchema);
		}

		return getOutputSchema(pos);
	}

}
