/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.storage;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IAssociativeStorage<T extends Tuple<?>> {

    void set(Object[] path, int[] index, Object value);

    Object get(Object[] path, int[] index);

}
