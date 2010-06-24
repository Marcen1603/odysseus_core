package de.uniol.inf.is.odysseus.assoziation.physicaloperator;

import de.uniol.inf.is.odysseus.assoziation.AbstractHypothesisEvaluationFunction;
import de.uniol.inf.is.odysseus.assoziation.logicaloperator.HypothesisEvaluationAO;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;

public class HypothesisEvaluationPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private MVRelationalTuple<M> oldList;
	private MVRelationalTuple<M> newList;
	private MVRelationalTuple<M> conList;
	
	private int[] oldObjListPath;
	private int[] newObjListPath;
	private int[] objConListPath;
	
	private AbstractHypothesisEvaluationFunction hypothesisEvaluationFunction;
	
	public HypothesisEvaluationPO(HypothesisEvaluationAO<M> hypothesisEvaluationAO) {
		this.oldObjListPath = hypothesisEvaluationAO.getOldObjListPath();
		this.newObjListPath = hypothesisEvaluationAO.getNewObjListPath();
		this.objConListPath = hypothesisEvaluationAO.getObjConListPath();
		this.hypothesisEvaluationFunction = hypothesisEvaluationAO.getHypothesisEvaluationFunction();
		
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.newObjListPath)).getAttributes();
		MVRelationalTuple<M>[] oldList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.oldObjListPath)).getAttributes();;
		MVRelationalTuple<M>[] objConList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.objConListPath)).getAttributes();;
		// TODO Matrizen generieren
		// this.hypothesisEvaluationFunction.evaluateAll(matrix, newList, oldList);
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
