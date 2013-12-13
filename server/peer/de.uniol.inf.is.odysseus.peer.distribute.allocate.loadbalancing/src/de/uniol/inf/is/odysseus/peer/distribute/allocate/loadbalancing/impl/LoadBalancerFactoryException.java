package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

public class LoadBalancerFactoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoadBalancerFactoryException() {
		super();
	}

	public LoadBalancerFactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoadBalancerFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoadBalancerFactoryException(String message) {
		super(message);
	}

	public LoadBalancerFactoryException(Throwable cause) {
		super(cause);
	}
}
