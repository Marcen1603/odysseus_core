/**
 * 
 */
package de.uniol.inf.is.odysseus.core.physicaloperator.storage;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IAssociativeStorage<T extends Tuple<?>> {

    void set(Object[] path, int[] index, Double value);

    Double get(Object[] path, int[] index);

    Double[] getLine(Object[] path, int[] index);

}
