package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public interface IAccessConnectionHandler<T> extends IClone {

	@Override
	IAccessConnectionHandler<T> clone();
	
	public void open(IAccessConnectionListener<T> caller) throws OpenFailedException;
	public void close(IAccessConnectionListener<T> caller) throws IOException;
	public void reconnect();
	
	public String getUser();
	public String getPassword();
	
}
