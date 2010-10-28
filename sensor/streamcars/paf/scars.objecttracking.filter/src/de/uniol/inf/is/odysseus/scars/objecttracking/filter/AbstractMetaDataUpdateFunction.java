/**
 * Interface for the implementation of a filter function,
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;

/**
 * @author dtwumasi
 *
 */
public abstract class AbstractMetaDataUpdateFunction<M extends IProbability> {
	
	public AbstractMetaDataUpdateFunction() {
		parameters = new HashMap<Enum, Object>();
	}
	
	public AbstractMetaDataUpdateFunction(HashMap<Enum,Object> parameters ) {
		this.setParameters(parameters);
	}
	
	public AbstractMetaDataUpdateFunction(AbstractMetaDataUpdateFunction<M> copy ) {
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
	}
	
	@Override
	public abstract AbstractMetaDataUpdateFunction<M> clone();
		
	
	private HashMap<Enum, Object> parameters;
	
	/**
	 * this method executes the function
	 * 
	 * @return Object the result of the computation
	 */
	public abstract void compute(IConnection connected, MVRelationalTuple<M> tuple, HashMap<Enum, Object> parameters);

	/**
	 * @param parameters the parameters needed for computation
	 */
	public void setParameters(HashMap<Enum, Object> parameters) { 
	this.parameters = parameters;
	}
	
	/**
	 * @return the parameters
	 */
	public HashMap<Enum, Object> getParameters() {
		
		return parameters;
	} 

	/**
	 * @param parameters the parameters to set
	 */
	public void addParameter(Enum key, Object value) {
		this.parameters.put(key, value);
	}

	

}
