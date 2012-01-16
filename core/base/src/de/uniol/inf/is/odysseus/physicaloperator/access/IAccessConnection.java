package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

public interface IAccessConnection extends IClone {

	IAccessConnection clone();
	
	public void open(IAccessConnectionListener caller) throws OpenFailedException;
	public void close(IAccessConnectionListener caller) throws IOException;
	public void reconnect();
	
}
