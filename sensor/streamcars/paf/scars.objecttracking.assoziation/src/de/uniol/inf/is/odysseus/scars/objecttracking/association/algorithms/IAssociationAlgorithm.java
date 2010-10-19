package de.uniol.inf.is.odysseus.scars.objecttracking.association.algorithms;

import java.util.HashMap;

/**
 * IAssociationAlgorithm represents a algorithm for the evaluation of connections.
 * @author VJ
 *
 */
public interface IAssociationAlgorithm {

	/**
	 * Since each algorithm can have special parameters, they are defined in the query and afterwards are stored within a HashMap.
	 * initAlgorithmParameter takes the parameters out of the HashMap and puts them into class variables.
	 * @param parameterMap The HashMap that contains the parameters defined in the query.
	 */
	public void initAlgorithmParameter(HashMap<String, String> parameterMap);

	/**
	 * Evaluate returns a new rating for a specific connection.
	 * @param scannedObjCovariance Covariance matrix of the scanned object.
	 * @param scannedObjMesurementValues Mesurement values of the scanned object.
	 * @param predictedObjCovariance Covariance matrix of the predicted object.
	 * @param predictedObjMesurementValues Mesurement values of the predicted object.
	 * @param currentRating The current rating of this connection.
	 * @return New rating of this connection.
	 */
	public double evaluate(double[][] scannedObjCovariance, double[] scannedObjMesurementValues, double[][] predictedObjCovariance, double[] predictedObjMesurementValues, double currentRating);

}
