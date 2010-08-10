package de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
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

	private SchemaHelper leftSh;
	private SchemaHelper rightSh;

	public HypothesisGenerationAO() {
		super();
		leftSh = new SchemaHelper(this.getSubscribedToSource(LEFT).getSchema());
		rightSh = new SchemaHelper(this.getSubscribedToSource(RIGHT).getSchema());
	}

	public HypothesisGenerationAO(HypothesisGenerationAO<M> copy) {
		super(copy);
		this.oldObjListPath = copy.oldObjListPath;
		this.newObjListPath = copy.newObjListPath;
		copy.leftSh = new SchemaHelper(this.getSubscribedToSource(LEFT).getSchema());
		copy.rightSh = new SchemaHelper(this.getSubscribedToSource(RIGHT).getSchema());

	}

	// LEFT -> SOURCE (neu erkannte objekte)
	// RIGHT -> PREDICTION (alte, prädizierte objekte)

	// Schema von PREDICTION wird erweitert um Schema der Liste von SOURCE (analog zur Änderung des Tupels im PO)

	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList predictionSchema = this.getSubscribedToSource(RIGHT).getSchema();
		predictionSchema.addAttributes(OrAttributeResolver.getSubSchema(this.getSubscribedToSource(LEFT).getSchema(), this.getNewObjListPath()));
		return predictionSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new HypothesisGenerationAO<M>(this);
	}

	public void initPaths(String oldObjListPath, String newObjListPath) {
		this.oldObjListPath = oldObjListPath;
		this.newObjListPath = newObjListPath;
	}

	public int[] getNewObjListPath() {
		return leftSh.getSchemaIndexPath(newObjListPath).toArray();
	}

	public int[] getOldObjListPath() {
		return rightSh.getSchemaIndexPath(oldObjListPath).toArray();
	}

	public SDFAttributeList getLeftSchema() {
		if(this.getSubscribedToSource(LEFT) != null) {
			return this.getSubscribedToSource(LEFT).getSchema();
		}else {
			return null;
		}
	}

	public SDFAttributeList getRightSchema() {
		if(this.getSubscribedToSource(RIGHT) != null) {
			return this.getSubscribedToSource(RIGHT).getSchema();
		}else {
			return null;
		}
	}

}
