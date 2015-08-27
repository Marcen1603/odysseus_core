package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces;

public interface IMonitoringThread {
	public void addListener(IMonitoringThreadListener listener);
	public void removeListener(IMonitoringThreadListener listener);
	public void start();
	public void setInactive();
}
