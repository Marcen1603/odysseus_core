package de.uniol.inf.is.odysseus.assoziation;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;

public abstract class AbstractHypothesisEvaluationFunction implements IHypothesisEvaluationFunction {
 	
	@Override
	public abstract int evaluate(MVRelationalTuple tupleNew, MVRelationalTuple tupleOld);

	@Override
	public int[][] evaluateAll(int[][] matrix, MVRelationalTuple[] tupleNew, MVRelationalTuple[] tupleOld) {
		if(matrix == null || tupleNew == null || tupleOld == null) {
			throw new NullPointerException("");
		} 
			
	

		for(int i=0; i<matrix[0].length; i++) {
			for(int j=0; i<matrix.length; j++) {
				matrix[i][j] = evaluate(tupleNew[i], tupleOld[j]);
			}
		}
		return matrix;
	}

}
