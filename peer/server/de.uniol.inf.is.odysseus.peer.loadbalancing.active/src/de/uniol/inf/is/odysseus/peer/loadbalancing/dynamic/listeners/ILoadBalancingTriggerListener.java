package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners;

public interface ILoadBalancingTriggerListener {

	public void triggerLoadBalancing(double cpuUsage, double memUsage, double netUsage);
}
