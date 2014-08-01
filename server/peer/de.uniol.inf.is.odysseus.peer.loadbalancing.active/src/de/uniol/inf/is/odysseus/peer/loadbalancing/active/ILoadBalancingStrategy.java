package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

/**
 * The {@link ILoadBalancingStrategy} interface provides methods to start monitoring the current load and enabling the load balancing 
 * as well as to stop monitoring the current load and disabling the load balancing. <br />
 * Additionally, an {@link ILoadBalancingAllocator} and an {@link ILoadBalancingCommunicator} are necessary for the load balancing process. 
 * Both can be set within an {@link ILoadBalancingStrategy}.
 * @author Michael Brand
 */
public interface ILoadBalancingStrategy extends INamedInterface {
	
	/**
	 * A {@link LoadBalancingException} indicates, that an error occurred either in 
	 * {@link ILoadBalancingStrategy#startMonitoring()} or in 
	 * {@link ILoadBalancingStrategy#stopMonitoring()}.
	 * @author Michael Brand
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
	
	/**
	 * Sets the allocator to be used for handing over {@link ILogicalQueryPart}s.
	 * @param allocator An implementation of {@link ILoadBalancingAllocator}.
	 */
	public void setAllocator(ILoadBalancingAllocator allocator);
	
	/**
	 * Sets the communicator to be used for load balancing coordination.
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