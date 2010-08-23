/**
 * Interface for the implementation of a filter function,
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;

/**
 * @author dtwumasi
 *
 */
public abstract class AbstractMetaDataUpdateFunction {
	
	public AbstractMetaDataUpdateFunction() {
		
	}
	
	public AbstractMetaDataUpdateFunction(HashMap<Integer,Object> parameters ) {
		this.setParameters(parameters);
	}
	
	public AbstractMetaDataUpdateFunction(AbstractMetaDataUpdateFunction copy ) {
		this.setParameters(new HashMap<Integer, Object>(copy.getParameters()));	
	}
	
	public abstract AbstractMetaDataUpdateFunction clone();
		
	
	private HashMap<Integer, Object> parameters;
	
	/**
	 * this method executes the function
	 * 
	 * @return Object the result of the computation
	 */
	public abstract void compute(Connection connected);

	/**
	 * @param parameters the parameters needed for computation
	 */
	public void setParameters(HashMap<Integer, Object> parameters) { 
	this.parameters = parameters;
	}
	
	/**
	 * @return the parameters
	 */
	public HashMap<Integer, Object> getParameters() {
		
		return parameters;
	} 

	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(Integer key, Object value) {
		this.parameters.put(key, value);
	}

	

}
