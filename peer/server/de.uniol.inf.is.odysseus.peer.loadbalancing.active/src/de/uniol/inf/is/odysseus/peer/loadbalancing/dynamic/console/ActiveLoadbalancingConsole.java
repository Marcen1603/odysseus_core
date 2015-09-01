package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.console;

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
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.control.ILoadBalancingController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingCommunicatorRegistry;

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
	private static IServerExecutor executor;
	private static ILoadBalancingController loadBalancingControl;
	private static IQueryPartController queryPartController;
	private static ILoadBalancingLock lock;
	

	private static ILoadBalancingCommunicatorRegistry communicatorRegistry;
	
	
	
	
	
	public static void bindQueryPartController(IQueryPartController serv) {
		queryPartController=serv;
	}
	
	public static void unbindQueryPartController(IQueryPartController serv) {
		if(queryPartController==serv) {
			queryPartController=null;
		}
	}
	
	public static void bindLoadBalancingLock(ILoadBalancingLock serv) {
		lock = serv;
	}
	
	public static void unbindLoadBalancingLock(ILoadBalancingLock serv) {
		if(lock==serv) {
			lock=null;
		}
	}

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
	}

	// called by OSGi-DS
	public void deactivate() {
	}

	@Override
	/**
	 * Outputs a simple Help.
	 */
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Active Loadbalancing commands---\n");
		sb.append("\n");
		sb.append("   Diagnostic Commands\n");
		sb.append("     lsParts		             	 				- Lists all queryParts installed on peer with ids\n");
		sb.append("     lsLBStrategies	              				- Lists all available load balancing strategies\n");
		sb.append("     lsLBCommunicators                 		    - Lists all available load balancing communicators\n");
		sb.append("     testLog                                     - Outputs different log levels.\n");
		sb.append("     printSubscriptions <queryId>                - Prints detais about all Subscriptions in a query.\n");
		sb.append("     printQPController					  		- Prints Current Content of Query Part Controller\n");
		sb.append("     verifyDependencies							- Verifies if all Dependencies are available\n");
		sb.append("     dumpl <LocalQueryID>                        - dumps logical plan of queryId\n");
		sb.append("     sharedQueryInfo <LocalQueryID>   			- prints Info aboutd shared Query For QueryId\n");
		sb.append("     showLogicalSchemaInfo <LocalQueryID>   		- prints Schema Information of all logical Operators in Query.\n");
		sb.append("     printMetadataRegistry                       - Print currently registered Metadata names.\n");
		sb.append("     showSourceInfo                              - Shows Information about installed Sources\n");
		sb.append("     lockStatus                                  - Shows status of Load Balancing Lock\n");
		sb.append("     qv                                          - Shows a Console based QueryView\n");
		sb.append("\n");
		sb.append("   Control LoadBalancing\n");
		sb.append("     initLB <strategyname>						- Initiate Loadbalancing with load balancing strategy <strategyname>\n");
		sb.append("     stopLB                            			- Stops the Load Balancing\n");
		sb.append("   Debug LoadBalancing\n");
		sb.append("    cpJxtaSender <oldPipeId> <newPipeId> <newPeername>   - Tries to copy and install a Sender\n");
		sb.append("    cpJxtaReceiver <oldPipeId> <newPipeId> <newPeername> - Tries to copy and install a Receiver\n");
		sb.append("    forceUnlock                                          - forces Load Balancing Lock to unlock\n");
		sb.append("    testCommunicator <communicatorName> <queryId> <PeerName> - Tries to send Query with chosen communicator to chosen peer.\n");
		return sb.toString();
	}
	
	
	public void _qv(CommandInterpreter ci) {
		
		ci.println("Current Queries:");
		ci.println("ID | Name | Status | Parser | Startzeit");
		
		for(IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			StringBuilder sb = new StringBuilder();
			sb.append(query.getID());
			sb.append(" |");
			sb.append(query.getName());
			sb.append(" | ");
			sb.append(query.getState());
			sb.append(" | ");
			sb.append(query.getParserId());
			sb.append(" | ");
			sb.append(query.getQueryStartTS());
			ci.println(sb.toString());
		}
	}
	
	public void _testCommunicator(CommandInterpreter ci) {

		Preconditions.checkNotNull(communicatorRegistry, "Communicator Registry not bound.");
		Preconditions.checkNotNull(peerDictionary,"Peer Dictionary not bound.");
		
		final String ERROR_USAGE = "Usage: testCommunciator <communicatorName> <queryID> <otherPeerName>";
		final String ERROR_COMMUNICATOR = "Error: Communicator not found.";
		final String ERROR_PEER = "Error: Peer unknown";
		
		String communicatorName = ci.nextArgument();
		if (Strings.isNullOrEmpty(communicatorName)) {
			ci.println(ERROR_USAGE);
			return;
		}
		
		ILoadBalancingCommunicator communicator = communicatorRegistry.getCommunicator(communicatorName);
		
		if(communicator==null) {
			ci.println(ERROR_COMMUNICATOR);
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
		
		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {
			ci.println(ERROR_USAGE);
			return;
		}
		PeerID pid = null;
		for(PeerID pidFromList : peerDictionary.getRemotePeerIDs()) {
			if(peerDictionary.getRemotePeerName(pidFromList).equals(peerName)) {
				pid = pidFromList;
				break;
			}
		}
		if(pid==null) {
			ci.println(ERROR_PEER);
			return;
		}
		ci.println("Initiating transfer.");
		communicator.initiateLoadBalancing(pid, localQueryId);
		
	}
	
	
	public void _forceUnlock(CommandInterpreter ci) {
		if(lock==null) {
			ci.println("Lock is null.");
		}
		else {
			lock.forceUnlock();
			ci.println("Unlocking of Peer forced.");
			ci.println("Cuurent Lock Status:" + lock.isLocked());
		}
	}
	
	public void _lockStatus(CommandInterpreter ci) {
		if(lock==null) {
			ci.println("Lock is null.");
		}
		else {
			ci.println("Cuurent Lock Status:" + lock.isLocked());
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
		final String ERROR_USAGE = "usage: initLB <strategyname>";
		final String ERROR_STRATEGY = "No load balancing strategy found with the name ";

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
	
	public void _showSourceInfo(CommandInterpreter ci) {
		List<ViewInformation> sources = executor.getStreamsAndViewsInformation(getActiveSession());
		
		for(ViewInformation source : sources) {
			ci.println();
			ci.println("Source Name:" + source.getName());
			ci.println("Output Schema: " + source.getOutputSchema().toString());
			
			List<SDFMetaSchema> metaschemata = source.getOutputSchema().getMetaschema();
			StringBuilder sb = new StringBuilder();
			for(SDFMetaSchema metaAttribute : metaschemata) {
				sb.append(" || ");
				sb.append(metaAttribute.toString());
			}
			ci.println("META-Schema: " + sb.toString());
			ci.println();
			
		}
	}
	
	public void _showLogicalSchemaInfo(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: dumpl <LocalQueryId>";

		final String ERROR_QUERY = "Logical Query not found.";
		
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
		ILogicalQuery query = null;
		try {
			query = executor.getLogicalQueryById(localQueryId, getActiveSession());
		}
		catch(Exception e) {
			ci.println(ERROR_QUERY);
			return;
		}
		if(query==null){
			ci.println(ERROR_QUERY);
			return;
		}
		List<ILogicalOperator> operatorList = Lists.newArrayList();
		RestructHelper.collectOperators(query.getLogicalPlan(), operatorList);
		for(ILogicalOperator op : operatorList) {
			
			ci.println("Operator:" + op.getName());
			ci.println("IN:");
			for(int i=0;i<op.getNumberOfInputs();i++) {
				SDFSchema schema = op.getInputSchema(i);
				ci.println("("+i+") SCHEMA: " + schema.toString());
				
				List<SDFMetaSchema> metaschema = schema.getMetaschema();
				StringBuilder sb = new StringBuilder();
				for(SDFMetaSchema metaAttribute : metaschema) {
					sb.append(" || ");
					sb.append(metaAttribute.toString());
				}
				ci.println("("+i+") META: " + sb.toString());
			}
			
			ci.println("OUT:");
			for(Integer i : op.getOutputSchemaMap().keySet()) {
				
				SDFSchema schema = op.getOutputSchema(i);
				ci.println("("+i+") SCHEMA: " + schema.toString());
				
				List<SDFMetaSchema> metaschema = schema.getMetaschema();
				StringBuilder sb = new StringBuilder();
				for(SDFMetaSchema metaAttribute : metaschema) {
					sb.append(" || ");
					sb.append(metaAttribute.toString());
				}
				ci.println("("+i+") META: " + sb.toString());
			}
			
		}
	}
	
	public void _printMetadataRegistry(CommandInterpreter ci) {
		ci.println("Currently Registered (not combined) names in Metadata Registry:");
		for(String name : MetadataRegistry.getNames()) {
			ci.println(name);
		}
	}
	
	

	public void _dumpl(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: dumpl <LocalQueryId>";

		final String ERROR_QUERY = "Logical Query not found.";
		
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

		ILogicalQuery query = executor.getLogicalQueryById(localQueryId, getActiveSession());
		if(query==null){
			ci.println(ERROR_QUERY);
			return;
		}
		
		SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
		String plan = printer.createString(query.getLogicalPlan());
		
		ci.println(plan);

	}
	


	public void _sharedQueryInfo(CommandInterpreter ci) {
		final String ERROR_USAGE = "Usage: dumpl <LocalQueryId>";

		final String ERROR_QUERY = "Logical Query not found.";
		
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

		ILogicalQuery query = executor.getLogicalQueryById(localQueryId, getActiveSession());
		if(query==null){
			ci.println(ERROR_QUERY);
			return;
		}
		
		ID sharedQueryID = queryPartController.getSharedQueryID(localQueryId);
		if(sharedQueryID==null) {
			ci.println("Shared Query ID is null.");
		}
		else {
			ci.println("Shared Query ID:"+sharedQueryID.toString());
			ci.println("Local IDs in shared Query:");
			for(Integer qid : queryPartController.getLocalIds(sharedQueryID)) {
				ci.print(qid);
				if(queryPartController.isMasterForQuery(qid)) {
					ci.print("(Master)");
				}
				ci.print(" ");
			}
			ci.println();
		}

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
