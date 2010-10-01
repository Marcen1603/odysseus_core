package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IPriority extends IMetaAttribute{
	public byte getPriority();
	public void setPriority(byte priority);
}
