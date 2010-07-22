package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;

/**
 * The IConnectionContainer contains an connection list where the rated connections between
 * objects are stored. It serves as a meta data so that in the objecttracking process
 * you could access the connections by object.getMetadata().get/setConnectionList().
 *
 * @author Volker Janz
 *
 * @param <L> Datatype of the left object
 * @param <R> Datatype of the right object
 * @param <W> Datatype of the rating - has to extend java.lang.Number (Double, Integer, ...)
 */
public interface IConnectionContainer<L, R, W extends java.lang.Number> extends IMetaAttribute, IClone{

	public void setConnectionList(ConnectionList<L, R, W> list);
	public ConnectionList<L, R, W> getConnectionList();

}
