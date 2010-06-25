package de.uniol.inf.is.odysseus.assoziation;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public interface IHypothesisEvaluationFunction<M extends IProbability> {
		
	public double[][] evaluateAll(double[][] matrix, MVRelationalTuple<M>[] tupleNew, MVRelationalTuple<M>[] tupleOld);
	
	public double evaluate(MVRelationalTuple<M> tupleNew, MVRelationalTuple<M> tupleOld);
	

}
