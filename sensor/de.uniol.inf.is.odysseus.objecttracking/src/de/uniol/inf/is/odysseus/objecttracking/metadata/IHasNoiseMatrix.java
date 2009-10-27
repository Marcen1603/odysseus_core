package de.uniol.inf.is.odysseus.objecttracking.metadata;

/**
 * This interface will be implemented by
 * probability prediction functions, since
 * they have to use a noise matrix for calculating
 * the new covariance matrix.
 * 
 * @author Andre Bolles
 *
 */
public interface IHasNoiseMatrix{

	public void setNoiseMatrix(double[][] noiseMatrix);
}
