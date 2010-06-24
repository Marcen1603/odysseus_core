package de.uniol.inf.is.odysseus.assoziation;

public abstract class AbstractHypothesisEvaluationFunction implements IHypothesisEvaluationFunction {
 	
	@Override
	public abstract int evaluate(Object tupleNew, Object tupleOld);

	@Override
	public int[][] evaluateAll(int[][] matrix, Object[] tupleNew, Object[] tupleOld) {
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
