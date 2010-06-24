package de.uniol.inf.is.odysseus.assoziation;

import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CorrelationMatrixUtils {
	
	/*
	 * This function generates a correlation matrix of a given scanlist. It is required that a HypothesisGeneration Operator added the connectionlist to the schema before.
	 */
	public static double[][] encodeMatrix(MVRelationalTuple scan, int[] pathToConList, int[] pathToOldList, int[] pathToNewList, int[] pathToConAttrOld, int[] pathToConAttrNew, int[] pathToConAttrRating, SDFAttributeList schema) {
		MVRelationalTuple[] objConList = (MVRelationalTuple[]) ((MVRelationalTuple)OrAttributeResolver.resolveTuple(scan, pathToConList)).getAttributes();
		MVRelationalTuple[] newList = (MVRelationalTuple[]) ((MVRelationalTuple)OrAttributeResolver.resolveTuple(scan, pathToNewList)).getAttributes();
		MVRelationalTuple[] oldList = (MVRelationalTuple[]) ((MVRelationalTuple)OrAttributeResolver.resolveTuple(scan, pathToOldList)).getAttributes();
		
		double[][] correlationMatrix = new double[newList.length][oldList.length];
		
		for(MVRelationalTuple connectionEntry : objConList) {
			Object newRef = OrAttributeResolver.resolveTuple(connectionEntry, pathToConAttrNew);
			Object oldRef = OrAttributeResolver.resolveTuple(connectionEntry, pathToConAttrOld);
			Object conRating = OrAttributeResolver.resolveTuple(connectionEntry, pathToConAttrRating);
			
			int indexOfNewRef = OrAttributeResolver.indexOfAttribute((RelationalTuple<?>) OrAttributeResolver.resolveTuple(scan, pathToNewList), newRef);
			int indexOfOldRef = OrAttributeResolver.indexOfAttribute((RelationalTuple<?>) OrAttributeResolver.resolveTuple(scan, pathToOldList), oldRef);
			
			correlationMatrix[indexOfNewRef][indexOfOldRef] = Double.parseDouble(conRating.toString());
		}
		
		return correlationMatrix;
	}
	
	public static MVRelationalTuple[] decodeMatrix(MVRelationalTuple[] newList, MVRelationalTuple[] oldList, int[][] correlationMatrix) {
		List<MVRelationalTuple> updatedConnections;
		for(int i = 0; i < newList.length; i++) {
			for(int j = 0; j < oldList.length; j++) {
				if(correlationMatrix[i][j] != 0) {
					// TODO verbindung zur liste hinzufügen am ende in array umwandeln und zurückgeben
				}
			}
		}
		return null;
	}

}
