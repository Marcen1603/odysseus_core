package de.uniol.inf.is.odysseus.assoziation.logicaloperator;

import de.uniol.inf.is.odysseus.assoziation.AbstractHypothesisEvaluationFunction;
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
	
	private AbstractHypothesisEvaluationFunction<M> hypothesisEvaluationFunction;
	
	private int[] oldObjListPath;
	private int[] newObjListPath;
	private int[] objConListPath;
	private int[] conAttrNewPath;
	private int[] conAttrOldPath;
	private int[] conAttrRatingPath;

	public HypothesisEvaluationAO() {
		super();
	}
	
	public HypothesisEvaluationAO(HypothesisEvaluationAO<M> copy) {
		super(copy);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AbstractHypothesisEvaluationFunction<M> getHypothesisEvaluationFunction() {
		return hypothesisEvaluationFunction;
	}

	public void setHypothesisEvaluationFunction(
			AbstractHypothesisEvaluationFunction<M> hypothesisEvaluationFunction) {
		this.hypothesisEvaluationFunction = hypothesisEvaluationFunction;
	}
	
	public void initNeededAttributeIndices(SDFAttributeList input, String[] oldObjListPath, String[] newObjListPath, String[] objConListPath, String[] conAttrNewPath, String[] conAttrOldPath, String[] conAttrRatingPath) {
		this.initOldObjListPath(input, oldObjListPath);
		this.initNewObjListPath(input, newObjListPath);
		this.initObjConListPath(input, objConListPath);
		this.initConAttrNewPath(input, conAttrNewPath);
		this.initConAttrOldPath(input, conAttrOldPath);
		this.initConAttrRatingPath(input, conAttrRatingPath);
	}
	
	public int[] getOldObjListPath() {
		return oldObjListPath;
	}

	public int[] getNewObjListPath() {
		return newObjListPath;
	}

	public int[] getObjConListPath() {
		return objConListPath;
	}
	
	public int[] getConAttrNewPath() {
		return conAttrNewPath;
	}
	
	public int[] getConAttrOldPath() {
		return conAttrOldPath;
	}
	
	public int[] getConAttrRatingPath() {
		return conAttrRatingPath;
	}
	
	private void initOldObjListPath(SDFAttributeList input, String[] oldObjListPath) {
		this.oldObjListPath = OrAttributeResolver.resolveIndices(input, oldObjListPath);
	}
	
	private void initNewObjListPath(SDFAttributeList input, String[] newObjListPath) {
		this.newObjListPath = OrAttributeResolver.resolveIndices(input, newObjListPath);
	}
	
	private void initObjConListPath(SDFAttributeList input, String[] objConListPath) {
		this.objConListPath = OrAttributeResolver.resolveIndices(input, objConListPath);
	}
	
	private void initConAttrNewPath(SDFAttributeList input, String[] conAttrNewPath) {
		this.conAttrNewPath = OrAttributeResolver.resolveIndices(input, conAttrNewPath);
	}
	
	private void initConAttrOldPath(SDFAttributeList input, String[] conAttrOldPath) {
		this.conAttrOldPath = OrAttributeResolver.resolveIndices(input, conAttrOldPath);
	}
	
	private void initConAttrRatingPath(SDFAttributeList input, String[] conAttrRatingPath) {
		this.conAttrRatingPath = OrAttributeResolver.resolveIndices(input, conAttrRatingPath);
	}
}
