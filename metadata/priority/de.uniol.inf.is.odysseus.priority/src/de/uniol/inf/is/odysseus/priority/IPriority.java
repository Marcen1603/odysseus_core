package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.base.IClone;

public interface IPriority extends IClone{
	public byte getPriority();
	public void setPriority(byte priority);
}
