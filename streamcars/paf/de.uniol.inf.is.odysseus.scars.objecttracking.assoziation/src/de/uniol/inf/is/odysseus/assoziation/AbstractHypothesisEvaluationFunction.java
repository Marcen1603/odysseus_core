package de.uniol.inf.is.odysseus.assoziation;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public abstract class AbstractHypothesisEvaluationFunction<M extends IProbability> implements IHypothesisEvaluationFunction<M> {
 	
	@Override
	public abstract double evaluate(MVRelationalTuple<M> tupleNew, MVRelationalTuple<M> tupleOld);

	@Override
	public double[][] evaluateAll(double[][] matrix, MVRelationalTuple<M>[] tupleNew, MVRelationalTuple<M>[] tupleOld) {
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
