package de.uniol.inf.is.odysseus.net.discovery;

public interface INodeDiscoverer {

	public void start();
	public void stop();
	public boolean isStarted();
	public boolean isStopped();
	
}
