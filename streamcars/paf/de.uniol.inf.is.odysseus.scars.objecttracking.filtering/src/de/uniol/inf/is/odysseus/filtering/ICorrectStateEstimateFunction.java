/**
 * Interface for the implementation of a state estimate correction function
 */
package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;



/**
 * @author dtwumasi
 *
 */
public interface ICorrectStateEstimateFunction {

	//public double[] correctStateEstimate(double[] measurementOld, double[] measurementNew, double[][] Gain, Object matrixes);

	public double[] correctStateEstimate();
	
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
