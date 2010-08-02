package de.uniol.inf.is.odysseus.scars.objecttracking.association;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;

public interface ICorrelationMatrixUtils<M extends IProbability> {

	/*
	 * This function generates a correlation matrix of a given scanlist. It is required that a HypothesisGeneration Operator added the connectionlist to the schema before.
	 */
	public double[][] encodeMatrix(MVRelationalTuple<M>[] newList, MVRelationalTuple<M>[] oldList, Connection[] objConList);

	/*
	 * Returns a MVRelationalTuple with connections for a given correlation matrix.
	 */
	public ConnectionList decodeMatrix(MVRelationalTuple<M>[] newList, MVRelationalTuple<M>[] oldList, double[][] correlationMatrix);

}
