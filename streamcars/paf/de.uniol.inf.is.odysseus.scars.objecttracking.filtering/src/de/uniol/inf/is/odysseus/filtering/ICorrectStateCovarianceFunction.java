/**
 * Interace for the implementation of a function for correcting
 * the state error covariance matrix
 */
package de.uniol.inf.is.odysseus.filtering;


import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

/**
 * @author dtwumasi
 *
 */
public interface ICorrectStateCovarianceFunction<M extends IProbability> {

	public double[][] correctStateCovariance(double[][] covarianceOld, double[][] gain, Object[] matrixes);

}
