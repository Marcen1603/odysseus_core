package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;

/**
 * Interface to implement custom Load Balancing Strategy for dynamic Load Balancing
 * @author Michael Brand, Carsten Cordes
 *
 */
public interface ILoadBalancingStrategy extends INamedInterface {
	
	/***
	 * Load Balancing Exception
	 * @author Carsten Cordes
	 */
	public class LoadBalancingException extends Exception {

		/**
		 * The version of this class for serialization.
		 */
		private static final long serialVersionUID = 5089656276107275911L;

		/**
		 * @see Exception#Exception()
		 */
		public LoadBalancingException() {
			super();
		}

		/**
		 * @see Exception#Exception(String, Throwable)
		 */
		public LoadBalancingException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @see Exception#Exception(String)
		 */
		public LoadBalancingException(String message) {
			super(message);	
		}

		/**
		 * @see Exception#Exception(Throwable)
		 */
		public LoadBalancingException(Throwable cause) {
			super(cause);
		}
		
	}
	
	/***
	 * Method that can be used to force a load balancing, e.g. for testing purpose
	 * @throws LoadBalancingException
	 */
	public void forceLoadBalancing() throws LoadBalancingException;
	
	/**
	 * Sets the allocator to be used for handing over {@link ILogicalQueryPart}s.
	 * (do not use this)
	 * @param allocator An implementation of {@link ILoadBalancingAllocator}.
	 */
	public void setAllocator(ILoadBalancingAllocator allocator);
	
	/**
	 * Sets the communicator to be used for load balancing coordination.
	 * (do not use this, use communicator choser instead)
	 * @param communicator An implementation of {@link ILoadBalancingCommunicator}.
	 */
	public void setCommunicator(ILoadBalancingCommunicator communicator);

	/**
	 * Starts monitoring the current load and enables the load balancing.
	 * @throws LoadBalancingException if any error occurs. This may happen, if either the allocator or the communicator is not set.
	 */
	public void startMonitoring() throws LoadBalancingException;
	
	/**
	 * Stops monitoring the current load and disables the load balancing.
	 */
	public void stopMonitoring();

}