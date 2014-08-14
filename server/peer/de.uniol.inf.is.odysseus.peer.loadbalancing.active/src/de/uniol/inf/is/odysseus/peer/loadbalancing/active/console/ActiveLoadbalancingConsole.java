package de.uniol.inf.is.odysseus.peer.loadbalancing.active.console;

import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.ILoadBalancingCommunicator;

/**
 * Console with debug commands for active LoadBalancing.
 * @author Carsten Cordes
 *
 */
public class ActiveLoadbalancingConsole implements CommandProvider {

	/**
	 * Logger.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ActiveLoadbalancingConsole.class);

	/**
	 * Session Variable
	 */
	private static ISession activeSession;
	/**
	 * Used to store OSGi Services.
	 */
	private static IP2PDictionary p2pDictionary;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IServerExecutor executor;
	private static Collection<ILoadBalancingStrategy> loadBalancingStrategies = Lists.newArrayList();
	private static Collection<ILoadBalancingAllocator> loadBalancingAllocators = Lists.newArrayList();
	private static Optional<ILoadBalancingCommunicator> loadBalancingCommunicator;

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindLoadBalancingStrategy(ILoadBalancingStrategy strategy) {
		
		ActiveLoadbalancingConsole.loadBalancingStrategies.add(strategy);
		
	}

	// called by OSGi-DS
	public static void unbindLoadBalancingStrategy(ILoadBalancingStrategy strategy) {
		
		if(strategy != null) {
		
			ActiveLoadbalancingConsole.loadBalancingStrategies.remove(strategy);
			
		}
		
	}
	
	// called by OSGi-DS
	public static void bindLoadBalancingAllocator(ILoadBalancingAllocator allocator) {
		
		ActiveLoadbalancingConsole.loadBalancingAllocators.add(allocator);
		
	}

	// called by OSGi-DS
	public static void unbindLoadBalancingAllocator(ILoadBalancingAllocator allocator) {
		
		if(allocator != null) {
		
			ActiveLoadbalancingConsole.loadBalancingAllocators.remove(allocator);
			
		}
		
	}
	
	// called by OSGi-DS
	public static void bindLoadBalancingCommunicator(ILoadBalancingCommunicator communicator) {
		
		ActiveLoadbalancingConsole.loadBalancingCommunicator = Optional.of(communicator);
		
	}

	// called by OSGi-DS
	public static void unbindLoadBalancingCommunicator(ILoadBalancingCommunicator communicator) {
		
		if(communicator != null && communicator.equals(ActiveLoadbalancingConsole.loadBalancingCommunicator)) {
		
			ActiveLoadbalancingConsole.loadBalancingCommunicator = Optional.absent();
			
		}
		
	}

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Active Loadbalancing console activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Active Loadbalancing console deactivated");
	}

	@Override
	/**
	 * Outputs a simple Help.
	 */
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Active Loadbalancing commands---\n");
		sb.append("    lsParts		              		- Lists all queryParts installed on peer with ids\n");
		sb.append("    lsLBStrategies	              		- Lists all available load balancing strategies\n");
		sb.append("    lsLBAllocators	              		- Lists all available load balancing allocators\n");
		sb.append("    initLB <strategyname> <allocatorname>	- Initiate Loadbalancing with load balancing strategy <strategyname> and load balancing allocator <allocatorname>\n");
		sb.append("    cpJxtaSender <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Sender");
		sb.append("    cpJxtaReceiver <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Receiver");
		return sb.toString();
	}
	
	/**
	 * Initializes the load balancing process for the peer by calling a given {@link ILoadBalancingStrategy} 
	 * with a given {@link ILoadBalancingAllocator}.
	 * @param ci The {@link CommandInterpreter} instance.
	 */
	public void _initLB(CommandInterpreter ci) {
		
		Preconditions.checkNotNull(ci, "Command interpreter must be not null!");
		
		final String ERROR_USAGE = "usage: initLB <peername> <strategyname> <allocatorname>";
		final String ERROR_STRATEGY = "No load balancing strategy found with the name ";
		final String ERROR_ALLOCATOR = "No load balancing allocator found with the name ";
		final String ERROR_COMMUNICATOR = "No load balancing communicator available";
		
		String strategyName = ci.nextArgument();
		if(Strings.isNullOrEmpty(strategyName)) {
			
			System.out.println(ERROR_USAGE);
			return;
			
		}
		
		Optional<ILoadBalancingStrategy> optStrategy = ActiveLoadbalancingConsole.determineStrategy(strategyName);
		if(!optStrategy.isPresent()) {
			
			System.out.println(ERROR_STRATEGY + strategyName);
			return;
			
		}
		ILoadBalancingStrategy strategy = optStrategy.get();
		
		String allocatorName = ci.nextArgument();
		if(Strings.isNullOrEmpty(allocatorName)) {
			
			System.out.println(ERROR_USAGE);
			return;
			
		}
		
		Optional<ILoadBalancingAllocator> optAllocator = ActiveLoadbalancingConsole.determineAllocator(allocatorName);
		if(!optAllocator.isPresent()) {
			
			System.out.println(ERROR_ALLOCATOR + allocatorName);
			return;
			
		}
		ILoadBalancingAllocator allocator = optAllocator.get();
		
		if(!ActiveLoadbalancingConsole.loadBalancingCommunicator.isPresent()) {
			
			System.out.println(ERROR_COMMUNICATOR);
			return;
			
		}
		ILoadBalancingCommunicator communicator = ActiveLoadbalancingConsole.loadBalancingCommunicator.get();
		
		strategy.setAllocator(allocator);
		strategy.setCommunicator(communicator);
		
		try {
			
			strategy.startMonitoring();
			
		} catch(LoadBalancingException e) {
			
			System.out.println("An error occured: " + e.getMessage());
			
		}
		
	}

	/**
	 * Lists installed QueryParts (logical)
	 * @param ci
	 */
	public void _lsParts(CommandInterpreter ci) {

		System.out.println("Query Parts on current Peer:");
		for (int queryID : executor.getLogicalQueryIds(getActiveSession())) {
			System.out.println("["
					+ queryID
					+ "] "
					+ executor.getLogicalQueryById(queryID, getActiveSession())
							.getQueryText());
		}
	}
	
	/**
	 * Lists all available {@link ILoadBalancingStrategy}s bound via OSGI-DS.
	 * @param ci The {@link CommandInterpreter} instance.
	 */
	public void _lsLBStrategies(CommandInterpreter ci) {

		System.out.println("Available load balancing strategies:");
		for(ILoadBalancingStrategy strategy : ActiveLoadbalancingConsole.loadBalancingStrategies) {
			
			System.out.println(strategy.getName());
			
		}
		
	}
	
	/**
	 * Lists all available {@link ILoadBalancingAllocator}s bound via OSGI-DS.
	 * @param ci The {@link CommandInterpreter} instance.
	 */
	public void _lsLBAllocators(CommandInterpreter ci) {

		System.out.println("Available load balancing allocators:");
		for(ILoadBalancingAllocator allocator : ActiveLoadbalancingConsole.loadBalancingAllocators) {
			
			System.out.println(allocator.getName());
			
		}
		
	}
	
	/**
	 * Determines the {@link ILoadBalancingStrategy} by name.
	 * @param strategyName The name of the strategy.
	 * @return An {@link ILoadBalancingStrategy}, if there is one bound with <code>strategyName</code> as name.
	 */
	private static Optional<ILoadBalancingStrategy> determineStrategy(
			String strategyName) {
		
		Preconditions.checkNotNull(strategyName, "The name of the load balancing strategy must be not null!");
		
		for(ILoadBalancingStrategy strategy : ActiveLoadbalancingConsole.loadBalancingStrategies) {
			
			if(strategy.getName().equals(strategyName)) {
				
				return Optional.of(strategy);
				
			}
			
		}
		
		return Optional.absent();		
		
	}
	
	/**
	 * Determines the {@link ILoadBalancingAllocator} by name.
	 * @param allocatorName The name of the allocator.
	 * @return An {@link ILoadBalancingAllocator}, if there is one bound with <code>allocatorName</code> as name.
	 */
	private static Optional<ILoadBalancingAllocator> determineAllocator(
			String allocatorName) {
		
		Preconditions.checkNotNull(allocatorName, "The name of the load balancing allocator must be not null!");
		
		for(ILoadBalancingAllocator allocator : ActiveLoadbalancingConsole.loadBalancingAllocators) {
			
			if(allocator.getName().equals(allocatorName)) {
				
				return Optional.of(allocator);
				
			}
			
		}
		
		return Optional.absent();		
		
	}

	/**
	 * Returns currently active Session.
	 * @return
	 */
	private static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}

		return activeSession;
	}

}
