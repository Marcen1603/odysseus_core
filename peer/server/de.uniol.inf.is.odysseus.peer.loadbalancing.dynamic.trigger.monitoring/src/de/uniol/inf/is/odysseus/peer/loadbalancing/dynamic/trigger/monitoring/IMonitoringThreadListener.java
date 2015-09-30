package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.trigger.monitoring;

public interface IMonitoringThreadListener {

	public void notifyLoadBalancingTriggered(double cpuUsage, double memUsage, double netUsage);
}
