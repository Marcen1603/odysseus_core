package de.uniol.inf.is.odysseus.assoziation.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * The Hypothesis Generation has two inputstreams:
 * 1 - predicted objects from the prediction operator
 * 2 - the new detected objects
 * Both list now have the same timestamp. The Hypothesis Generation Operator initiates the connection list
 * in the metadata and changes the schema so that the next operator gets both lists (new and old) as input.
 *
 * @author Volker Janz
 *
 */
public class HypothesisGenerationAO<M extends IProbability> extends BinaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private String oldObjListPath;
	private String newObjListPath;

	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;

	public HypothesisGenerationAO() {
		super();
	}

	public HypothesisGenerationAO(HypothesisGenerationAO<M> copy) {
		super(copy);
		this.oldObjListPath = copy.oldObjListPath;
		this.newObjListPath = copy.newObjListPath;
		this.leftSchema = copy.getLeftSchema();
		this.rightSchema = copy.getRightSchema();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList newSchema = new SDFAttributeList();
		newSchema.addAttributes(OrAttributeResolver.getSubSchema(this.getSubscribedToSource(LEFT).getSchema(), this.getNewObjListPath()));
		newSchema.addAttributes(OrAttributeResolver.getSubSchema(this.getSubscribedToSource(RIGHT).getSchema(), this.getOldObjListPath()));
		return newSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return null;
	}

	public void initPaths(String oldObjListPath, String newObjListPath) {
		this.oldObjListPath = oldObjListPath;
		this.newObjListPath = newObjListPath;
	}

	public int[] getNewObjListPath() {
		this.leftSchema = this.getSubscribedToSource(LEFT).getSchema();
		return OrAttributeResolver.getAttributePath(leftSchema, this.newObjListPath);
	}

	public int[] getOldObjListPath() {
		this.rightSchema = this.getSubscribedToSource(RIGHT).getSchema();
		return OrAttributeResolver.getAttributePath(rightSchema, this.oldObjListPath);
	}

	public SDFAttributeList getLeftSchema() {
		return this.getSubscribedToSource(LEFT).getSchema();
	}

	public SDFAttributeList getRightSchema() {
		return this.getSubscribedToSource(RIGHT).getSchema();
	}

}
