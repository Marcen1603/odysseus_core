package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

public interface IMonitoringThreadListener {

	public void triggerLoadBalancing(double cpuUsage, double memUsage, double netUsage);
}
