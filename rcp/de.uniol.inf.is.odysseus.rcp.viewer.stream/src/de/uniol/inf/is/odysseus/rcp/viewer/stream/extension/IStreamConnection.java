package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;



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
	public void notifyListenersPunctuation( PointInTime point, int port );
	Collection<ISource<? extends In>> getSources();
	
}
