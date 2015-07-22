package de.uniol.inf.is.odysseus.peer.loadbalancing.active.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateReceiver;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateSender;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingCommunicatorRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

/**
 * Console with debug commands for active LoadBalancing.
 * 
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
	private static IPeerDictionary peerDictionary;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IServerExecutor executor;
	private static ILoadBalancingCommunicatorRegistry communicatorRegistry;
	private static ILoadBalancingController loadBalancingControl;

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}
	
	public static void bindCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		communicatorRegistry = serv;
	}
	
	public static void unbindCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		if(communicatorRegistry == serv) {
			communicatorRegistry = null;
		}
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
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

	public static void bindLoadBalancingController(ILoadBalancingController serv) {
		loadBalancingControl = serv;
	}
	

	public static void unbindLoadBalancingController(ILoadBalancingController serv) {
		if(loadBalancingControl == serv) {
			loadBalancingControl=null;
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
		sb.append("    lsLBCommunicators                    - Lists all available load balancing communicators\n");
		sb.append("    initLB <strategyname> <allocatorname>	- Initiate Loadbalancing with load balancing strategy <strategyname> and load balancing allocator <allocatorname>\n");
		sb.append("    stopLB                            - Stops the Load Balancing\n");
		sb.append("    cpJxtaSender <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Sender\n");
		sb.append("    cpJxtaReceiver <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Receiver\n");
		sb.append("    installStateSender <peerID>                          - Installs new MovingStateSender to PeerID\n");
		sb.append("    installStateReceiver <peerID> <pipeID>               - Installs new MovingStateReceiver with peer and pipeID\n");
		sb.append("    sendData <pipeID>                                    - Sends Data to MovingStateSender with pipeID\n");
		sb.append("    testLog                                              - Outputs different log levels.\n");
		sb.append("    testSendState <pipeID> <LocalQueryId>              - Sends state of first StatefulPO in Query to pipe\n");
		sb.append("    testInjectState <pipeID> <LocalQueryId>              - Injects received state from pipe to first statefol Operator in query.\n");
		sb.append("    listStatefolOperators <queryID>                      - Lists all statefol Operators in a query (useful to determine order)\n");
		sb.append("    printSubscriptions <queryId>                         - Prints detais about all Subscriptions in a query.\n");
		sb.append("    sendLongString <pipeID>                              - Sends a 100MB big String to test Buffers.\n");
		sb.append("    printQPController									- Prints Current Content of Query Part Controller\n");
		sb.append("    verifyDependencies									- Verifies if all Dependencies are available\n");
		sb.append("    extractState <LocalQueryId>							- Extracts first StatefulPO State from Query with Query ID\n");
		return sb.toString();
	}

	/**
	 * Lists all Stateful Operators in a particular query.
	 * 
	 * @param ci
	 *            CommandInterpreter
	 */
	public void _listStatefulOperators(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: listStatefulOperators <queryID>";
		ci.println("Looking for stateful OPs");

		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		List<IStatefulPO> statefulList = MovingStateHelper
				.getStatefulOperatorList(localQueryId);
		int i = 0;
		for (IStatefulPO statefulOp : statefulList) {
			IPhysicalOperator operator = (IPhysicalOperator) statefulOp;
			ci.println("[" + i + "] " + operator.getName());
		}
	}
	
	public void _verifyDependencies(CommandInterpreter ci) {
		boolean executorAvailable = (OsgiServiceManager.getExecutor()!=null);
		boolean networkManagerAvailable = (OsgiServiceManager.getP2pNetworkManager()!=null);
		boolean queryManagerAvailable = (OsgiServiceManager.getQueryManager()!=null);
		boolean queryPartControllerAvailable = (OsgiServiceManager.getQueryPartController()!=null);
		
		ci.println("Executor: " + executorAvailable);
		ci.println("P2PNetworkManager: " + networkManagerAvailable);
		ci.println("LoadBalancingQueryManager: " + queryManagerAvailable);
		ci.println("QueryPartController: " + queryPartControllerAvailable);
				
	}
	
	public void _printQPController(CommandInterpreter ci) {
		IQueryPartController controller = OsgiServiceManager.getQueryPartController();
		for (int query : executor.getLogicalQueryIds(OsgiServiceManager.getActiveSession())) {
			ci.println();
			ID sharedQueryID = controller.getSharedQueryID(query);
			boolean isMaster = controller.isMasterForQuery(query);
			
			if(isMaster) {
				ci.println("["+query+"] ***MASTER*** SharedQueryID" + sharedQueryID);
			}
			else {
				ci.println("["+query+"] ***SLAVE*** SharedQueryID" + sharedQueryID);
				ci.println("(Master is " + peerDictionary.getRemotePeerName(controller.getMasterForQuery(sharedQueryID))+")");
			}
			
			if(isMaster) {
				for (PeerID other : controller.getOtherPeers(sharedQueryID)) {
					ci.println("        -> Slave: "+ peerDictionary.getRemotePeerName(other));
				}
			}
			ci.print("        -> Local Queries:");
			for (int queryNum : controller.getLocalIds(sharedQueryID)) {
				ci.print(queryNum+",");
			}
			
			
		}
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

		final String ERROR_NO_CONTROLLER = "No Load Balancing controller bound.";
		
		if(loadBalancingControl==null) {
			ci.println(ERROR_NO_CONTROLLER);
			return;
		}			
		
		
		if (loadBalancingControl.isLoadBalancingRunning()) {
			ci.println(ERROR_ALREADY_RUNNING);
			return;
		}

		String strategyName = ci.nextArgument();
		if (Strings.isNullOrEmpty(strategyName)) {
			ci.println(ERROR_USAGE);
			return;
		}

		if (!loadBalancingControl.setLoadBalancingStrategy(strategyName)) {
			ci.println(ERROR_STRATEGY + strategyName);
			return;

		}

		String allocatorName = ci.nextArgument();
		if (Strings.isNullOrEmpty(allocatorName)) {
			ci.println(ERROR_USAGE);
			return;

		}

		if (!loadBalancingControl.setLoadBalancingAllocator(allocatorName)) {
			ci.println(ERROR_ALLOCATOR + allocatorName);
			return;
		}
		
		loadBalancingControl.startLoadBalancing();

	}

	/**
	 * Stops Loadbalancing Monitoring.
	 * 
	 * @param ci
	 *            CommandInterpreter
	 */
	public void _stopLB(CommandInterpreter ci) {

		final String ERROR_NOT_RUNNING = "No load balancing running!";
		final String ERROR_NO_CONTROLLER = "No Load Balancing controller bound.";
		if(loadBalancingControl==null) {
			ci.println(ERROR_NO_CONTROLLER);
			return;
		}			
			
		if (!loadBalancingControl.isLoadBalancingRunning()) {
			ci.println(ERROR_NOT_RUNNING);
			return;
		}

		loadBalancingControl.stopLoadBalancing();

	}

	/**
	 * Manually install State sender (returns a PipeID which can be used to
	 * install State Receiver)
	 * 
	 * @param ci
	 *            CommandInterpreter
	 */
	public void _installStateSender(CommandInterpreter ci) {

		final String ERROR_USAGE = "Usage: installStateSender <peerName>";
		final String ERROR_NOTFOUND = "Error: No peer found with name ";
		

		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {

			ci.println(ERROR_USAGE);
			return;
		}

		Collection<PeerID> peerIDs = peerDictionary.getRemotePeerIDs();
		for (PeerID peer : peerIDs) {
			if (peerDictionary.getRemotePeerName(peer).equals(peerName)) {
				String newPipe = MovingStateManager.getInstance().addSender(
						peer.toString());
				ci.println("Pipe installed:");
				ci.println(newPipe);
				return;
			}
		}
		ci.println(ERROR_NOTFOUND + peerName);
	}

	public void _testSendState(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: testSendStatus <pipeID> <LocalQueryId>";
		final String ERROR_LOCALOP = "ERROR: Local Query does not contain stateful Operator.";
		final String ERROR_NOTFOUND = "ERROR: No Sender found for pipe:";
		final String ERROR_NO_STATE ="Error: Could not get state.";
		
		String pipeId = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeId)) {

			ci.println(ERROR_USAGE);
			return;
		}

		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		IStatefulPO statefulOp = null;

		IPhysicalQuery localQuery = executor.getExecutionPlan().getQueryById(
				localQueryId);
		for (IPhysicalOperator operator : localQuery.getAllOperators()) {
			if (operator instanceof IStatefulPO) {
				statefulOp = (IStatefulPO) operator;
				break;
			}
		}

		if (statefulOp == null) {
			ci.println(ERROR_LOCALOP);
			return;
		}
		IPhysicalOperator physicalOp = (IPhysicalOperator)statefulOp;
		Serializable state = null;
		
		try {
			MovingStateHelper.startBuffering(physicalOp);
			IOperatorState stateObject = statefulOp.getState();
			state = (Serializable)stateObject.getSerializedState();
			MovingStateHelper.stopBuffering(physicalOp);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e1) {
			ci.print("Error while Stopping or Starting Buffering.");
		}
		MovingStateSender sender = MovingStateManager.getInstance().getSender(
				pipeId);
		if (sender == null) {
			ci.println(ERROR_NOTFOUND + pipeId);
			return;
		}
		
		if(state==null) {
			ci.println(ERROR_NO_STATE);
		}
		
		try {
			sender.sendData(state);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e) {
			ci.println("ERROR:" + e.getMessage());
			return;
		}
		ci.println("Data sent.");

	}
	

	public void _extractState(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: extractState <LocalQueryId>";
		final String ERROR_LOCALOP = "ERROR: Local Query does not contain stateful Operator.";
		final String ERROR_NO_STATE ="Error: Could not get state.";
		
		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		IStatefulPO statefulOp = null;

		IPhysicalQuery localQuery = executor.getExecutionPlan().getQueryById(
				localQueryId);
		for (IPhysicalOperator operator : localQuery.getAllOperators()) {
			if (operator instanceof IStatefulPO) {
				statefulOp = (IStatefulPO) operator;
				break;
			}
		}

		if (statefulOp == null) {
			ci.println(ERROR_LOCALOP);
			return;
		}
		IPhysicalOperator physicalOp = (IPhysicalOperator)statefulOp;
		Serializable state = null;
		
		try {
			MovingStateHelper.startBuffering(physicalOp);
			IOperatorState stateObject = statefulOp.getState();
			long startTime = System.currentTimeMillis();
			ci.println("Estimated size:"+stateObject.estimateSizeInBytes());
			long curTime = System.currentTimeMillis();
			ci.println("Estimation took "+(curTime-startTime)+" ms.");
			startTime = System.currentTimeMillis();
			state = (Serializable)stateObject.getSerializedState();
			int realSize = getSizeInBytesOfSerializable(state);
			curTime = System.currentTimeMillis();
			ci.println("Real State Size (after serialization:)" + realSize);
			ci.println("Serialization took "+(curTime-startTime)+" ms.");
			
			MovingStateHelper.stopBuffering(physicalOp);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e1) {
			ci.print("Error while Stopping or Starting Buffering.");
		}
		
		if(state==null) {
			ci.println(ERROR_NO_STATE);
		}
		ci.println(state.toString());
		
		ci.println("Data sent.");

	}
	
	

	protected int getSizeInBytesOfSerializable(Serializable obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			out.flush();
			out.close();
		} catch (IOException e) {
			LOG.error("Could not serialize Streamable. Returning 0");
			return 0;
		}
		//Sub 4 Bytes for Serialization magic Numbers
		int objectSize = baos.toByteArray().length - 4;
		return objectSize;
	}
	

	/***
	 * Prints all downstream Subscriptions of a Query (Can be used to track
	 * open/suspend Calls)
	 * 
	 * @param ci
	 */
	public void _printSubscriptions(CommandInterpreter ci) {

		final String ERROR_USAGE = "Usage: printSubscriptions <queryId>";

		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		IExecutor executor = OsgiServiceManager.getExecutor();
		List<IPhysicalOperator> roots = executor.getPhysicalRoots(localQueryId,
				OsgiServiceManager.getActiveSession());

		ArrayList<IPhysicalOperator> sourcesOfQuery = new ArrayList<IPhysicalOperator>();
		for (IPhysicalOperator root : roots) {
			ArrayList<IPhysicalOperator> newSources = traverseGraphAndGetSources(
					root, new ArrayList<IPhysicalOperator>());
			for (IPhysicalOperator source : newSources) {
				if (!sourcesOfQuery.contains(source))
					sourcesOfQuery.add(source);
			}
		}

		for (IPhysicalOperator source : sourcesOfQuery) {
			traverseGraphAndPrintSubscriptions(source, ci);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * Used when printing subscriptions. Recursively traverses the whole graph from the roots upstream to get sources.
	 * @param root Where to start traversing
	 * @param knownSources Already known sources.
	 * @return List of sources.
	 */
	private static ArrayList<IPhysicalOperator> traverseGraphAndGetSources(
			IPhysicalOperator root, ArrayList<IPhysicalOperator> knownSources) {
		if (root instanceof IPipe) {
			IPipe rootAsSource = (IPipe) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>) rootAsSource
					.getSubscribedToSource()) {
				ArrayList<IPhysicalOperator> newSources = new ArrayList<IPhysicalOperator>();
				newSources.addAll(traverseGraphAndGetSources(
						(IPhysicalOperator) subscription.getTarget(),
						knownSources));
				for (IPhysicalOperator source : newSources) {
					if (!knownSources.contains(source)) {
						knownSources.add(source);
					}
				}
			}
			return knownSources;
		}

		if (root instanceof ISource) {
			if (!knownSources.contains(root)) {
				knownSources.add(root);
			}
			return knownSources;
		}
		if (root instanceof ISink) {
			ISink rootAsSink = (ISink) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>) rootAsSink
					.getSubscribedToSource()) {
				ArrayList<IPhysicalOperator> newSources = new ArrayList<IPhysicalOperator>();
				newSources.addAll(traverseGraphAndGetSources(
						(IPhysicalOperator) subscription.getTarget(),
						knownSources));
				for (IPhysicalOperator source : newSources) {
					if (!knownSources.contains(source)) {
						knownSources.add(source);
					}
				}
			}
			return knownSources;
		}
		return knownSources;
	}

	/***
	 * Traverses Graph downstream recursively and prints all Subscriptions
	 * 
	 * @param root
	 *            Where to start.
	 * @param ci
	 *            CommandInterpreter
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void traverseGraphAndPrintSubscriptions(
			IPhysicalOperator root, CommandInterpreter ci) {
		if (root instanceof IPipe) {
			IPipe rootAsPipe = (IPipe) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>) rootAsPipe
					.getSubscriptions()) {
				printSubscsription(subscription, root, ci);
				traverseGraphAndPrintSubscriptions(
						(IPhysicalOperator) subscription.getTarget(), ci);
			}
			return;
		}

		if (root instanceof ISource) {
			ISource rootAsSource = (ISource) root;
			for (AbstractPhysicalSubscription subscription : (Collection<AbstractPhysicalSubscription>) rootAsSource
					.getSubscriptions()) {
				printSubscsription(subscription, root, ci);
				traverseGraphAndPrintSubscriptions(
						(IPhysicalOperator) subscription.getTarget(), ci);
			}
			return;
		}
		if (root instanceof ISink) {
			return;
		}
		return;
	}

	/***
	 * Prints a subscription.
	 * 
	 * @param subscription
	 *            Subscription to print.
	 * @param operator
	 *            Operator where subscription belongs to
	 * @param ci
	 *            CommandInterpreter (needed for printing)
	 */
	@SuppressWarnings("rawtypes")
	private static void printSubscsription(
			AbstractPhysicalSubscription subscription,
			IPhysicalOperator operator, CommandInterpreter ci) {
		IPhysicalOperator target = (IPhysicalOperator) subscription.getTarget();
		if (subscription instanceof ControllablePhysicalSubscription) {
			ControllablePhysicalSubscription cSub = (ControllablePhysicalSubscription) subscription;
			ci.println(operator.getName() + "->" + target.getName()
					+ "    OpenCalls:" + cSub.getOpenCalls() + " suspended: "
					+ cSub.isSuspended());
			ci.println("* " + cSub.toString());
		} else {
			ci.println(operator.getName() + "->" + target.getName()
					+ "    OpenCalls:" + subscription.getOpenCalls()
					+ " (not controllable)");
		}
	}

	public void _installStateReceiver(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: installStateReceiver <peerName> <pipeID>";
		final String ERROR_NOTFOUND = "Error: No peer found with name ";

		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {

			ci.println(ERROR_USAGE);
			return;
		}

		String pipeID = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeID)) {
			ci.println(ERROR_USAGE);
			return;
		}

		Collection<PeerID> peerIDs = peerDictionary.getRemotePeerIDs();
		for (PeerID peer : peerIDs) {
			if (peerDictionary.getRemotePeerName(peer).equals(peerName)) {
				MovingStateManager.getInstance().addReceiver(peer.toString(),
						pipeID);
				ci.println("Receiver installed");
				return;
			}
		}
		ci.println(ERROR_NOTFOUND + peerName);
	}

	public void _sendData(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: sendData <pipeID> <message>";
		final String ERROR_NOTFOUND = "Error: No sender found with pipe ";
		String pipeID = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeID)) {

			ci.println(ERROR_USAGE);
			return;
		}

		String nextWord = ci.nextArgument();
		if (Strings.isNullOrEmpty(nextWord)) {

			ci.println(ERROR_USAGE);
			return;
		}
		StringBuilder sb = new StringBuilder();
		while (nextWord != null) {
			sb.append(nextWord);
			sb.append(' ');
			nextWord = ci.nextArgument();
		}
		String message = sb.toString();

		MovingStateSender sender = MovingStateManager.getInstance().getSender(
				pipeID);
		if (sender == null) {
			ci.println(ERROR_NOTFOUND + pipeID);
			return;
		}
		try {
			sender.sendData(message);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e) {
			ci.println("ERROR:" + e.getMessage());
			return;
		}
		ci.println("Sent: " + message);
	}
	
	
	public void _sendLongString(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: sendLongString <pipeID>";
		final String ERROR_NOTFOUND = "Error: No sender found with pipe ";
		String pipeID = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeID)) {

			ci.println(ERROR_USAGE);
			return;
		}
		
		//Create 100MB of Data.
		
		StringBuilder sb = new StringBuilder();
		StringBuilder kbString = new StringBuilder();
		//Create 1kB
		for (int i=0;i<1024;i++) {
			kbString.append('A');
		}
		StringBuilder mbString = new StringBuilder();
		//Creat 1Mb
		for (int i=0;i<1024;i++) {
			mbString.append(kbString);
		}
		//Create 100Mb
		for (int i=0;i<100;i++) {
			sb.append(mbString);
		}
		String message = sb.toString();

		MovingStateSender sender = MovingStateManager.getInstance().getSender(
				pipeID);
		if (sender == null) {
			ci.println(ERROR_NOTFOUND + pipeID);
			return;
		}
		try {
			sender.sendData(message);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e) {
			ci.println("ERROR:" + e.getMessage());
			return;
		}
		ci.println("Sent.");
	}

	/***
	 * Injects a previously received state in the first stateful Operator in
	 * List. Can be used to test serialisation.
	 * 
	 * @param ci
	 *            CommandInterpreter
	 */
	public void _testInjectState(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: testInjectState <pipeID> <LocalQueryId>";
		final String ERROR_LOCALOP = "ERROR: Local Query does not contain stateful Operator.";
		final String ERROR_NOTFOUND = "ERROR: No Sender found for pipe:";
		final String ERROR_NOTHING_RECEIVED = "ERROR: No status received. Try sending a status first.";

		String pipeId = ci.nextArgument();
		if (Strings.isNullOrEmpty(pipeId)) {

			ci.println(ERROR_USAGE);
			return;
		}

		String localQueryIdString = ci.nextArgument();
		if (Strings.isNullOrEmpty(localQueryIdString)) {

			ci.println(ERROR_USAGE);
			return;
		}

		int localQueryId;

		try {
			localQueryId = Integer.parseInt(localQueryIdString);
		} catch (NumberFormatException e) {
			ci.println(ERROR_USAGE);
			return;
		}

		IStatefulPO statefulOp = null;

		IPhysicalQuery localQuery = executor.getExecutionPlan().getQueryById(
				localQueryId);
		for (IPhysicalOperator operator : localQuery.getAllOperators()) {
			if (operator instanceof IStatefulPO) {
				statefulOp = (IStatefulPO) operator;
				break;
			}
		}

		if (statefulOp == null) {
			ci.println(ERROR_LOCALOP);
			return;
		}
		MovingStateReceiver receiver = MovingStateManager.getInstance()
				.getReceiver(pipeId);
		if (receiver == null) {
			ci.println(ERROR_NOTFOUND + pipeId);
			return;
		}

		try {
			receiver.injectState(statefulOp);
		} catch (de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.LoadBalancingException e) {
			ci.println(ERROR_NOTHING_RECEIVED);
			return;
		}
		ci.println("Injected State.");

	}

	/**
	 * Lists installed QueryParts (logical)
	 * 
	 * @param ci
	 *            CommandInterpreter
	 */
	public void _lsParts(CommandInterpreter ci) {

		ci.println("Query Parts on current Peer:");
		for (int queryID : executor.getLogicalQueryIds(getActiveSession())) {
			ci.println("["
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

		final String ERROR_NO_CONTROLLER = "No Load Balancing controller bound.";
		if(loadBalancingControl==null) {
			ci.println(ERROR_NO_CONTROLLER);
			return;
		}			
		
		ci.println("Available load balancing strategies:");
		for (String strategyName : loadBalancingControl.getAvailableStrategies()) {

			ci.println(strategyName);

		}

	}

	/**
	 * Lists all available {@link ILoadBalancingAllocator}s bound via OSGI-DS.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _lsLBAllocators(CommandInterpreter ci) {

		final String ERROR_NO_CONTROLLER = "No Load Balancing controller bound.";
		if(loadBalancingControl==null) {
			ci.println(ERROR_NO_CONTROLLER);
			return;
		}			

		ci.println("Available load balancing Allocators:");
		for (String allocatorName : loadBalancingControl.getAvailableAllocators()) {

			ci.println(allocatorName);

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
		ci.println("Available load balancing Communicators:");
		for (String communicator : communicatorRegistry.getRegisteredCommunicators()) {
			ci.println(communicator);
		}
	}


	/**
	 * Returns currently active Session.
	 * 
	 * @return Session.
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

	/**
	 * Prints a Message on every Log-Level, useful to verify which log-level is
	 * active ;)
	 * 
	 * @param ci
	 */
	public static void _testLog(CommandInterpreter ci) {
		LOG.debug("Debug Log.");
		LOG.warn("Warning Log");
		LOG.info("Info Log.");
		LOG.error("Error Log");
	}

}
