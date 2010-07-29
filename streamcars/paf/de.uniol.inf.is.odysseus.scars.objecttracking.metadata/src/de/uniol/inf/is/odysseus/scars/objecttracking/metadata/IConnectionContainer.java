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
 */
public interface IConnectionContainer extends IMetaAttribute, IClone{

	public void setConnectionList(ConnectionList list);
	public ConnectionList getConnectionList();

}
