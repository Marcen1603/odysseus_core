package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This interface is used to carry a key for a prediction function
 * in tuple. A tuple will not carry the prediction function itself,
 * but key that can be used to find the corresponding prediction
 * function or their transformaed equations in the operators of
 * a query plan.
 * 
 * @author André Bolles
 *
 * @param <K> The type of the key. Usually IPredicate will be used.
 */
public interface IPredictionFunctionKey<K> extends IMetaAttribute, IClone{

	public void setPredictionFunctionKey(K key);
	public K getPredictionFunctionKey();
}
