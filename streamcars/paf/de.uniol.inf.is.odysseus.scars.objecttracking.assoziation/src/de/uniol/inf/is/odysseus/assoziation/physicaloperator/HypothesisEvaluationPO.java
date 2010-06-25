package de.uniol.inf.is.odysseus.assoziation.physicaloperator;

import de.uniol.inf.is.odysseus.assoziation.AbstractHypothesisEvaluationFunction;
import de.uniol.inf.is.odysseus.assoziation.CorrelationMatrixUtils;
import de.uniol.inf.is.odysseus.assoziation.logicaloperator.HypothesisEvaluationAO;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;

/**
 * Physical Operator for the rating of connections within the association process.
 * 
 * @author Volker Janz
 *
 * @param <M>
 */
public class HypothesisEvaluationPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {
	
	private int[] oldObjListPath;
	private int[] newObjListPath;
	private int[] objConListPath;
	private int[] conAttrNewPath;
	private int[] conAttrOldPath;
	private int[] conAttrRatingPath;
	
	private AbstractHypothesisEvaluationFunction<M> hypothesisEvaluationFunction;
	
	public HypothesisEvaluationPO(HypothesisEvaluationAO<M> hypothesisEvaluationAO) {
		this.oldObjListPath = hypothesisEvaluationAO.getOldObjListPath();
		this.newObjListPath = hypothesisEvaluationAO.getNewObjListPath();
		this.objConListPath = hypothesisEvaluationAO.getObjConListPath();
		this.conAttrNewPath = hypothesisEvaluationAO.getConAttrNewPath();
		this.conAttrOldPath = hypothesisEvaluationAO.getConAttrOldPath();
		this.conAttrRatingPath = hypothesisEvaluationAO.getConAttrRatingPath();
		this.hypothesisEvaluationFunction = hypothesisEvaluationAO.getHypothesisEvaluationFunction();
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.newObjListPath)).getAttributes();
		MVRelationalTuple<M>[] oldList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.oldObjListPath)).getAttributes();
		MVRelationalTuple<M>[] objConList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.objConListPath)).getAttributes();
		CorrelationMatrixUtils<M> corUtils = new CorrelationMatrixUtils<M>();
		double[][] corMatrix = corUtils.encodeMatrix(object, this.objConListPath, this.oldObjListPath, this.newObjListPath, this.conAttrOldPath, this.conAttrNewPath, this.conAttrRatingPath, this.getOutputSchema());
		corMatrix = this.hypothesisEvaluationFunction.evaluateAll(corMatrix, newList, oldList);
		MVRelationalTuple<M>[] newObjConList = corUtils.decodeMatrix(newList, oldList, corMatrix, OrAttributeResolver.getSubSchema(this.getOutputSchema(), this.objConListPath), this.conAttrOldPath[this.conAttrOldPath.length-1], this.conAttrNewPath[this.conAttrNewPath.length-1], this.conAttrRatingPath[this.conAttrRatingPath.length-1]);
		MVRelationalTuple<M> newObjConListTuple = new MVRelationalTuple<M>(OrAttributeResolver.getSubSchema(this.getOutputSchema(), this.objConListPath));
		for(int i = 0; i < newObjConList.length; i++) {
			newObjConListTuple.addAttributeValue(i, newObjConList[i]);
		}
		// replace old con list in current scan
		// TODO add a class which enables to use a path with setAttribute
		object.setAttribute(this.objConListPath[0], newObjConListTuple);
		// ready -> transfer to next operator
		transfer(object);
	}

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
