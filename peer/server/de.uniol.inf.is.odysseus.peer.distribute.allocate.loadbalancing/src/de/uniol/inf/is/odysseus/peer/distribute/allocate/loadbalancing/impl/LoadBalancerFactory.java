package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.ILoadBalancer;

public class LoadBalancerFactory {

	private LoadBalancerFactory() {
		// do not instantiate this
	}

	public static ILoadBalancer create(String type) throws LoadBalancerFactoryException {

		switch (type.toLowerCase()) {

		case "mincpu":
			return new MinCPULoadBalancer();

		case "maxcpu":
			return new MaxCPULoadBalancer();

		case "minquery":
			return new MinQueryCountLoadBalancer();

		default:
			throw new LoadBalancerFactoryException("Load balancer type '" + type + "' not known");
		}
	}
}
