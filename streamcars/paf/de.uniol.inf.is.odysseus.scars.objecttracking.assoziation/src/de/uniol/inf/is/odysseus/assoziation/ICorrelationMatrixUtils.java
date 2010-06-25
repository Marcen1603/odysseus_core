package de.uniol.inf.is.odysseus.assoziation;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface ICorrelationMatrixUtils<M extends IProbability> {
	
	/*
	 * This function generates a correlation matrix of a given scanlist. It is required that a HypothesisGeneration Operator added the connectionlist to the schema before.
	 */
	public double[][] encodeMatrix(MVRelationalTuple<M> scan, int[] pathToConList, int[] pathToOldList, int[] pathToNewList, int[] pathToConAttrOld, int[] pathToConAttrNew, int[] pathToConAttrRating, SDFAttributeList schema);
	
	/*
	 * Returns a MVRelationalTuple with connections for a given correlation matrix.
	 */
	public MVRelationalTuple<M>[] decodeMatrix(MVRelationalTuple<M>[] newList, MVRelationalTuple<M>[] oldList, double[][] correlationMatrix, SDFAttributeList schema, int indexOfConAttrOld, int indexOfConAttrNew, int indexOfConAttrRating);

}
