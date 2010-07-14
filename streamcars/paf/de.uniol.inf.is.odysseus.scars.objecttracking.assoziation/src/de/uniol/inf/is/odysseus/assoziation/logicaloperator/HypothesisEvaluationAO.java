package de.uniol.inf.is.odysseus.assoziation.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Logical Operator for the rating of connections within the association process.
 * 
 * @author Volker Janz
 *
 * @param <M>
 */
public class HypothesisEvaluationAO<M extends IProbability> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	
	private int[] oldObjListPath;
	private int[] newObjListPath;
	
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;

	public HypothesisEvaluationAO() {
		super();
	}
	
	public HypothesisEvaluationAO(HypothesisEvaluationAO<M> copy) {
		super(copy);
	}
	
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}
	
	

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void initSchemata(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
	}
	
	public SDFAttributeList getLeftSchema() {
		return this.leftSchema;
	}
	
	public SDFAttributeList getRightSchema() {
		return this.rightSchema;
	}
	
	public void initNeededAttributeIndices(SDFAttributeList input, String[] oldObjListPath, String[] newObjListPath, String[] objConListPath, String[] conAttrNewPath, String[] conAttrOldPath, String[] conAttrRatingPath) {
		this.initOldObjListPath(input, oldObjListPath);
		this.initNewObjListPath(input, newObjListPath);
	}
	
	public int[] getOldObjListPath() {
		return oldObjListPath;
	}

	public int[] getNewObjListPath() {
		return newObjListPath;
	}

	
	private void initOldObjListPath(SDFAttributeList input, String[] oldObjListPath) {
		this.oldObjListPath = OrAttributeResolver.resolveIndices(input, oldObjListPath);
	}
	
	private void initNewObjListPath(SDFAttributeList input, String[] newObjListPath) {
		this.newObjListPath = OrAttributeResolver.resolveIndices(input, newObjListPath);
	}
}
