package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Collection;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAddQueryMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAddQueryResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * Entity to handle received query parts to add. <br />
 * Handles incoming query parts and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class AddQueryReceiver extends AbstractRepeatingMessageReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AddQueryReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static AddQueryReceiver cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link AddQueryReceiver}.
	 */
	public static AddQueryReceiver getInstance() {
		return cInstance;
	}

	/**
	 * The executor, if there is one bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an executor. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to bind. <br />
	 *            Must be not null.
	 */
	public static void bindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);
		cExecutor = Optional.of((IServerExecutor) serv);
		LOG.debug("Bound {} as an executor.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds an executor, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);

		if (cExecutor.isPresent() && cExecutor.get() == (IServerExecutor) serv) {

			cExecutor = Optional.absent();
			LOG.debug("Unbound {} as an executor.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional.absent();

	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to bind. <br />
	 *            Must be not null.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);
		cP2PNetworkManager = Optional.of(serv);
		LOG.debug("Bound {} as a P2P network manager.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a P2P network manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);

		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {

			cP2PNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cRecoveryCommunicator = Optional.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryCommunicator(IRecoveryCommunicator serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IRecoveryCommunicator);
		cRecoveryCommunicator = Optional.of((IRecoveryCommunicator) serv);
		LOG.debug("Bound {} as a recovery communicator.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryCommunicator(IRecoveryCommunicator serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IRecoveryCommunicator);

		if (cRecoveryCommunicator.isPresent() && cRecoveryCommunicator.get() == (IRecoveryCommunicator) serv) {

			cRecoveryCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", serv.getClass().getSimpleName());

		}

	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryAddQueryMessage.class);
		serv.addListener(this, RecoveryAddQueryMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryAddQueryMessage.class);
		serv.removeListener(this, RecoveryAddQueryMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof RecoveryAddQueryMessage) {
			RecoveryAddQueryMessage addMessage = (RecoveryAddQueryMessage) message;
			if (!mReceivedUUIDs.contains(addMessage.getUUID())) {
				mReceivedUUIDs.add(addMessage.getUUID());
			} else {
				return;
			}

			RecoveryAddQueryResponseMessage response = null;
			try {
				addQuery(addMessage.getPQLCode(), addMessage.getLocalQueryId());
				response = new RecoveryAddQueryResponseMessage(addMessage.getUUID());
			} catch (Exception e) {
				response = new RecoveryAddQueryResponseMessage(addMessage.getUUID(), e.getMessage());
			}

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send add query response message!", e);
			}
		}
	}

	/**
	 * Adds a query (installs and runs it) and saves the new backup-information. If necessary, searches for a buddy
	 * 
	 * @param pql
	 *            The PQL String to install
	 * @param sharedQueryId
	 *            The id of the shared query where this PQL belongs to
	 */
	private static void addQuery(String pql, int localQueryId) throws Exception {
		Preconditions.checkNotNull(pql);

		if (!cExecutor.isPresent()) {
			throw new IllegalArgumentException("No executor bound!");
		} else if (!cP2PNetworkManager.isPresent()) {
			throw new IllegalArgumentException("No P2P network manager bound!");
		} else if (!cRecoveryCommunicator.isPresent()) {
			throw new IllegalArgumentException("No recovery communicator bound!");
		}

		Collection<Integer> installedQueries = RecoveryHelper.installAndRunQueryPartFromPql(pql);
		if (installedQueries == null || installedQueries.size() == 0) {
			throw new IllegalArgumentException("Installing QueryPart on Peer failed. Searching for other peers.");
		}

		LOG.debug("Installed query for recovery.");

		// Call "receiveFromNewPeer" on the subsequent receiver so that that
		// peer creates a socket-connection to us
		IServerExecutor executor = cExecutor.get();
		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			if (installedQueries.contains(query.getID())) {
				for (IPhysicalOperator operator : query.getAllOperators()) {
					if (operator instanceof JxtaSenderPO) {
						JxtaSenderPO<?> sender = (JxtaSenderPO<?>) operator;

						// For this sender we want to get the peer to which
						// it sends to update the receiver on the other peer

						PeerID peer = RecoveryHelper.convertToPeerId(sender.getPeerIDString());
						// To this peer we have to send an "UPDATE_RECEIVER"
						// message
						PipeID pipe = RecoveryHelper.convertToPipeId(sender.getPipeIDString());
						PeerID ownPeerId = cP2PNetworkManager.get().getLocalPeerID();

						if (peer != null && pipe != null) {
							cRecoveryCommunicator.get().sendUpdateReceiverMessage(peer, ownPeerId, pipe, localQueryId);
						}

					} else if (operator instanceof JxtaReceiverPO) {
						// GO ON
						// -----
						JxtaReceiverPO<?> receiver = (JxtaReceiverPO<?>) operator;

						// For this receiver, we want to tell the sender that he
						// can go on
						PeerID peer = RecoveryHelper.convertToPeerId(receiver.getPeerIDString());
						PipeID pipe = RecoveryHelper.convertToPipeId(receiver.getPipeIDString());

						if (peer != null && pipe != null) {
							cRecoveryCommunicator.get().sendGoOnMessage(peer, pipe);
						}
					}
				}
			}
		}
	}

}