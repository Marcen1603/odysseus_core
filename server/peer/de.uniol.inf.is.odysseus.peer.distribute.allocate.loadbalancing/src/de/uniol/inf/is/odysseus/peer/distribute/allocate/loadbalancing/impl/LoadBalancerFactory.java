package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.ILoadBalancer;

public class LoadBalancerFactory {

	private LoadBalancerFactory() {
		// do not instantiate this
	}

	public static ILoadBalancer create(String type) throws LoadBalancerFactoryException {
		
		switch (type) {
		
		case "min":
			return new MinLoadBalancer();
			
		case "max":
			return new MaxLoadBalancer();
			
		case "avg":
			return new AvgLoadBalancer();
		}

		throw new LoadBalancerFactoryException("Load balancer type '" + type + "' not known");
	}
}
