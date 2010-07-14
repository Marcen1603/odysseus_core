package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;

public interface IConnectionContainer<L, R, W extends java.lang.Number> extends IMetaAttribute, IClone{
	
	public void setConnectionList(ConnectionList<L, R, W> list);
	public ConnectionList<L, R, W> getConnectionList();

}
