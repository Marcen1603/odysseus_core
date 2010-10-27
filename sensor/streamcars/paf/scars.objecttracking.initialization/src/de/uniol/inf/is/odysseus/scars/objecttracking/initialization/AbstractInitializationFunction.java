package de.uniol.inf.is.odysseus.scars.objecttracking.initialization;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;

/**
 * @author dtwumasi
 *
 */
public abstract class AbstractInitializationFunction<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer> {

	private HashMap<Enum, Object> parameters;

	public abstract MVRelationalTuple<M> compute(MVRelationalTuple<M> object, SchemaIndexPath newTupleIndexPath, SchemaIndexPath oldTupleIndexPath);
	
	/**
	 * @param parameters the parameters to set
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
	
	@Override
	public abstract AbstractInitializationFunction clone();

}
