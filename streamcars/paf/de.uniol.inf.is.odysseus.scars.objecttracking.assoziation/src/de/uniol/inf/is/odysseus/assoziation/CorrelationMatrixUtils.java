package de.uniol.inf.is.odysseus.assoziation;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;

public class CorrelationMatrixUtils {
	
	/***
	 * This function generates a correlation matrix of a given scanlist. It is required that a HypothesisGeneration Operator added the connectionlist to the schema before.
	 * @param scan Scan with added connection list
	 * @param pathToConnectionList Path to connection list
	 * @param pathToOldList Path to old list
	 * @param pathToNewList Path to new list
	 * @return
	 */
	public static int[][] encodeMatrix(MVRelationalTuple scan, int[] pathToConnectionList, int[] pathToOldList, int[] pathToNewList) {
		MVRelationalTuple[] objConList = (MVRelationalTuple[]) ((MVRelationalTuple)OrAttributeResolver.resolveTuple(scan, pathToConnectionList)).getAttributes();
		MVRelationalTuple[] newList = (MVRelationalTuple[]) ((MVRelationalTuple)OrAttributeResolver.resolveTuple(scan, pathToNewList)).getAttributes();
		MVRelationalTuple[] oldList = (MVRelationalTuple[]) ((MVRelationalTuple)OrAttributeResolver.resolveTuple(scan, pathToOldList)).getAttributes();
		int[][] correlationMatrix = new int[oldList.length][newList.length];
		/*
		RatedPair[] ratedPairs = new RatedPair[objConList.length];
		for(MVRelationalTuple tuple : objConList) {
			ratedPairs[] = (RatedPair) OrAttributeResolver.resolveTuple(scan, pathToConnectionList);
		}
		for(RatedPair p : OrAttributeResolver.resolveTuple(scan, pathToConnectionList))
		RatedPair pair = (RatedPair) OrAttributeResolver.resolveTuple(scan, pathToConnectionList);
		*/
		return null;
	}
	
	public static MVRelationalTuple decodeMatrix(int[][] correlationMatrix) {
		return null;
	}

}
