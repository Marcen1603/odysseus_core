package de.uniol.inf.is.odysseus.net;

import java.util.Collection;

import com.google.common.base.Optional;

public interface IOdysseusNetStartupManager {

	public void start() throws OdysseusNetException ;
	public void stop();
	
	public boolean isStarted();
	public IOdysseusNode getLocalOdysseusNode() throws OdysseusNetException;
	
	public void addListener( IOdysseusNetStartupListener listener);
	public void removeListener( IOdysseusNetStartupListener listener);
	
	public Collection<IOdysseusNetComponent> getComponents();
	public Optional<OdysseusNetComponentStatus> getComponentStatus(IOdysseusNetComponent component);
}
