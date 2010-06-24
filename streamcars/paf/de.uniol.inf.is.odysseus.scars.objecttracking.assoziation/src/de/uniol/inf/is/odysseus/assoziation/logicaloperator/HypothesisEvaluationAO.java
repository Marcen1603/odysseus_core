package de.uniol.inf.is.odysseus.assoziation.logicaloperator;

import de.uniol.inf.is.odysseus.assoziation.AbstractHypothesisEvaluationFunction;
import de.uniol.inf.is.odysseus.assoziation.physicaloperator.HypothesisEvaluationPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HypothesisEvaluationAO <M extends IProbability> extends UnaryLogicalOp{
	
	private AbstractHypothesisEvaluationFunction hypothesisEvaluationFunction;
	
	private int[] oldObjListPath;
	private int[] newObjListPath;
	private int[] objConListPath;

	public HypothesisEvaluationAO() {
		super();
	}
	
	public HypothesisEvaluationAO(HypothesisEvaluationAO copy) {
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
	
	public AbstractHypothesisEvaluationFunction getHypothesisEvaluationFunction() {
		return hypothesisEvaluationFunction;
	}

	public void setHypothesisEvaluationFunction(
			AbstractHypothesisEvaluationFunction hypothesisEvaluationFunction) {
		this.hypothesisEvaluationFunction = hypothesisEvaluationFunction;
	}
	
	public void initNeededAttributeIndices(SDFAttributeList input, String[] oldObjListPath, String[] newObjListPath, String[] objConListPath) {
		this.initOldObjListPath(input, oldObjListPath);
		this.initNewObjListPath(input, newObjListPath);
		this.initObjConListPath(input, objConListPath);
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
	
	private void initOldObjListPath(SDFAttributeList input, String[] oldObjListPath) {
		this.oldObjListPath = OrAttributeResolver.resolveIndices(input, oldObjListPath);
	}
	
	private void initNewObjListPath(SDFAttributeList input, String[] newObjListPath) {
		this.newObjListPath = OrAttributeResolver.resolveIndices(input, newObjListPath);
	}
	
	private void initObjConListPath(SDFAttributeList input, String[] objConListPath) {
		this.objConListPath = OrAttributeResolver.resolveIndices(input, objConListPath);
	}

}
