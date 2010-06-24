package de.uniol.inf.is.odysseus.assoziation;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;

public interface IHypothesisEvaluationFunction {
		
	public int[][] evaluateAll(int[][] matrix, MVRelationalTuple[] tupleNew, MVRelationalTuple[] tupleOld);
	
	public int evaluate(MVRelationalTuple tupleNew, MVRelationalTuple tupleOld);
	

}
