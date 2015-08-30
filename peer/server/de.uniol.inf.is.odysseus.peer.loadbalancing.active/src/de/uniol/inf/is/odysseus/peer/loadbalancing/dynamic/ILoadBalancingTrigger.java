package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.ILoadBalancingTriggerListener;

public interface ILoadBalancingTrigger extends INamedInterface {
	public String getName();
	public void addListener(ILoadBalancingTriggerListener listener);
	public void removeListener(ILoadBalancingTriggerListener listener);
	public void start();
	public void setInactive();
}
