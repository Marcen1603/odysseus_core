package de.uniol.inf.is.odysseus.assoziation;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
	public double[][] encodeMatrix(MVRelationalTuple<M> scan, int[] pathToConList, int[] pathToOldList, int[] pathToNewList, int[] pathToConAttrOld, int[] pathToConAttrNew, int[] pathToConAttrRating, SDFAttributeList schema) {
		MVRelationalTuple<M>[] objConList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(scan, pathToConList)).getAttributes();
		MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(scan, pathToNewList)).getAttributes();
		MVRelationalTuple<M>[] oldList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(scan, pathToOldList)).getAttributes();
		
		double[][] correlationMatrix = new double[newList.length][oldList.length];
		
		for(MVRelationalTuple<M> connectionEntry : objConList) {
			Object newRef = OrAttributeResolver.resolveTuple(connectionEntry, pathToConAttrNew);
			Object oldRef = OrAttributeResolver.resolveTuple(connectionEntry, pathToConAttrOld);
			Object conRating = OrAttributeResolver.resolveTuple(connectionEntry, pathToConAttrRating);
			
			int indexOfNewRef = OrAttributeResolver.indexOfAttribute((RelationalTuple<?>) OrAttributeResolver.resolveTuple(scan, pathToNewList), newRef);
			int indexOfOldRef = OrAttributeResolver.indexOfAttribute((RelationalTuple<?>) OrAttributeResolver.resolveTuple(scan, pathToOldList), oldRef);
			
			correlationMatrix[indexOfNewRef][indexOfOldRef] = Double.parseDouble(conRating.toString());
		}
		
		return correlationMatrix;
	}
	
	/**
	 * Returns a MVRelationalTuple with connections for a given correlation matrix.
	 */
	@SuppressWarnings("unchecked")
	public MVRelationalTuple<M>[] decodeMatrix(MVRelationalTuple<M>[] newList, MVRelationalTuple<M>[] oldList, double[][] correlationMatrix, SDFAttributeList connectionSchema, int indexOfConAttrOld, int indexOfConAttrNew, int indexOfConAttrRating) {
		ArrayList<MVRelationalTuple<M>> updatedConnections = new ArrayList<MVRelationalTuple<M>>();
		for(int i = 0; i < newList.length; i++) {
			for(int j = 0; j < oldList.length; j++) {
				if(correlationMatrix[i][j] != 0) {
					MVRelationalTuple<M> newTuple = new MVRelationalTuple<M>(connectionSchema);
					newTuple.addAttributeValue(indexOfConAttrNew, newList[i]);
					newTuple.addAttributeValue(indexOfConAttrOld, newList[j]);
					newTuple.addAttributeValue(indexOfConAttrRating, correlationMatrix[i][j]);
					updatedConnections.add(newTuple);
				}
			}
		}
		MVRelationalTuple<M>[] updatedConArray = (MVRelationalTuple<M>[]) updatedConnections.toArray();
		return updatedConArray;
	}

}
