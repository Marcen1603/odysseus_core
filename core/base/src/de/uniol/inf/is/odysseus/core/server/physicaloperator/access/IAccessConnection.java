package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public interface IAccessConnection extends IClone {

	@Override
	IAccessConnection clone();
	
	public void open(IAccessConnectionListener caller) throws OpenFailedException;
	public void close(IAccessConnectionListener caller) throws IOException;
	public void reconnect();
	
	public String getUser();
	public String getPassword();
	
}
