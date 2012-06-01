package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.util.Map;

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
	
	public String getName();
	public IAccessConnectionHandler<T> getInstance(Map<String,String> options);
	
}
