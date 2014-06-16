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
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingMessage.messageTypes;

public class ActiveLoadBalancingProtocol implements IPeerCommunicatorListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ActiveLoadBalancingProtocol.class);
	
	private static IServerExecutor executor;
	private static IPeerCommunicator peerCommunicator;
	private static ActiveLoadBalancingProtocol instance;
		
	public static ActiveLoadBalancingProtocol getInstance() {
		return instance;
	}
	
	public void activate() {
		instance = this;
		LOG.debug("Instantiated");
	}
	
	public void deactivate() {
		LOG.debug("Deinitialized");
		instance=null;
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
		peerCommunicator.registerMessageType(LoadBalancingMessage.class);
		peerCommunicator.addListener(this, LoadBalancingMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Unbound Peer Communicator.");
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, LoadBalancingMessage.class);
			peerCommunicator.unregisterMessageType(LoadBalancingMessage.class);
			peerCommunicator = null;
		}
	}

	@SuppressWarnings("unused")
	private void copyQueryPart(int queryID, ISession session) {
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID,
				session);
		
		LOG.debug("Got Copy Message for Query: {}",queryID);
		
		ILogicalOperator queryPlan = logicalQuery.getLogicalPlan();
		LogicalQueryPart part = new LogicalQueryPart(queryPlan);
		ArrayList<ILogicalQueryPart> parts = new ArrayList<ILogicalQueryPart>();
		parts.add(part);
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copies = LogicalQueryHelper.copyAndCutQueryParts(parts, 1);
		ILogicalQueryPart copy=null;
		for(ILogicalQueryPart iterator : copies.get(part)) {
			copy = iterator;
		}
		relinkQueryPart(copy);

		LOG.debug("Copy created.");
	}

	private void relinkQueryPart(ILogicalQueryPart part) {
	
	}
	
	

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		@SuppressWarnings("unused")
		LoadBalancingMessage lbMessage = (LoadBalancingMessage)message;

		LOG.debug("Msg received.");	
		
	}
	
	public void sendCopyMessage(PeerID destinationPeerId) {
		try {
			if(peerCommunicator != null) {
				peerCommunicator.send(destinationPeerId, new LoadBalancingMessage(messageTypes.LB_Ack));
			}
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send Message");
		}
	}

}