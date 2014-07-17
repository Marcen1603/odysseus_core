package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingAddQueryMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingCopyConnectionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingDeleteConnectionMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingDeleteQueryMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInitiateCopyMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages.LoadBalancingInitiateMessage;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingSynchronizerPO;

public class LoadBalancingCommunicationListener implements
		IPeerCommunicatorListener, ILoadBalancingCommunicator {

	/**
	 * 
	 * @author Carsten Cordes Class to encapsulate a Connection to an operator
	 *         on another Peer.
	 * 
	 */
	private class ConnectionToOperator {

		/**
		 * Constructor
		 * 
		 * @param operator
		 *            Operator-sided Endpoint of the connection
		 * @param remotePeerID
		 *            Peer to connect to
		 * @param oldPipeID
		 *            oldPipe Id, which should be replaced
		 * @param port
		 *            connection port (source-out or sink-in depends on context)
		 * @param schema
		 *            connection schema
		 */
		ConnectionToOperator(ILogicalOperator operator, String remotePeerID,
				String oldPipeID, int port, SDFSchema schema) {
			this.localOperator = operator;
			this.remotePeerID = remotePeerID;
			this.oldPipeID = oldPipeID;
			this.port = port;
			this.schema = schema;
		}

		private String remotePeerID;
		private ILogicalOperator localOperator;
		private String oldPipeID;
		private int port;
		private SDFSchema schema;
	};

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

			peerCommunicator = null;
		}
	}

	/**
	 * Removes a query from current Peer.
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
		
		// TODO Order of Messages
		// TODO Correct Error Handling
		// TODO Improve protocol.

		//If Peer is asked -> answer (Ack)
		if (message instanceof LoadBalancingInitiateMessage) {
			// TODO Check if loadBalancing ok.
			sendCopyMessage(senderPeer,
					((LoadBalancingInitiateMessage) message)
							.getLoadBalancingProcessId());
		}

		//When Peer gets this message it starts copying a queryPart.
		if (message instanceof LoadBalancingInitiateCopyMessage) {
			int loadBalancingProcessId = ((LoadBalancingInitiateCopyMessage) message)
					.getLoadBalancingProcessId();
			ILogicalQueryPart part = LoadBalancingQueryCache.getInstance()
					.getQueryPart(loadBalancingProcessId);
			ILogicalQueryPart copy = getCopyOfQueryPart(part);
			relinkQueryPart(loadBalancingProcessId, copy, senderPeer.toString());
			String pqlFromQueryPart = LogicalQueryHelper
					.generatePQLStatementFromQueryPart(copy);
			LOG.debug("Created PQL from Query Part: " + pqlFromQueryPart);
			sendAddQueryPartMessage(loadBalancingProcessId, senderPeer,
					pqlFromQueryPart);
			
		}

		//Peer should install Query Now.
		if (message instanceof LoadBalancingAddQueryMessage) {
			LoadBalancingAddQueryMessage queryMessage = (LoadBalancingAddQueryMessage) message;
			installAndRunQueryPartFromPql(queryMessage.getPqlQuery());
			// TODO Send Ack Or Failure Message.
		}

		//Peer should now copy an existing JxtaSender or Receiver.
		if (message instanceof LoadBalancingCopyConnectionMessage) {
			LoadBalancingCopyConnectionMessage copyMessage = (LoadBalancingCopyConnectionMessage) message;
			findAndCopyLocalJxtaOperator(copyMessage.isSender(),
					copyMessage.getNewPeerId(), copyMessage.getOldPipeId(),
					copyMessage.getNewPipeId(),copyMessage.getLoadBalancingProcessId());
		}

		//Peer can now delete old Connection.
		if (message instanceof LoadBalancingDeleteConnectionMessage) {
			LoadBalancingDeleteConnectionMessage deleteConnectionMessage = (LoadBalancingDeleteConnectionMessage) message;
			if (deleteConnectionMessage.isSender()) {
				deleteDeprecatedSender(deleteConnectionMessage.getOldPipeId());
			} else {
				deleteDeprecatedReceiver(deleteConnectionMessage.getOldPipeId());
			}
		}

		//Peer can now delete deprecated Query.
		if (message instanceof LoadBalancingDeleteQueryMessage) {
			LoadBalancingDeleteQueryMessage deleteQueryMessage = (LoadBalancingDeleteQueryMessage) message;
			int loadBalancingProcessId = deleteQueryMessage
					.getLoadBalancingProcessId();
			LoadBalancingQueryCache queryCache = LoadBalancingQueryCache
					.getInstance();
			ILogicalQueryPart queryPart = queryCache
					.getQueryPart(loadBalancingProcessId);
			if (!queryPart.getOperators().asList().isEmpty()) {
				
				//Tell Receivers and Senders to delete duplicate Connections.
				for(ILogicalOperator operator : queryPart.getOperators()) {
					if(operator instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO)operator;
						sendDeleteJxtaOperatorMessage(false,receiver.getPeerID(), receiver.getPipeID(),deleteQueryMessage.getLoadBalancingProcessId());
					}
					if(operator instanceof JxtaSenderAO) {
						JxtaSenderAO sender = (JxtaSenderAO)operator;
						sendDeleteJxtaOperatorMessage(false,sender.getPeerID(), sender.getPipeID(),deleteQueryMessage.getLoadBalancingProcessId());
					}
				}
				
				
				int queryId = getQueryWithOperator(queryPart.getOperators()
						.asList().get(0));
				if (queryId != LOOK_FOR_QUERY_FAILED) {
					deleteQuery(queryId);
				} else {
					// TODO Log fail
				}
			} else {
				// TODO Log fail
			}
		}

	}
	
	/**
	 * Deletes deprecated JxtaSenderPO.
	 * @param oldPipeId
	 */
	private void deleteDeprecatedSender(String oldPipeId) {
		JxtaSenderPO<?> physicalJxtaOperator = (JxtaSenderPO<?>) getPhysicalJxtaOperator(
				true, oldPipeId);
		if (physicalJxtaOperator != null) {
			// Sender always have one input port
			physicalJxtaOperator.done(0);
			physicalJxtaOperator.unsubscribeFromAllSources();
		}
	}

	/**
	 * Deletes Deprecated jxtaReceiverPO.
	 * @param oldPipeId
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void deleteDeprecatedReceiver(String oldPipeId) {
		JxtaReceiverPO<?> physicalJxtaOperator = (JxtaReceiverPO<?>) getPhysicalJxtaOperator(
				false, oldPipeId);
		if (physicalJxtaOperator != null) {
			PhysicalSubscription<?> receiverSubscription = physicalJxtaOperator
					.getSubscriptions().get(0);

			physicalJxtaOperator.unsubscribeFromAllSinks();

			if (receiverSubscription.getTarget() instanceof LoadBalancingSynchronizerPO) {
				LoadBalancingSynchronizerPO syncPO = (LoadBalancingSynchronizerPO<?>) receiverSubscription
						.getTarget();
				
				
				// Sync Operator has max 2 sources. so if there is another
				// receiver the size of the list need to
				// be 1, because the old receiver has already been removed
				if (!syncPO.getSubscribedToSource().isEmpty()
						&& syncPO.getSubscribedToSource().size() == 1) {
					PhysicalSubscription syncSourceSubscription = (PhysicalSubscription) syncPO
							.getSubscribedToSource().get(0);

					if (syncSourceSubscription.getTarget() instanceof JxtaReceiverPO) {
						JxtaReceiverPO<?> newReceiver = (JxtaReceiverPO<?>) syncSourceSubscription
								.getTarget();
						newReceiver.block();
						newReceiver.unsubscribeFromAllSinks();

						List<PhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = syncPO
								.getSubscriptions();

						for (PhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {

							syncPO.unsubscribeSink(subscription);

							newReceiver.subscribeSink(subscription.getTarget(),
									subscription.getSinkInPort(),
									subscription.getSourceOutPort(),
									subscription.getSchema(), true,
									subscription.getOpenCalls());
						}
						newReceiver.unblock();
					}
				}

			}

		}
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
	 * Finds a JxtaOperator by pipeId, creates a physical Copy and connects both
	 * Operators to the stream.
	 * 
	 * @param isSender
	 *            determines if Operator to look for is a Sender or a receiver.
	 * @param newPeerId
	 *            PeerId which the new Operator is connected to.
	 * @param oldPipeId
	 *            Pipe Id of old Operator
	 * @param newPipeId
	 *            Pipe Id of new Operator.
	 * @Param lbProcessId
	 * 			  LoadBalancingProcessId.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void findAndCopyLocalJxtaOperator(boolean isSender,
			String newPeerId, String oldPipeId, String newPipeId, int lbProcessId) {
		ILogicalOperator operator = getLogicalJxtaOperator(isSender, oldPipeId);
		if (operator != null) {
			if (isSender) {
				JxtaSenderAO logicalSender = (JxtaSenderAO) operator;
				JxtaSenderAO copy = (JxtaSenderAO) logicalSender.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);
				try {
					JxtaSenderPO physicalCopy = new JxtaSenderPO(copy);
					JxtaSenderPO physicalOriginal = (JxtaSenderPO) getPhysicalJxtaOperator(
							isSender, oldPipeId);
					physicalCopy.setOutputSchema(physicalOriginal
							.getOutputSchema());

					PhysicalSubscription subscription = physicalOriginal
							.getSubscribedToSource(0);

					if (subscription.getTarget() instanceof AbstractPipe) {
						((AbstractPipe) subscription.getTarget())
								.subscribeSink(physicalCopy, 0,
										subscription.getSourceOutPort(),
										subscription.getSchema(), true,
										subscription.getOpenCalls());
					} else if (subscription.getTarget() instanceof AbstractSource) {
						((AbstractSource) subscription.getTarget())
								.subscribeSink(physicalCopy, 0,
										subscription.getSourceOutPort(),
										subscription.getSchema(), true,
										subscription.getOpenCalls());
					}
				} catch (DataTransmissionException e) {
					LOG.debug("Did not go as expected.");
				}

			} else {
				JxtaReceiverAO logicalReceiver = (JxtaReceiverAO) operator;
				JxtaReceiverAO copy = (JxtaReceiverAO) logicalReceiver.clone();
				copy.setPipeID(newPipeId);
				copy.setPeerID(newPeerId);
				copy.setSchema(logicalReceiver.getSchema());
				try {
					JxtaReceiverPO physicalCopy = new JxtaReceiverPO(copy);
					JxtaReceiverPO physicalOriginal = (JxtaReceiverPO) getPhysicalJxtaOperator(
							isSender, oldPipeId);
					physicalCopy.setOutputSchema(physicalOriginal
							.getOutputSchema());

					LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>> synchronizer = new LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>>();
					
					
					LoadBalancingFinishedListener listener = new LoadBalancingFinishedListener(toPeerID(physicalOriginal.getPeerIDString()),lbProcessId);
					synchronizer.addListener(listener);
					
					physicalOriginal.block();

					List<PhysicalSubscription<ISink<? super IStreamObject>>> subscriptionList = physicalOriginal
							.getSubscriptions();

					for (PhysicalSubscription<ISink<? super IStreamObject>> subscription : subscriptionList) {

						physicalOriginal.unsubscribeSink(subscription);
						synchronizer.subscribeSink(subscription.getTarget(),
								subscription.getSinkInPort(),
								subscription.getSourceOutPort(),
								subscription.getSchema(), true,
								subscription.getOpenCalls());
					}

					ArrayList<IPhysicalOperator> emptyCallPath = new ArrayList<IPhysicalOperator>();

					physicalOriginal.subscribeSink(synchronizer, 0, 0,
							physicalOriginal.getOutputSchema());

					physicalOriginal.open(synchronizer, 0, 0, emptyCallPath,
							physicalOriginal.getOwner());
					physicalCopy.subscribeSink(synchronizer, 1, 0,
							physicalOriginal.getOutputSchema());
					physicalCopy.open(synchronizer, 0, 1, emptyCallPath,
							physicalOriginal.getOwner());

					physicalOriginal.unblock();
				} catch (DataTransmissionException e) {
					LOG.debug("Did not go as expected.");
				}
			}
		}

	}

	/**
	 * Installs and executes a Query from PQL.
	 * 
	 * @param pql
	 *            PQL to execute.
	 */
	private boolean installAndRunQueryPartFromPql(String pql) {
		//TODO better Error Handling.
		try {
			Collection<Integer> installedQueries = executor.addQuery(pql, "PQL",
					getActiveSession(), "Standard", Context.empty());
			for (int query : installedQueries) {
				executor.startQuery(query, getActiveSession());
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets a (logical) Copy of a single Query Part.
	 * 
	 * @param part
	 *            Part to copy.
	 * @return Copy of part.
	 */
	private ILogicalQueryPart getCopyOfQueryPart(ILogicalQueryPart part) {
		ILogicalQueryPart result = null;
		ArrayList<ILogicalQueryPart> partsList = new ArrayList<ILogicalQueryPart>();

		partsList.add(part);

		Map<ILogicalQueryPart, ILogicalQueryPart> copies = LogicalQueryHelper
				.copyQueryPartsDeep(partsList);

		for (Map.Entry<ILogicalQueryPart, ILogicalQueryPart> entry : copies
				.entrySet()) {
			result = entry.getKey();
		}

		return result;
	}

	/**
	 * Get all currently Installed Queries as Query Parts.
	 * 
	 * @return List of installed LogicalQueryParts.
	 */
	private Collection<ILogicalQueryPart> getInstalledQueryParts() {
		ArrayList<ILogicalQueryPart> parts = new ArrayList<ILogicalQueryPart>();
		for (int queryId : executor.getLogicalQueryIds(getActiveSession())) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId,
					getActiveSession());

			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			parts.add(new LogicalQueryPart(operators));
		}
		return parts;
	}

	/**
	 * Sends a delete ConnectionMessage to another Peer. (Removes duplicate JxtaOperators)
	 * @param isSender is JxtaOperator Sender?
	 * @param peerId Peer which holds Operator
	 * @param pipeId old Pipe ID (to delete)
	 * @param lbProcessId Load Balancing Process Id
	 */
	private void sendDeleteJxtaOperatorMessage(boolean isSender, String peerId, String pipeId, int lbProcessId) {
		LoadBalancingDeleteConnectionMessage message = new LoadBalancingDeleteConnectionMessage(lbProcessId, pipeId, isSender);
		
		if(peerCommunicator != null) {
			try {
				peerCommunicator.send(toPeerID(peerId), message);
			} catch (PeerCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	public void initiateLoadBalancing(PeerID otherPeer, int queryPartID) {
		ILogicalQueryPart partToCopy = null;
		for (ILogicalQueryPart part : getInstalledQueryParts()) {
			partToCopy = part;
		}
		int lbProcessIdentifier = LoadBalancingQueryCache.getInstance()
				.addQueryPartCopy(partToCopy);
		sendInitiateLoadBalancingMessage(otherPeer, lbProcessIdentifier);
	}

	/**
	 * Removes all TopAOs from a queryPart.
	 * 
	 * @param part
	 *            Part where TopAOs should be removed.
	 */
	private void removeTopAOs(ILogicalQueryPart part) {
		ArrayList<ILogicalOperator> toRemove = new ArrayList<>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof TopAO) {
				toRemove.add(operator);
			}
		}

		for (ILogicalOperator topAO : toRemove) {
			topAO.unsubscribeFromAllSinks();
			topAO.unsubscribeFromAllSources();
			part.removeOperator(topAO);
		}
	}

	/**
	 * Send Message to other peer with PQL to install.
	 * 
	 * @param loadBalancingProcessID
	 *            current LoadBalancing Process ID.
	 * @param destinationPeer
	 *            Receiving peer.
	 * @param queryPartPql
	 *            QueryPart as PQL.
	 */
	private void sendAddQueryPartMessage(int loadBalancingProcessID,
			PeerID destinationPeer, String queryPartPql) {
		LoadBalancingAddQueryMessage message = new LoadBalancingAddQueryMessage(
				loadBalancingProcessID, queryPartPql);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			LOG.debug("Communication excepting while sending query Part");
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to a Peer to copy a particular connection with a new Pipe
	 * and Peer ID.
	 * 
	 * @param isSender
	 *            determines, if a sender or a receiver should be duplicated
	 * @param loadBalancingProcessID
	 *            Load Balancing process id.
	 * @param destinationPeer
	 *            Message Destination.
	 * @param oldPipeId
	 *            Pipe Id of pipe which should be replicated
	 * @param newPipeId
	 *            Pipe Id which should substitute old Pipe Id
	 * @param newPeerId
	 *            Peer Id which should substitute old Peer Id
	 */
	private void sendCopyConnectionMessage(boolean isSender,
			int loadBalancingProcessID, PeerID destinationPeer,
			String oldPipeId, String newPipeId, String newPeerId) {
		LoadBalancingCopyConnectionMessage message = new LoadBalancingCopyConnectionMessage(
				loadBalancingProcessID, isSender, oldPipeId, newPipeId,
				newPeerId);
		try {
			peerCommunicator.send(destinationPeer, message);
		} catch (PeerCommunicationException e) {
			LOG.debug("Communication excepting while sending query Part");
			e.printStackTrace();
		}
	}

	/**
	 * Removes all JxtaReceivers from a QueryPart.
	 * 
	 * @param part
	 *            Part, where Receivers should be removed.
	 * @return Map with Logical Operators and their Connections to external
	 *         Operators.
	 */
	private Map<ILogicalOperator, Collection<ConnectionToOperator>> stripJxtaReceivers(
			ILogicalQueryPart part) {

		HashMap<ILogicalOperator, Collection<ConnectionToOperator>> result = new HashMap<ILogicalOperator, Collection<ConnectionToOperator>>();
		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaReceiverAO) {
				JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
				for (LogicalSubscription subscription : receiver
						.getSubscriptions()) {

					ILogicalOperator targetOperator = subscription.getTarget();
					if (!result.containsKey(targetOperator)) {
						result.put(targetOperator,
								new ArrayList<ConnectionToOperator>());
					}
					result.get(targetOperator).add(
							new ConnectionToOperator(targetOperator, receiver
									.getPeerID(), receiver.getPipeID(),
									subscription.getSinkInPort(), subscription
											.getSchema()));

				}
				toRemove.add(receiver);
			}
		}

		for (ILogicalOperator operator : toRemove) {
			operator.unsubscribeFromAllSinks();
			part.removeOperator(operator);
		}
		return result;
	}

	/**
	 * Removes all JxtaSenders from a QueryPart.
	 * 
	 * @param part
	 *            Part, where Senders should be removed.
	 * @return Map with Logical Operators and their Connections to external
	 *         Operators.
	 */
	private Map<ILogicalOperator, Collection<ConnectionToOperator>> stripJxtaSenders(
			ILogicalQueryPart part) {
		HashMap<ILogicalOperator, Collection<ConnectionToOperator>> result = new HashMap<ILogicalOperator, Collection<ConnectionToOperator>>();
		ArrayList<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof JxtaSenderAO) {
				JxtaSenderAO sender = (JxtaSenderAO) operator;
				for (LogicalSubscription subscription : sender
						.getSubscribedToSource()) {

					ILogicalOperator incomingOperator = subscription
							.getTarget();

					if (!result.containsKey(incomingOperator)) {
						result.put(incomingOperator,
								new ArrayList<ConnectionToOperator>());
					}
					result.get(incomingOperator).add(
							new ConnectionToOperator(incomingOperator, sender
									.getPeerID(), sender.getPipeID(),
									subscription.getSourceOutPort(),
									subscription.getSchema()));

				}
				toRemove.add(sender);
			}
		}

		for (ILogicalOperator operator : toRemove) {
			operator.unsubscribeFromAllSources();
			part.removeOperator(operator);
		}
		return result;
	}

	/**
	 * Send Message to initiate LoadBalancing with another Peer
	 * 
	 * @param volunteeringPeer
	 *            Peer that wants to take Load.
	 * @param loadBalancingProcessId
	 *            Peer unique Id referencing the Query Part which should be
	 *            given to volunteering peer.
	 */
	private void sendInitiateLoadBalancingMessage(PeerID volunteeringPeer,
			int loadBalancingProcessId) {
		try {
			if (peerCommunicator != null) {
				peerCommunicator
						.send(volunteeringPeer,
								new LoadBalancingInitiateMessage(
										loadBalancingProcessId));
			}
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send Message");
		}
	}

	/**
	 * Get Logical JxtaOperator (Sender or Receiver) by PipeID.
	 * 
	 * @param lookForSender
	 *            true if we look for a sender, false if we look for receiver.
	 * @param pipeID
	 *            Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	private ILogicalOperator getLogicalJxtaOperator(boolean lookForSender,
			String pipeID) {
		for (ILogicalQueryPart part : getInstalledQueryParts()) {
			for (ILogicalOperator operator : part.getOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderAO) {
						JxtaSenderAO sender = (JxtaSenderAO) operator;
						if (sender.getPipeID().equals(pipeID)) {
							return sender;
						}

					}
				} else {
					if (operator instanceof JxtaReceiverAO) {
						JxtaReceiverAO receiver = (JxtaReceiverAO) operator;
						if (receiver.getPipeID().equals(pipeID)) {
							return receiver;
						}

					}
				}

			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Get Physical JxtaOperator (Sender or Receiver) by PipeID.
	 * @param lookForSender true if we look for a sender, false if we look for receiver.
	 * @param pipeID Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	private IPhysicalOperator getPhysicalJxtaOperator(boolean lookForSender,
			String pipeID) {
		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			for (IPhysicalOperator operator : query.getAllOperators()) {
				if (lookForSender) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO sender = (JxtaSenderPO) operator;
						if (sender.getPipeIDString().equals(pipeID)) {
							return sender;
						}
					}
				} else {
					if (operator instanceof JxtaReceiverPO) {
						JxtaReceiverPO receiver = (JxtaReceiverPO) operator;
						if (receiver.getPipeIDString().equals(pipeID)) {
							return receiver;
						}
					}
				}
			}
		}
		return null;
	}

	
	/**
	 * Get Number of Query from executor which contains a particular Operator.
	 * @param operator Operator to look for
	 * @return Number of Query if found, LOOK_FOR_QUERY_FAILED else.
	 */
	private int getQueryWithOperator(ILogicalOperator operator) {
		for (int queryId : executor.getLogicalQueryIds(getActiveSession())) {
			ILogicalQuery query = executor.getLogicalQueryById(queryId,
					getActiveSession());
			ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			if (operators.contains(operator)) {
				return queryId;
			}
		}
		return LOOK_FOR_QUERY_FAILED;
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
	 * Converts a String to a peer ID.
	 * 
	 * @param peerIDString
	 *            String to convert
	 * @return PeerId (if it exists), null else.
	 */
	protected static PeerID toPeerID(String peerIDString) {
		try {
			final URI id = new URI(peerIDString);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not get id from peerIDString {}", peerIDString, ex);
			return null;
		}
	}

	/**
	 * Relinks a Query part to new Pipes and Peers.
	 * 
	 * @param lbProcessId
	 *            loadBalancing process Id.
	 * @param part
	 *            Part to relink.
	 * @param newPeerID
	 *            new Peer Id which should contain the Part.
	 */
	private void relinkQueryPart(int lbProcessId, ILogicalQueryPart part,
			String newPeerID) {
		removeTopAOs(part);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> incomingConnections = stripJxtaReceivers(part);
		Map<ILogicalOperator, Collection<ConnectionToOperator>> outgoingConnections = stripJxtaSenders(part);
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(part);
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSourcesOfLogicalQueryPart(part);

		for (ILogicalOperator relativeSource : relativeSources) {
			if (incomingConnections.containsKey(relativeSource)) {
				Collection<ConnectionToOperator> connections = incomingConnections
						.get(relativeSource);
				for (ConnectionToOperator connection : connections) {

					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();
					JxtaReceiverAO receiver = new JxtaReceiverAO();

					receiver.setPipeID(newPipeID);
					receiver.setPeerID(connection.remotePeerID);
					receiver.setSchema(connection.schema.getAttributes());
					receiver.connectSink(relativeSource, connection.port, 0,
							relativeSource.getInputSchema(0));
					sendCopyConnectionMessage(true, lbProcessId,
							toPeerID(connection.remotePeerID),
							connection.oldPipeID.toString(), newPipeID,
							newPeerID);
				}
			}
		}

		for (ILogicalOperator relativeSink : relativeSinks) {
			if (outgoingConnections.containsKey(relativeSink)) {
				Collection<ConnectionToOperator> connections = outgoingConnections
						.get(relativeSink);
				for (ConnectionToOperator connection : connections) {
					String newPipeID = IDFactory.newPipeID(
							p2pNetworkManager.getLocalPeerGroupID()).toString();

					JxtaSenderAO sender = new JxtaSenderAO();
					sender.setPeerID(connection.remotePeerID);
					sender.setPipeID(newPipeID);
					sender.setOutputSchema(connection.schema);
					connection.localOperator.connectSink(sender, 0,
							connection.port,
							connection.localOperator.getOutputSchema());
					sendCopyConnectionMessage(false, lbProcessId,
							toPeerID(connection.remotePeerID),
							connection.oldPipeID.toString(), newPipeID,
							newPeerID);
				}
			}
		}

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
		if(listeners.contains(listener))
			listeners.remove(listener);
	}

	/**
	 * Sends finished Mesage
	 * @param initiatingPeer Peer which should receive the message. (The one which initiated the Load Balancing)
	 * @param lbProcessId Load Balancing Process Id.
	 */
	public void sendLoadBalancingFinishedMessage(PeerID initiatingPeer,
			int lbProcessId) {
		LoadBalancingDeleteQueryMessage message = new LoadBalancingDeleteQueryMessage(lbProcessId);
		if(peerCommunicator != null) {
			try {
				peerCommunicator.send(initiatingPeer, message);
			} catch (PeerCommunicationException e) {
				// TODO Resend probably.
				e.printStackTrace();
			}
		}
	}


}