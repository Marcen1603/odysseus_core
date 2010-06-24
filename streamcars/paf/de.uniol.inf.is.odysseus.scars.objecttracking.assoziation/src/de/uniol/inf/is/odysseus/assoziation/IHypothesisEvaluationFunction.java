package de.uniol.inf.is.odysseus.assoziation;

public interface IHypothesisEvaluationFunction {
		
	public int[][] evaluateAll(int[][] matrix, Object[] tupleNew, Object[] tupleOld);
	
	public int evaluate(Object tupleNew, Object tupleOld);
	

}
