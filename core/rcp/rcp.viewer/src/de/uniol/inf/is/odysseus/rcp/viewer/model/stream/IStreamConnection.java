package de.uniol.inf.is.odysseus.rcp.viewer.model.stream;

import java.util.Collection;

import de.uniol.inf.is.odysseus.physicaloperator.ISource;


public interface IStreamConnection<In> {

	public void connect();
	public void disconnect();
	public boolean isConnected();
	
	public void disable();
	public void enable();
	public boolean isEnabled();
	
	public void addStreamElementListener( IStreamElementListener<In> listener );
	public void removeStreamElementListener( IStreamElementListener<In> listener );
	public void notifyListeners( In element, int port );
	
	public Collection<ISource<? extends In>> getSources();
	
}
