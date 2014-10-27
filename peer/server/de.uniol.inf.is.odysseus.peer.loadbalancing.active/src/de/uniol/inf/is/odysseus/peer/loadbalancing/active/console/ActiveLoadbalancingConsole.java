package de.uniol.inf.is.odysseus.peer.loadbalancing.active.console;

import java.io.Serializable;
import java.util.Collection;

import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateReceiver;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateSender;

/**
 * Console with debug commands for active LoadBalancing.
 * 
 * @author Carsten Cordes
 * 
 */
public class ActiveLoadbalancingConsole implements CommandProvider {

	private static String communicator = "ParallelTrack";

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
	private static Collection<ILoadBalancingStrategy> loadBalancingStrategies = Lists
			.newArrayList();
	private static Collection<ILoadBalancingAllocator> loadBalancingAllocators = Lists
			.newArrayList();
	private static Collection<ILoadBalancingCommunicator> loadBalancingCommunicators = Lists
			.newArrayList();

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
	public static void unbindLoadBalancingStrategy(
			ILoadBalancingStrategy strategy) {

		if (strategy != null) {

			ActiveLoadbalancingConsole.loadBalancingStrategies.remove(strategy);

		}

	}

	// called by OSGi-DS
	public static void bindLoadBalancingAllocator(
			ILoadBalancingAllocator allocator) {

		ActiveLoadbalancingConsole.loadBalancingAllocators.add(allocator);

	}

	// called by OSGi-DS
	public static void unbindLoadBalancingAllocator(
			ILoadBalancingAllocator allocator) {

		if (allocator != null) {

			ActiveLoadbalancingConsole.loadBalancingAllocators
					.remove(allocator);

		}

	}

	public static void _insertBuffer(CommandInterpreter ci) {

		final String USAGE_ERROR = "Usage: insertBuffer <pipeID>";

		String pipe = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipe)) {
			System.out.println(USAGE_ERROR);
			return;
		}
		MovingStateHelper.insertBuffer(pipe);
	}

	// called by OSGi-DS
	public static void bindLoadBalancingCommunicator(
			ILoadBalancingCommunicator communicator) {
		ActiveLoadbalancingConsole.loadBalancingCommunicators.add(communicator);

	}

	// called by OSGi-DS
	public static void unbindLoadBalancingCommunicator(
			ILoadBalancingCommunicator communicator) {

		if (communicator != null) {

			ActiveLoadbalancingConsole.loadBalancingCommunicators
					.remove(communicator);

		}

	}

	/**
	 * The currently running load balancing strategy, if there is one.
	 */
	private Optional<ILoadBalancingStrategy> mRunningStrategy = Optional
			.absent();

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
		sb.append("    lsLBCommunicators                    - Lists all available load balancing communicators\n");
		sb.append("    initLB <strategyname> <allocatorname>	- Initiate Loadbalancing with load balancing strategy <strategyname> and load balancing allocator <allocatorname>\n");
		sb.append("    stopLB                            - Stops the Load Balancing\n");
		sb.append("    cpJxtaSender <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Sender\n");
		sb.append("    cpJxtaReceiver <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Receiver\n");
		sb.append("    setLBCommunicator <communicatorName>                 - Sets LoadBalancing Communicator to use\n");
		sb.append("    insertBuffer <pipeID>                                - Inserts Buffer before Sender with pipeID\n");
		sb.append("    installStateSender <peerID>                          - Installs new MovingStateSender to PeerID\n");
		sb.append("    installStateReceiver <peerID> <pipeID>               - Installs new MovingStateReceiver with peer and pipeID\n");
		sb.append("    sendData <pipeID>                                    - Sends Data to MovingStateSender with pipeID\n");
		sb.append("    testLog                                              - Outputs different log levels.\n");
		sb.append("    testSendState <pipeID> <LocalQueryId>              - Sends state of first StatefulPO in Query to pipe");
		sb.append("    testInjectState <pipeID> <LocalQueryId>              - Injects received state from pipe to first statefol Operator in query.");
		return sb.toString();
	}

	/**
	 * Initializes the load balancing process for the peer by calling a given
	 * {@link ILoadBalancingStrategy} with a given
	 * {@link ILoadBalancingAllocator}.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _initLB(CommandInterpreter ci) {

		Preconditions.checkNotNull(ci, "Command interpreter must be not null!");

		final String ERROR_ALREADY_RUNNING = "A load balancing is already running!";
		final String ERROR_USAGE = "usage: initLB <peername> <strategyname> <allocatorname>";
		final String ERROR_STRATEGY = "No load balancing strategy found with the name ";
		final String ERROR_ALLOCATOR = "No load balancing allocator found with the name ";
		final String ERROR_COMMUNICATOR = "No load balancing communicator found with name ";

		if (this.mRunningStrategy.isPresent()) {

			System.out.println(ERROR_ALREADY_RUNNING);
			return;

		}

		String strategyName = ci.nextArgument();
		if (Strings.isNullOrEmpty(strategyName)) {

			System.out.println(ERROR_USAGE);
			return;

		}

		Optional<ILoadBalancingStrategy> optStrategy = ActiveLoadbalancingConsole
				.determineStrategy(strategyName);
		if (!optStrategy.isPresent()) {

			System.out.println(ERROR_STRATEGY + strategyName);
			return;

		}
		ILoadBalancingStrategy strategy = optStrategy.get();

		String allocatorName = ci.nextArgument();
		if (Strings.isNullOrEmpty(allocatorName)) {

			System.out.println(ERROR_USAGE);
			return;

		}

		Optional<ILoadBalancingAllocator> optAllocator = ActiveLoadbalancingConsole
				.determineAllocator(allocatorName);
		if (!optAllocator.isPresent()) {

			System.out.println(ERROR_ALLOCATOR + allocatorName);
			return;

		}
		ILoadBalancingAllocator allocator = optAllocator.get();

		Optional<ILoadBalancingCommunicator> optCommunicator = ActiveLoadbalancingConsole
				.determineCommunicator(communicator);
		if (!optCommunicator.isPresent()) {

			System.out.println(ERROR_COMMUNICATOR + allocatorName);
			return;

		}

		ILoadBalancingCommunicator communicator = optCommunicator.get();

		strategy.setAllocator(allocator);
		strategy.setCommunicator(communicator);

		try {

			strategy.startMonitoring();
			this.mRunningStrategy = Optional.of(strategy);

		} catch (LoadBalancingException e) {

			System.out.println("An error occured: " + e.getMessage());

		}

	}

	public void _setLBCommunicator(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: setLBCommunicator <communicatorName>";
		final String ERROR_NOTFOUND = "Error: No communicator found with name ";

		String communicatorName = ci.nextArgument();
		if (Strings.isNullOrEmpty(communicatorName)) {

			System.out.println(ERROR_USAGE);
			return;
		}
		Optional<ILoadBalancingCommunicator> optCommunicator = determineCommunicator(communicatorName);
		if (optCommunicator.isPresent()) {
			communicator = optCommunicator.get().getName();
		} else {
			System.out.println(ERROR_NOTFOUND + communicatorName);
			System.out.println("Available Communicators are: ");
			for (ILoadBalancingCommunicator communicator : loadBalancingCommunicators) {
				System.out.println(communicator.getName());
			}
		}

	}

	public void _stopLB(CommandInterpreter ci) {

		final String ERROR_NOT_RUNNING = "No load balancing running!";

		if (!this.mRunningStrategy.isPresent()) {

			System.out.println(ERROR_NOT_RUNNING);
			return;

		}

		this.mRunningStrategy.get().stopMonitoring();
		this.mRunningStrategy = Optional.absent();

	}

	public void _installStateSender(CommandInterpreter ci) {

		final String ERROR_USAGE = "Usage: installStateSender <peerName>";
		final String ERROR_NOTFOUND = "Error: No peer found with name ";

		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {

			System.out.println(ERROR_USAGE);
			return;
		}

		Collection<PeerID> peerIDs = p2pDictionary.getRemotePeerIDs();
		for (PeerID peer : peerIDs) {
			if (p2pDictionary.getRemotePeerName(peer).equals(peerName)) {
				String newPipe = MovingStateManager.getInstance().addSender(peer.toString());
				System.out.println("Pipe installed:");
				System.out.println(newPipe);
				return;
			}
		}
		System.out.println(ERROR_NOTFOUND + peerName);
	}
	
	public void _testSendState(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: testSendStatus <pipeID> <LocalQueryId>";
		final String ERROR_LOCALOP = "ERROR: Local Query does not contain stateful Operator.";
		final String ERROR_NOTFOUND = "ERROR: No Sender found for pipe:";
		
		String pipeId = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeId)) {

			System.out.println(ERROR_USAGE);
			return;
		}
		
		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			System.out.println(ERROR_USAGE);
			return;
		}
		
		int localQueryId;
		
		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		}
		catch(NumberFormatException e) {
			System.out.println(ERROR_USAGE);
			return;
		}
		
		IStatefulPO statefulOp=null;
		
		IPhysicalQuery localQuery = executor.getExecutionPlan().getQueryById(localQueryId);
		for(IPhysicalOperator operator : localQuery.getAllOperators()) {
			if(operator instanceof IStatefulPO) {
				statefulOp = (IStatefulPO)operator;
				break;
			}
		}
		
		if(statefulOp==null) {
			System.out.println(ERROR_LOCALOP);
			return;
		}
		Serializable state = statefulOp.getState();
		
		MovingStateSender sender = MovingStateManager.getInstance().getSender(pipeId);
		if(sender==null) {
			System.out.println(ERROR_NOTFOUND + pipeId);
			return;
		}
		try {
			sender.sendData(state);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e) {
			System.out.println("ERROR:" + e.getMessage());
			return;
		}
		System.out.println("Data sent.");
		
	}
	
	public void _installStateReceiver(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: installStateReceiver <peerName> <pipeID>";
		final String ERROR_NOTFOUND = "Error: No peer found with name ";

		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {

			System.out.println(ERROR_USAGE);
			return;
		}
		
		String pipeID = ci.nextArgument();
		if(Strings.isNullOrEmpty(pipeID)) {
			System.out.println(ERROR_USAGE);
			return;
		}

		Collection<PeerID> peerIDs = p2pDictionary.getRemotePeerIDs();
		for (PeerID peer : peerIDs) {
			if (p2pDictionary.getRemotePeerName(peer).equals(peerName)) {
				MovingStateManager.getInstance().addReceiver(peer.toString(), pipeID);
				System.out.println("Receiver installed");
				return;
			}
		}
		System.out.println(ERROR_NOTFOUND + peerName);
	}
	
	public void _sendData(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: sendData <pipeID> <message>";
		final String ERROR_NOTFOUND = "Error: No sender found with pipe ";
		String pipeID = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeID)) {

			System.out.println(ERROR_USAGE);
			return;
		}
		
		String nextWord = ci.nextArgument();
		if (Strings.isNullOrEmpty(nextWord)) {

			System.out.println(ERROR_USAGE);
			return;
		}
		StringBuilder sb = new StringBuilder();
		while(nextWord!=null) {
			sb.append(nextWord);
			sb.append(' ');
			nextWord = ci.nextArgument();
		}
		String message = sb.toString();

		MovingStateSender sender = MovingStateManager.getInstance().getSender(pipeID);
		if(sender==null) {
			System.out.println(ERROR_NOTFOUND + pipeID);
			return;
		}
		try {
			sender.sendData(message);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e) {
			System.out.println("ERROR:" + e.getMessage());
			return;
		}
		System.out.println("Sent: " + message);
	}
	
	public void _testInjectState(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: testInjectState <pipeID> <LocalQueryId>";
		final String ERROR_LOCALOP = "ERROR: Local Query does not contain stateful Operator.";
		final String ERROR_NOTFOUND = "ERROR: No Sender found for pipe:";
		final String ERROR_NOTHING_RECEIVED ="ERROR: No status received. Try sending a status first.";
		
		String pipeId = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeId)) {

			System.out.println(ERROR_USAGE);
			return;
		}
		
		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			System.out.println(ERROR_USAGE);
			return;
		}
		
		int localQueryId;
		
		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		}
		catch(NumberFormatException e) {
			System.out.println(ERROR_USAGE);
			return;
		}
		
		IStatefulPO statefulOp=null;
		
		IPhysicalQuery localQuery = executor.getExecutionPlan().getQueryById(localQueryId);
		for(IPhysicalOperator operator : localQuery.getAllOperators()) {
			if(operator instanceof IStatefulPO) {
				statefulOp = (IStatefulPO)operator;
				break;
			}
		}
		
		if(statefulOp==null) {
			System.out.println(ERROR_LOCALOP);
			return;
		}
		MovingStateReceiver receiver = MovingStateManager.getInstance().getReceiver(pipeId);
		if(receiver==null) {
			System.out.println(ERROR_NOTFOUND + pipeId);
			return;
		}
		
		try {
			receiver.injectState(statefulOp);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e) {
			System.out.println(ERROR_NOTHING_RECEIVED);
			return;
		}
		System.out.println("Injected State.");
		
	}

	/**
	 * Lists installed QueryParts (logical)
	 * 
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
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _lsLBStrategies(CommandInterpreter ci) {

		System.out.println("Available load balancing strategies:");
		for (ILoadBalancingStrategy strategy : ActiveLoadbalancingConsole.loadBalancingStrategies) {

			System.out.println(strategy.getName());

		}

	}

	/**
	 * Lists all available {@link ILoadBalancingAllocator}s bound via OSGI-DS.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _lsLBAllocators(CommandInterpreter ci) {

		System.out.println("Available load balancing allocators:");
		for (ILoadBalancingAllocator allocator : ActiveLoadbalancingConsole.loadBalancingAllocators) {

			System.out.println(allocator.getName());

		}

	}

	/**
	 * Lists all available {@link ILoadBalancingCommunicator} implementations
	 * bound via OSGI-DS
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance
	 */
	public void _lsLBCommunicators(CommandInterpreter ci) {
		System.out.println("Available load balancing Communicators:");
		for (ILoadBalancingCommunicator communicator : ActiveLoadbalancingConsole.loadBalancingCommunicators) {
			System.out.println(communicator.getName());
		}
	}

	/**
	 * Determines the {@link ILoadBalancingStrategy} by name.
	 * 
	 * @param strategyName
	 *            The name of the strategy.
	 * @return An {@link ILoadBalancingStrategy}, if there is one bound with
	 *         <code>strategyName</code> as name.
	 */
	private static Optional<ILoadBalancingStrategy> determineStrategy(
			String strategyName) {

		Preconditions.checkNotNull(strategyName,
				"The name of the load balancing strategy must be not null!");

		for (ILoadBalancingStrategy strategy : ActiveLoadbalancingConsole.loadBalancingStrategies) {

			if (strategy.getName().equals(strategyName)) {

				return Optional.of(strategy);

			}

		}

		return Optional.absent();

	}

	/**
	 * Determines the {@link ILoadBalancingAllocator} by name.
	 * 
	 * @param allocatorName
	 *            The name of the allocator.
	 * @return An {@link ILoadBalancingAllocator}, if there is one bound with
	 *         <code>allocatorName</code> as name.
	 */
	private static Optional<ILoadBalancingAllocator> determineAllocator(
			String allocatorName) {

		Preconditions.checkNotNull(allocatorName,
				"The name of the load balancing allocator must be not null!");

		for (ILoadBalancingAllocator allocator : ActiveLoadbalancingConsole.loadBalancingAllocators) {

			if (allocator.getName().equals(allocatorName)) {

				return Optional.of(allocator);

			}

		}

		return Optional.absent();

	}

	/**
	 * Determines the {@link ILoadBalancingCommunicator} by name.
	 * 
	 * @param communicatorName
	 *            The name of the allocator.
	 * @return An {@link ILoadBalancingCommunicator}, if there is one bound with
	 *         <code>communicatorName</code> as name.
	 */
	private static Optional<ILoadBalancingCommunicator> determineCommunicator(
			String communicatorName) {

		Preconditions.checkNotNull(communicatorName,
				"The name of the load balancing allocator must be not null!");

		for (ILoadBalancingCommunicator communicator : ActiveLoadbalancingConsole.loadBalancingCommunicators) {

			if (communicator.getName().equals(communicatorName)) {

				return Optional.of(communicator);

			}

		}

		return Optional.absent();

	}
	

	/**
	 * Returns currently active Session.
	 * 
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

	public static void _testLog(CommandInterpreter ci) {
		LOG.debug("Debug Log.");
		LOG.warn("Warning Log");
		LOG.info("Info Log.");
		LOG.error("Error Log");
	}
	
}
