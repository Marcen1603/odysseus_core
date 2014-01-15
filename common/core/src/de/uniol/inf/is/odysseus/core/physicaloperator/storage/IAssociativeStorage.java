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

    void set(Object[] path, int[] index, double value);

    double get(Object[] path, int[] index);

    double[] getLine(Object[] path, int[] index);

}
