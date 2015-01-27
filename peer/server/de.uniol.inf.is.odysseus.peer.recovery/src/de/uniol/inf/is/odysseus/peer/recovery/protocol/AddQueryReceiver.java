package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
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

	// Maximum number of threads in the thread pool.
	private final static int NUM_THREADS = 4;

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

	/**
	 * The query part controller, if there is one bound.
	 */
	private static Optional<IQueryPartController> cController = Optional.absent();

	/**
	 * Binds a query part controller. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param controller
	 *            The query part controller to bind. <br />
	 *            Must be not null.
	 */
	public static void bindController(IQueryPartController controller) {

		Preconditions.checkNotNull(controller, "The query part controller to bind must be not null!");
		cController = Optional.of(controller);
		LOG.debug("Bound {} as a query part controller.", controller.getClass().getSimpleName());

	}

	/**
	 * Unbinds a query part controller. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param controller
	 *            The query part controller to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindController(IQueryPartController controller) {

		Preconditions.checkNotNull(controller, "The query part controller to unbind must be not null!");
		if (cController.isPresent() && cController.get().equals(controller)) {

			cController = Optional.absent();
			LOG.debug("Unbound {} as a query part controller.", controller.getClass().getSimpleName());

		}

	}

	private static IBackupInformationAccess backupInformationAccess;

	public static void bindBackupInformationAccess(IBackupInformationAccess infoAccess) {
		backupInformationAccess = infoAccess;
	}

	public static void unbindBackupInformationAccess(IBackupInformationAccess infoAccess) {
		if (backupInformationAccess == infoAccess) {
			backupInformationAccess = null;
		}
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
				addQuery(addMessage.getPQLCode(), addMessage.getLocalQueryId(), addMessage.getQueryState(),
						addMessage.getSharedQueryId(), addMessage.isMaster(), addMessage.getMasterId(),
						addMessage.getFailedPeerId());
				response = new RecoveryAddQueryResponseMessage(addMessage.getUUID());
			} catch (Exception e) {
				LOG.error("Could not install query from add query message!", e);
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
	 * Adds a query (installs and runs it) and saves the new backup-information.
	 * 
	 * @param pql
	 *            The PQL String to install
	 * @param sharedQueryId
	 *            The id of the shared query where this PQL belongs to
	 */
	private static void addQuery(String pql, int localQueryId, QueryState queryState, ID sharedQuery, boolean master,
			PeerID masterPeer, PeerID failedPeerId) throws Exception {
		Preconditions.checkNotNull(pql);

		if (!cExecutor.isPresent()) {
			throw new IllegalArgumentException("No executor bound!");
		} else if (!cP2PNetworkManager.isPresent()) {
			throw new IllegalArgumentException("No P2P network manager bound!");
		} else if (!cRecoveryCommunicator.isPresent()) {
			throw new IllegalArgumentException("No recovery communicator bound!");
		}

		if (pql.contains(failedPeerId.toString())) {
			// Well, we have to install something that probably doesn't work, because probably a JxtaSender or
			// JxtaReceier want to connect to the failed peer
			// If we assume, that we get the other half of the JxtaSender - JxtaReceiver - pair, too, we can just insert
			// our own peerId instead of the failed one
			// IF we don't get the other half, the recovery-leading peer will tell us to change the sender or receiver
			// to a new peerId so that this case should work, too.
			// Attention: Just experimental for now (T.B.)
			String localPeerId = cP2PNetworkManager.get().getLocalPeerID().toString();
			pql = pql.replaceAll(failedPeerId.toString(), localPeerId);
			LOG.debug("Replaced peerId of failed peer in PQL to install with my own peerId. (failed peer was {})", failedPeerId.toString());

		}
		Collection<Integer> installedQueries = RecoveryHelper.installAndRunQueryPartFromPql(pql, queryState);
		if (installedQueries == null || installedQueries.size() == 0) {
			throw new IllegalArgumentException("Installing QueryPart on Peer failed. Searching for other peers.");
		}

		LOG.debug("Installed query for recovery.");
		if (master && sharedQuery != null) {
			Collection<PeerID> otherPeers = determineOtherPeers(sharedQuery);
			// TODO I think this is not correct. The "localQueryId" is the localQueryId on the OLD peer, not in this
			// peer (T.B.)
			cController.get().registerAsMaster(
					cExecutor.get().getLogicalQueryById(installedQueries.iterator().next(),
							RecoveryCommunicator.getActiveSession()), localQueryId, sharedQuery, otherPeers);
		} else if (sharedQuery != null) {
			cController.get().registerAsSlave(installedQueries, sharedQuery, masterPeer);
		}

		ExecutorService service = Executors.newFixedThreadPool(NUM_THREADS);

		// Call "receiveFromNewPeer" on the subsequent receiver so that that
		// peer creates a socket-connection to us
		IServerExecutor executor = cExecutor.get();
		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			if (installedQueries.contains(query.getID())) {
				for (IPhysicalOperator operator : query.getAllOperators()) {
					if (operator instanceof JxtaSenderPO) {
						// I installed a sender
						JxtaSenderPO<?> sender = (JxtaSenderPO<?>) operator;

						// For this sender we want to get the peer to which
						// it sends to update the receiver on the other peer

						PeerID peer = RecoveryHelper.convertToPeerId(sender.getPeerIDString());
						// To this peer we have to send an "UPDATE_RECEIVER" message
						PipeID pipe = RecoveryHelper.convertToPipeId(sender.getPipeIDString());
						PeerID ownPeerId = cP2PNetworkManager.get().getLocalPeerID();

						if (peer != null && pipe != null) {
							final PeerID finalPeer = peer;
							final PeerID finalOwnPeer = ownPeerId;
							final PipeID finalPipe = pipe;
							final int finalLocalQueryId = localQueryId;

							service.submit(new Runnable() {
								public void run() {
									// I want to tell the sender on the other side that he has to update his peerId he
									// receives from
									cRecoveryCommunicator.get().sendUpdateReceiverMessage(finalPeer, finalOwnPeer,
											finalPipe, finalLocalQueryId);

								}
							});
							// We will, in every case, change the backup-information of that other peer. The
							// other one will do this, too, if he still exists. This is for the case, that the
							// other peer was the failed peer and can't update the JxtaReceiverPO or the
							// backup-information.
							backupInformationAccess.updateBackupInfoForPipe(failedPeerId.toString(),
									finalPipe.toString());

						}

					} else if (operator instanceof JxtaReceiverPO) {
						// I installed a receiver
						JxtaReceiverPO<?> receiver = (JxtaReceiverPO<?>) operator;

						PeerID peer = RecoveryHelper.convertToPeerId(receiver.getPeerIDString());
						PipeID pipe = RecoveryHelper.convertToPipeId(receiver.getPipeIDString());

						PeerID ownPeerId = cP2PNetworkManager.get().getLocalPeerID();

						if (peer != null && pipe != null) {

							final PeerID finalPeer = peer;
							final PeerID finalOwnPeer = ownPeerId;
							final PipeID finalPipe = pipe;
							final int finalLocalQueryId = localQueryId;

							service.submit(new Runnable() {
								public void run() {
									// I want to tell the sender on the other side that he has to update his peerId he
									// sends to
									cRecoveryCommunicator.get().sendUpdateSenderMessage(finalPeer, finalOwnPeer,
											finalPipe, finalLocalQueryId);
								}
							});
							service.submit(new Runnable() {
								public void run() {
									// And now he can GO ON
									cRecoveryCommunicator.get().sendGoOnMessage(finalPeer, finalPipe);
								}
							});
						}
					}
				}
			}
		}
	}

	private static Collection<PeerID> determineOtherPeers(ID sharedQuery) {
		final PeerID ownID = cP2PNetworkManager.get().getLocalPeerID();
		final Collection<PeerID> otherPeers = Lists.newArrayList();
		for (String peerID_str : backupInformationAccess.getBackupPeerIds()) {
			PeerID id = null;
			try {
				final URI uri = new URI(peerID_str);
				id = PeerID.create(uri);
			} catch (URISyntaxException | ClassCastException ex) {
				LOG.error("Could not get id from text {}", peerID_str, ex);
				continue;
			}
			if (ownID.equals(id)) {
				continue;
			}
			Map<Integer, BackupInfo> infos = backupInformationAccess.getBackupInformation(peerID_str);
			for (int localId : infos.keySet()) {
				String sharedQuery_str = infos.get(localId).sharedQuery;
				if (sharedQuery.toString().equals(sharedQuery_str)) {
					otherPeers.add(id);
					break;
				}
			}
		}
		return otherPeers;
	}

}