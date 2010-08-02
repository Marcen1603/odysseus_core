package de.uniol.inf.is.odysseus.scars.objecttracking.association;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;

/**
 * This class is used within the association process of the objecttracking architecture
 * to encode and decode a correlation matrix of the current scan.
 *
 * @author Volker Janz
 *
 * @param <M>
 */
public class CorrelationMatrixUtils<M extends IProbability> implements ICorrelationMatrixUtils<M> {

	/**
	 * This function generates a correlation matrix of a given scanlist. It is required that a HypothesisGeneration Operator added the connectionlist to the schema before.
	 */
	@SuppressWarnings("unchecked")
	public double[][] encodeMatrix(MVRelationalTuple<M>[] newList, MVRelationalTuple<M>[] oldList, Connection[] objConList) {

		double[][] correlationMatrix = new double[newList.length][oldList.length];

		for(Connection con : objConList) {
			MVRelationalTuple<M> newRef = (MVRelationalTuple<M>) con.getLeft();
			MVRelationalTuple<M> oldRef = (MVRelationalTuple<M>) con.getRight();
			Double conRating = con.getRating();

			int indexOfNewRef = this.indexOfTupleInTupleArray(newList, newRef);
			int indexOfOldRef = this.indexOfTupleInTupleArray(oldList, oldRef);

			correlationMatrix[indexOfNewRef][indexOfOldRef] = conRating;
		}

		return correlationMatrix;
	}

	/**
	 * Returns a MVRelationalTuple with connections for a given correlation matrix.
	 */
	public ConnectionList decodeMatrix(MVRelationalTuple<M>[] newList, MVRelationalTuple<M>[] oldList, double[][] correlationMatrix) {
		ConnectionList updatedConList = new ConnectionList();
		for(int i = 0; i < newList.length; i++) {
			for(int j = 0; j < oldList.length; j++) {
				if(correlationMatrix[i][j] != 0) {
					updatedConList.add(new Connection(newList[i], oldList[j], correlationMatrix[i][j]));
				}
			}
		}
		return updatedConList;
	}

	private int indexOfTupleInTupleArray(MVRelationalTuple<M>[] tupleArray, MVRelationalTuple<M> tuple) {
		for(int i = 0; i < tupleArray.length; i++) {
			if(tupleArray[i] == tuple) { return i; }
		}
		return -1;
	}

}
