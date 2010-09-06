/**
 * Interface for the implementation of a filter function,
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;

/**
 * @author dtwumasi
 * 
 */
public abstract class AbstractMetaDataCreationFunction<M extends IGain & IProbability> {

	private HashMap<Enum, Object> parameters;

	public AbstractMetaDataCreationFunction() {
		parameters = new HashMap<Enum, Object>();
	}

	public AbstractMetaDataCreationFunction(HashMap<Enum, Object> parameters) {
		this.setParameters(parameters);
	}

	public AbstractMetaDataCreationFunction(AbstractMetaDataCreationFunction<M> copy) {
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));
	}

	public abstract AbstractMetaDataCreationFunction<M> clone();

	/**
	 * this method executes the function
	 * 
	 * @return Object the result of the computation
	 */
	public abstract void compute(Connection connected, MVRelationalTuple<M> completeTuple, HashMap<Enum, Object> hashMap);

	/**
	 * @param parameters
	 *            the parameters needed for computation
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
	 * @param parameters
	 *            the parameters to set
	 */
	public void addParameter(Enum key, Object value) {
		this.parameters.put(key, value);
	}

}
