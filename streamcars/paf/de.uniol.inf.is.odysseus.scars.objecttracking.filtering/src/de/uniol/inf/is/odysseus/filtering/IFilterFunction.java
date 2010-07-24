/**
 * Interface for the implementation of a filter function,
 *
 */
package de.uniol.inf.is.odysseus.filtering;

import java.util.HashMap;

/**
 * @author dtwumasi
 *
 */
public interface IFilterFunction {
	
	/**
	 * this method executes the function
	 * 
	 * @return Object the result of the computation
	 */
	public Object compute();

	/**
	 * @param parameters the parameters needed for computation
	 */
	public void setParameters(HashMap<Integer, Object> parameters);


	/**
	 * @return the parameters
	 */
	public HashMap<Integer, Object> getParameters();


	/**
	 * @param parameters a single parameter to be added
	 */
	public void addParameter(Integer key, Object Value);

	public int getFunctionID();

}
