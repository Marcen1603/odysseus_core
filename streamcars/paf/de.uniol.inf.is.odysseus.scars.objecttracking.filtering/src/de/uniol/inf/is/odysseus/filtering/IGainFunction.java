/**
 * Interface for the implementation of a gain function,
 * that computes the gain for fusing old and new data
 */
package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

/**
 * @author dtwumasi
 *
 */
public interface IGainFunction {

	//public double[][] computeGain(double[][] covarianceOld, double[][] covarianceNew, Object[] matrixes);
//	public HashMap<String, Object> parameters;
	
	public double[][] computeGain();

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
