package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.QueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class LoadBalancingCommunicationListener implements IPeerCommunicatorListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingCommunicationListener.class);

	private static IServerExecutor executor;
	private static IPeerCommunicator peerCommunicator;
	private static LoadBalancingCommunicationListener instance;
	
	private static ISession activeSession;
	
	@SuppressWarnings("unused")
	private ArrayList<Integer> activeLoadBalancingProcesses;

	public static LoadBalancingCommunicationListener getInstance() {
		return instance;
	}

	public void activate() {
		this.activeLoadBalancingProcesses = new ArrayList<Integer>();
		instance = this;
		LOG.debug("Instantiated");
	}

	public void deactivate() {
		this.activeLoadBalancingProcesses = null;
		LOG.debug("Deinitialized");
		instance = null;
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor exe) {
		LOG.debug("Bound Executor.");
		executor = (IServerExecutor) exe;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor exe) {
		LOG.debug("Unbound Executor.");
		if (executor == exe) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Bound Peer Communicator.");
		peerCommunicator = serv;
		peerCommunicator
				.registerMessageType(LoadBalancingInitiateCopyMessage.class);
		peerCommunicator.registerMessageType(LoadBalancingInitiateMessage.class);
		
		peerCommunicator.addListener(this,
				LoadBalancingInitiateCopyMessage.class);
		peerCommunicator.addListener(this, LoadBalancingInitiateMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Unbound Peer Communicator.");
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this,
					LoadBalancingInitiateCopyMessage.class);
			peerCommunicator.removeListener(this, LoadBalancingInitiateMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingInitiateCopyMessage.class);
			peerCommunicator.unregisterMessageType(LoadBalancingInitiateMessage.class);
			peerCommunicator = null;
		}
	}

	
	private void relinkQueryPart(QueryPart part) {
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		
		if(message instanceof LoadBalancingInitiateMessage) {
			//TODO Check if loadBalancing ok.
			//Answer
			sendCopyMessage(senderPeer,((LoadBalancingInitiateMessage)message).getLoadBalancingProcessId());
		}
		
		if(message instanceof LoadBalancingInitiateCopyMessage) {
			int loadBalancingProcessId = ((LoadBalancingInitiateCopyMessage)message).getLoadBalancingProcessId();
			ILogicalQueryPart part = LoadBalancingQueryCache.getInstance().getQueryPart(loadBalancingProcessId);
			ILogicalQueryPart copy = getCopyOfQueryPart(part);
			QueryPart newQueryPart = new QueryPart(copy.getOperators(),senderPeer.toURI().toString());
			relinkQueryPart(newQueryPart);
			String pqlFromQueryPart = getPQLForQueryPart(copy);
			LOG.debug(pqlFromQueryPart);
			//TODO Send PQL to volunteering Peer.
		}
	}


	/**
	 * Sends a copy Message to initiating Peer, this message initiates a copy
	 * process for given LoadBalancing Process. This should be an answer to the
	 * initiating peer, after being accepted as loadbalancing partner.
	 * 
	 * @param destinationPeerId
	 *            initiating Peer
	 */
	public void sendCopyMessage(PeerID destinationPeerId,int loadBalancingProcessId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator.send(destinationPeerId,
						new LoadBalancingInitiateCopyMessage(loadBalancingProcessId));
			}
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send Message");
		}
	}
	
	
	private ILogicalQueryPart getCopyOfQueryPart(ILogicalQueryPart part) {
		ILogicalQueryPart result=null;
		ArrayList<ILogicalQueryPart> partsList = new ArrayList<ILogicalQueryPart>();
		partsList.add(part);
		Map<ILogicalQueryPart,ILogicalQueryPart> copies = getCopyOfQueryParts(partsList);
		for (Map.Entry<ILogicalQueryPart,ILogicalQueryPart> entry : copies.entrySet()) {
			result = entry.getKey();
		}
		return result;
	}
	
	/**
	 * Returns cut copies for each QueryPart in List
	 * @param parts QueryParts to copy
	 * @return Cut copies of Query parts.
	 */
	private Map<ILogicalQueryPart,ILogicalQueryPart> getCopyOfQueryParts(
			Collection<ILogicalQueryPart> parts) {
		Map<ILogicalQueryPart, ILogicalQueryPart> copies = LogicalQueryHelper
				.copyQueryPartsDeep(parts);
		return copies;
	}
	
	private Collection<ILogicalQueryPart> getRunningQueryParts() {
		ArrayList<ILogicalQueryPart> parts = new ArrayList<ILogicalQueryPart>(); 
		for (int queryId : executor.getLogicalQueryIds(getActiveSession())) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId, getActiveSession());
			
			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			parts.add(new LogicalQueryPart(operators));
		}
		return parts;
	}
	
	
	public void testCopy(PeerID otherPeer,int queryPartID) {
		ILogicalQueryPart partToCopy = null;
		for(ILogicalQueryPart part : getRunningQueryParts()) {
			partToCopy = part;
		}
		int lbProcessIdentifier = LoadBalancingQueryCache.getInstance().addQueryPartCopy(partToCopy);
		sendInitiateLoadBalancingMessage(otherPeer,lbProcessIdentifier);
	}
	

	/**
	 * Returns a PQL Statement for a LogicalQueryPart.
	 * @param queryPart Part for which PQL should be returned
	 * @return PQL describing QueryPart.
	 */
	private String getPQLForQueryPart(ILogicalQueryPart queryPart) {
		return LogicalQueryHelper.generatePQLStatementFromQueryPart(queryPart);
	}
	
	private void sendInitiateLoadBalancingMessage(PeerID volunteeringPeer, int loadBalancingProcessId) {
			try {
				if (peerCommunicator != null) {
					peerCommunicator.send(volunteeringPeer,
							new LoadBalancingInitiateMessage(loadBalancingProcessId));
				}
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send Message");
			}
	}
	
	public static ISession getActiveSession() {
		if(activeSession == null || !activeSession.isValid()) {	
			activeSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		
		return activeSession;
	}

	
	

}