package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAbortMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAddQueryMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingCopyConnectionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingDeleteConnectionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingDeleteQueryMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingFailureMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInitiateCopyMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInitiateMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInstallingSuccessfulMessage;

public class LoadBalancingCommunicationListener implements
		IPeerCommunicatorListener, ILoadBalancingCommunicator {
	
	/**
	 * 
	 * @author Carsten Cordes Class to encapsulate a Connection to an operator
	 *         on another Peer.
	 * 
	 */
	

	/**
	 * Constants
	 */
	public static final int LOOK_FOR_QUERY_FAILED = -1;

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingCommunicationListener.class);

	/**
	 * List of registered Listeners.
	 */
	private ArrayList<ILoadBalancingListener> listeners = new ArrayList<ILoadBalancingListener>();

	/**
	 * Executor to get queries
	 */
	private static IServerExecutor executor;

	/**
	 * Peer Communicator
	 */
	private static IPeerCommunicator peerCommunicator;

	/**
	 * Network Manager (needed to get GroupID, etc.)
	 */
	private static IP2PNetworkManager p2pNetworkManager;

	/**
	 * Instance of Communication Listener.
	 */
	private static LoadBalancingCommunicationListener instance;

	/**
	 * Active Session. Use getActiveSession() to avoid null pointers.
	 */
	private static ISession activeSession;

	/**
	 * Get Instance of Communication Listener
	 * 
	 * @return this.
	 */
	public static LoadBalancingCommunicationListener getInstance() {
		return instance;
	}

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
		instance = this;
	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		instance = null;
	}

	/**
	 * called by OSGi-DS to bind Executor
	 * 
	 * @param exe
	 *            Executor to bind.
	 */
	public static void bindExecutor(IExecutor exe) {
		LOG.debug("Bound Executor.");
		executor = (IServerExecutor) exe;
	}

	/**
	 * called by OSGi-DS to unbind Executor
	 * 
	 * @param exe
	 *            Executor to unbind.
	 */
	public static void unbindExecutor(IExecutor exe) {
		LOG.debug("Unbound Executor.");
		if (executor == exe) {
			executor = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Network Manager
	 * 
	 * @param serv
	 *            Networkmanager to bind.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		LOG.debug("Bound network Manager");
		p2pNetworkManager = serv;
	}

	/**
	 * called by OSGi-DS to unbind Network Manager
	 * 
	 * @param serv
	 *            Networkmanager to unbind.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		LOG.debug("Unbound NetworkMananger");
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Peer Communicator (registers all Messages and
	 * adds Listeners)
	 * 
	 * @param serv
	 *            PeerCommunicator to bind.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Bound Peer Communicator.");
		peerCommunicator = serv;
		peerCommunicator
				.registerMessageType(LoadBalancingInitiateCopyMessage.class);
		peerCommunicator
				.registerMessageType(LoadBalancingInitiateMessage.class);
		peerCommunicator
				.registerMessageType(LoadBalancingCopyConnectionMessage.class);
		peerCommunicator
				.registerMessageType(LoadBalancingAddQueryMessage.class);
		peerCommunicator
				.registerMessageType(LoadBalancingDeleteConnectionMessage.class);
		peerCommunicator
				.registerMessageType(LoadBalancingDeleteQueryMessage.class);
		peerCommunicator.registerMessageType(LoadBalancingFailureMessage.class);
		peerCommunicator.registerMessageType(LoadBalancingAbortMessage.class);

		peerCommunicator.addListener(this,
				LoadBalancingInitiateCopyMessage.class);
		peerCommunicator.addListener(this, LoadBalancingInitiateMessage.class);
		peerCommunicator.addListener(this,
				LoadBalancingCopyConnectionMessage.class);
		peerCommunicator.addListener(this, LoadBalancingAddQueryMessage.class);
		peerCommunicator.addListener(this,
				LoadBalancingDeleteConnectionMessage.class);
		peerCommunicator.addListener(this,
				LoadBalancingDeleteQueryMessage.class);
		peerCommunicator.addListener(this, LoadBalancingAbortMessage.class);
		peerCommunicator.addListener(this, LoadBalancingFailureMessage.class);
	}

	/**
	 * called by OSGi-DS to unbind Peer Communicator (unregisters all Messages
	 * and removes Listeners)
	 * 
	 * @param serv
	 *            Peer Communicator to unbind.
	 * 
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Unbound Peer Communicator.");
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this,
					LoadBalancingInitiateCopyMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingInitiateMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingCopyConnectionMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingAddQueryMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingDeleteConnectionMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingDeleteQueryMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingFailureMessage.class);
			peerCommunicator.removeListener(this,
					LoadBalancingAbortMessage.class);

			peerCommunicator
					.unregisterMessageType(LoadBalancingInitiateCopyMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingInitiateMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingCopyConnectionMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingAddQueryMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingDeleteConnectionMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingDeleteQueryMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingFailureMessage.class);
			peerCommunicator
					.unregisterMessageType(LoadBalancingAbortMessage.class);

			peerCommunicator = null;
		}
	}

	/**
	 * Removes a query from current Peer.
	 * 
	 * @param queryId
	 */
	public void deleteQuery(int queryId) {
		executor.removeQuery(queryId, getActiveSession());
	}
	
	

	@Override
	/**
	 * called when a registered Message is received.
	 * Implements basic LoadBalancing Protokoll.
	 * @param communicator Peer Communicator
	 * @param senderPeer Peer sending the Message
	 * @param message Received Message.
	 */
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {

		// TODO Correct Error Handling
		// TODO Improve protocol.

		/*
		 * 1. Step: Initiating Peer send LoadBalancingInitiateMessage to chosen Peer.
		 * (Happens in Allocator)
		 */
		
		/*
		 * 2. Step: Chosen Peer answers with LoadBalancingInitiateCopyMessage to signalize it's ready.
		 */
		if (message instanceof LoadBalancingInitiateMessage) {
			sendCopyMessage(senderPeer,
					((LoadBalancingInitiateMessage) message)
							.getLoadBalancingProcessId());
		}

		//TODO Timeout between Step 2 and 3?
		
		/*
		 * 3. Step: Initiating receives LoadBalancingInitiateCopyMessage and copies and relinks the QueryPart.
		 * Send modified QueryPart to volunteering Peer.
		 */
		if (message instanceof LoadBalancingInitiateCopyMessage) {
			
			int loadBalancingProcessId = ((LoadBalancingInitiateCopyMessage) message)
					.getLoadBalancingProcessId();
			
			LoadBalancingStatus status = LoadBalancingStatusCache.getInstance().getStatus(loadBalancingProcessId);
			
			
			
			ILogicalQueryPart modifiedQueryPart = copyQueryPart(loadBalancingProcessId);
			HashMap<String,String> replacedPipes = LoadBalancingHelper.relinkQueryPart(p2pNetworkManager, modifiedQueryPart,senderPeer);
			
			status.setReplacedPipes(replacedPipes);
			status.setPhase(LoadBalancingStatus.LB_PHASES.copying);
			status.setModifiedPart(modifiedQueryPart);
			
			String pqlFromQueryPart = LogicalQueryHelper
					.generatePQLStatementFromQueryPart(modifiedQueryPart);
			LoadBalancingMessageDispatcher.sendAddQueryPartMessage(peerCommunicator, loadBalancingProcessId, senderPeer,
					pqlFromQueryPart);
			
		}
		

		/*
		 * 4. Step: Vounteering peer receives new QueryPart and try to install
		 * Send Ack if ok, send Failure if not ok.
		 */
		if (message instanceof LoadBalancingAddQueryMessage) {
			try {
				LoadBalancingAddQueryMessage queryMessage = (LoadBalancingAddQueryMessage) message;
				LoadBalancingHelper.installAndRunQueryPartFromPql(executor, getActiveSession(), Context.empty(),queryMessage.getPqlQuery());
				//TODO Send success
			}
			catch(Exception e) {
				//TODO Send failure
			}
		}
		
		/*
		 * 5. Step: Initiating Peer receives InstallingQueryPartSuccessful or LoadBalancingFailedMessage
		 * 6. Step: Initiating Peer send CopyConnectionMessages to all incoming and outgoing peers in QueryPart.
		 */
		if(message instanceof LoadBalancingInstallingSuccessfulMessage) {
			int lbProcessId = ((LoadBalancingInstallingSuccessfulMessage) message).getLoadBalancingProcessId();
			
			LoadBalancingStatus status = LoadBalancingStatusCache.getInstance().getStatus(lbProcessId);
			
			LoadBalancingStatusCache.getInstance().setPhase(lbProcessId, LoadBalancingStatus.LB_PHASES.relinking);
			notifyOutgoingAndIncomingPeers(status);
		}
		
		
		
		/*
		 * 7. Step Incoming and outgoing Peers receive CopyConnectionMessage and act accordingly.
		 * They send an Ack or a Failure Message.
		 */

		// Peer should now copy an existing JxtaSender or Receiver.
		if (message instanceof LoadBalancingCopyConnectionMessage) {
					LoadBalancingCopyConnectionMessage copyMessage = (LoadBalancingCopyConnectionMessage) message;
					LoadBalancingHelper.findAndCopyLocalJxtaOperator(executor,peerCommunicator, getActiveSession(),copyMessage.isSender(),
					copyMessage.getNewPeerId(), copyMessage.getOldPipeId(),
					copyMessage.getNewPipeId(),
					copyMessage.getLoadBalancingProcessId());
		}
		
		/*
		 * Step 8. Initiating Peer received all Ack Messages? -> Ok
		 * If not... Try again and if that fails Abort. (Can probably not do anything here)
		 */

		
		/*
		 * Step 9. Receiving SyncComlete Message -> Delete old Connection.
		 */
		// Peer can now delete old Connection.
		if (message instanceof LoadBalancingDeleteConnectionMessage) {
			LoadBalancingDeleteConnectionMessage deleteConnectionMessage = (LoadBalancingDeleteConnectionMessage) message;
			if (deleteConnectionMessage.isSender()) {
				deleteDeprecatedSender(deleteConnectionMessage.getOldPipeId());
			} else {
				LoadBalancingHelper.deleteDeprecatedReceiver(executor,deleteConnectionMessage.getOldPipeId());
			}
		}

		// Peer can now delete deprecated Query.
		if (message instanceof LoadBalancingDeleteQueryMessage) {
			LoadBalancingDeleteQueryMessage deleteQueryMessage = (LoadBalancingDeleteQueryMessage) message;
			int loadBalancingProcessId = deleteQueryMessage
					.getLoadBalancingProcessId();
			LoadBalancingStatusCache queryCache = LoadBalancingStatusCache
					.getInstance();
			ILogicalQueryPart queryPart = queryCache
					.getOriginalQueryPart(loadBalancingProcessId);
			if (!queryPart.getOperators().asList().isEmpty()) {

				// Tell Receivers and Senders to delete duplicate Connections.
				for (ILogicalOperator operator : queryPart.getOperators()) {
					if (operator instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
						LoadBalancingMessageDispatcher.sendDeleteJxtaOperatorMessage(peerCommunicator,false,
								receiver.getPeerID(), receiver.getPipeID(),
								deleteQueryMessage.getLoadBalancingProcessId());
					}
					if (operator instanceof JxtaSenderAO) {
						JxtaSenderAO sender = (JxtaSenderAO) operator;
						LoadBalancingMessageDispatcher.sendDeleteJxtaOperatorMessage(peerCommunicator,false,
								sender.getPeerID(), sender.getPipeID(),
								deleteQueryMessage.getLoadBalancingProcessId());
					}
				}
			}
			
			int queryId = LoadBalancingStatusCache.getInstance().getStatus(loadBalancingProcessId).getLogicalQuery();
			deleteQuery(queryId);
		}

	}

	private void notifyOutgoingAndIncomingPeers(LoadBalancingStatus status) {
		
		HashMap<String, String> replacedPipes = status.getReplacedPipes();
		int lbProcessId = status.getProcessId();
		ILogicalQueryPart modifiedQueryPart = status.getModifiedPart();
		String volunteeringPeer = status.getVolunteeringPeer().toString();
		
		for (ILogicalOperator operator : modifiedQueryPart.getOperators()) {
			if(operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				LoadBalancingMessageDispatcher.sendCopyConnectionMessage(peerCommunicator,true,lbProcessId,LoadBalancingHelper.toPeerID(sender.getPeerID()),replacedPipes.get(sender.getPipeID()),sender.getPipeID(),volunteeringPeer);
			}
			if(operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO)operator;
				LoadBalancingMessageDispatcher.sendCopyConnectionMessage(peerCommunicator, true,lbProcessId,LoadBalancingHelper.toPeerID(receiver.getPeerID()),replacedPipes.get(receiver.getPipeID()),receiver.getPipeID(),volunteeringPeer);
			}
		}
		
	}

	/**
	 * Deletes deprecated JxtaSenderPO.
	 * 
	 * @param oldPipeId
	 */
	private void deleteDeprecatedSender(String oldPipeId) {
		JxtaSenderPO<?> physicalJxtaOperator = (JxtaSenderPO<?>) LoadBalancingHelper.getPhysicalJxtaOperator(executor,
				true, oldPipeId);
		if (physicalJxtaOperator != null) {
			// Sender always have one input port
			physicalJxtaOperator.done(0);
			physicalJxtaOperator.unsubscribeFromAllSources();
		}
	}
	
	
	private ILogicalQueryPart copyQueryPart(int lbProcessId) {
		ILogicalQueryPart part = LoadBalancingStatusCache.getInstance()
				.getOriginalQueryPart(lbProcessId);
		ILogicalQueryPart copy = LoadBalancingHelper.getCopyOfQueryPart(part);
		return copy;
	}
	

	/**
	 * Sends a copy Message to initiating Peer, this message initiates a copy
	 * process for given LoadBalancing Process. This should be an answer to the
	 * initiating peer, after being accepted as loadbalancing partner. (Ack
	 * Message)
	 * 
	 * @param destinationPeerId
	 *            initiating Peer
	 * @param loadBalancingProcessId
	 *            Peer-unique ID which identifies the referenced LoadBalancing
	 *            Process.
	 * 
	 */
	public void sendCopyMessage(PeerID destinationPeerId,
			int loadBalancingProcessId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator.send(destinationPeerId,
						new LoadBalancingInitiateCopyMessage(
								loadBalancingProcessId));
			}
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send Message");
		}
	}

	


	/**
	 * Initiates the copy Process between a Peer and another Peer (after
	 * Allocation)
	 * 
	 * @param otherPeer
	 *            Peer which should receive a QueryPart
	 * @param queryPartID
	 *            queryId of QueryPart to copy.
	 */
	@Override
	public void initiateLoadBalancing(PeerID otherPeer, int queryId) {
		ILogicalQueryPart partToCopy = LoadBalancingHelper.getInstalledQueryPart(executor,getActiveSession(),queryId);
		int lbProcessIdentifier = LoadBalancingStatusCache.getInstance().createNewProcess(partToCopy);
		LoadBalancingStatusCache.getInstance().getStatus(lbProcessIdentifier).setLogicalQuery(queryId);
		LoadBalancingMessageDispatcher.sendInitiateLoadBalancingMessage(peerCommunicator,otherPeer, lbProcessIdentifier);
	}

	/**
	 * Gets currently active Session.
	 * 
	 * @return active Session
	 */
	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}


	/**
	 * Adds a LoadBalancing Listener
	 */
	@Override
	public void registerLoadBalancingListener(ILoadBalancingListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a Load Balancing Listener
	 */
	@Override
	public void removeLoadBalancingListener(ILoadBalancingListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}
	
	

}