/**
 * Interace for the implementation of a function for correcting
 * the state error covariance matrix
 */
package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

/**
 * @author dtwumasi
 *
 */
public interface ICorrectStateCovarianceFunction {

	//public double[][] correctStateCovariance(double[][] covarianceOld, double[][] gain, Object[] matrixes);
	public double[][] correctStateCovariance();

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(HashMap<Integer, Object> parameters);
		

	/**
	 * @return the parameters
	 */
	public HashMap<Integer, Object> getParameters();


	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(Integer key, Object Value);
	
	public int getFunctionID();

}
