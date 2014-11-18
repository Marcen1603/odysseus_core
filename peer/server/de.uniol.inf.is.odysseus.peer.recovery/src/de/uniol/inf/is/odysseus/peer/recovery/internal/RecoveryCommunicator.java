package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionaryListener;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.message.RemoveQueryMessage;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.messages.BackupInformationMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAgreementMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryAgreementHandler;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryBackupMessageHandler;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.RecoveryInstructionHandler;
import de.uniol.inf.is.odysseus.peer.recovery.util.BuddyHelper;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;

/**
 * A recovery communicator handles the communication between peers for recovery mechanisms.
 * 
 * @author Tobias Brandt & Michael Brand & Simon Kuespert
 *
 */
public class RecoveryCommunicator implements IRecoveryCommunicator, IPeerCommunicatorListener, IPeerDictionaryListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryCommunicator.class);

	/**
	 * The list of sharedQueryIDs where we didn't find a buddy for, cause we were the last peer in the (known) network.
	 * If a new peer comes to the network, we will make him the buddy for us for these sharedQueryIds
	 */
	private List<ID> noBuddyList = new ArrayList<ID>();

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
	 * Gets the P2P network manager.
	 * 
	 * @return The bound P2P network manager or {@link Optional#absent()}, if there is none bound.
	 */
	public static Optional<IP2PNetworkManager> getP2PNetworkManager() {
		return cP2PNetworkManager;
	}

	/**
	 * The Peer dictionary, if there is one bound.
	 */
	private static Optional<IPeerDictionary> cPeerDictionary = Optional.absent();

	/**
	 * Binds a Peer dictionary. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to bind. <br />
	 *            Must be not null.
	 */
	public static void bindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);
		cPeerDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a Peer dictionary.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a Peer dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);

		if (cPeerDictionary.isPresent() && cPeerDictionary.get() == serv) {

			cPeerDictionary = Optional.absent();
			LOG.debug("Unbound {} as a Peer dictionary.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * Gets the Peer dictionary.
	 * 
	 * @return The bound Peer dictionary or {@link Optional#absent()}, if there is none bound.
	 */
	public static Optional<IPeerDictionary> getPeerDictionary() {

		return cPeerDictionary;

	}

	/**
	 * The peer communicator, if there is one bound.
	 */
	private static Optional<IPeerCommunicator> cPeerCommunicator = Optional.absent();

	/**
	 * Registers messages and listeners at the peer communicator.
	 */
	private void registerMessagesAndAddListeners() {

		Preconditions.checkArgument(cPeerCommunicator.isPresent());
		cPeerCommunicator.get().registerMessageType(RecoveryInstructionMessage.class);
		cPeerCommunicator.get().addListener(this, RecoveryInstructionMessage.class);
		cPeerCommunicator.get().registerMessageType(BackupInformationMessage.class);
		cPeerCommunicator.get().addListener(this, BackupInformationMessage.class);
		cPeerCommunicator.get().registerMessageType(RecoveryAgreementMessage.class);
		cPeerCommunicator.get().addListener(this, RecoveryAgreementMessage.class);
		cPeerCommunicator.get().registerMessageType(RemoveQueryMessage.class);
		cPeerCommunicator.get().addListener(this, RemoveQueryMessage.class);

	}

	/**
	 * Unregisters messages and listeners at the peer communicator.
	 */
	private void unregisterMessagesAndAddListeners() {

		Preconditions.checkArgument(cPeerCommunicator.isPresent());
		cPeerCommunicator.get().removeListener(this, RecoveryInstructionMessage.class);
		cPeerCommunicator.get().unregisterMessageType(RecoveryInstructionMessage.class);
		cPeerCommunicator.get().removeListener(this, BackupInformationMessage.class);
		cPeerCommunicator.get().unregisterMessageType(BackupInformationMessage.class);
		cPeerCommunicator.get().removeListener(this, RecoveryAgreementMessage.class);
		cPeerCommunicator.get().unregisterMessageType(RecoveryAgreementMessage.class);
		cPeerCommunicator.get().removeListener(this, RemoveQueryMessage.class);

	}

	/**
	 * Binds a peer communicator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The peer communicator to bind. <br />
	 *            Must be not null.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {

		Preconditions.checkNotNull(serv);
		cPeerCommunicator = Optional.of(serv);
		this.registerMessagesAndAddListeners();
		LOG.debug("Bound {} as a peer communicator.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a peer communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The peer communicator to unbind. <br />
	 *            Must be not null.
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {

		Preconditions.checkNotNull(serv);

		if (cPeerCommunicator.isPresent() && cPeerCommunicator.get() == serv) {

			this.unregisterMessagesAndAddListeners();
			cPeerCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a peer communicator.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * The recovery strategy manager, if there is one bound.
	 */
	private static Optional<IRecoveryStrategyManager> cRecoveryStrategyManager = Optional.absent();

	/**
	 * Binds a recovery strategy manager. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery strategy manager to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryStrategyManager(IRecoveryStrategyManager serv) {

		Preconditions.checkNotNull(serv);
		cRecoveryStrategyManager = Optional.of(serv);
		LOG.debug("Bound {} as a recovery strategy manager.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery strategy manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery strategy manager to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryStrategyManager(IRecoveryStrategyManager serv) {

		Preconditions.checkNotNull(serv);

		if (cRecoveryStrategyManager.isPresent() && cRecoveryStrategyManager.get() == serv) {

			cRecoveryStrategyManager = Optional.absent();
			LOG.debug("Unbound {} as a recovery strategy manager.", serv.getClass().getSimpleName());

		}

	}

	private static IPQLGenerator pqlGenerator;

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
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
	 * Gets the executor.
	 * 
	 * @return The bound executor or {@link Optional#absent()}, if there is none bound.
	 */
	public static Optional<IServerExecutor> getExecutor() {

		return cExecutor;

	}

	/**
	 * The active Session. <br />
	 * Only to be used by {@link #getActiveSession()}.
	 */
	private static ISession cActiveSession;

	/**
	 * Gets the currently active session.
	 */
	public static ISession getActiveSession() {

		if (cActiveSession == null || !cActiveSession.isValid()) {

			cActiveSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null,
					UserManagementProvider.getDefaultTenant().getName());

		}

		return cActiveSession;

	}

	@Override
	public void sendHoldOnMessage(PeerID peerToHoldOn, RecoveryInstructionMessage holdOnMessage) {
		sendMessage(peerToHoldOn, holdOnMessage);
	}

	// TODO Better way: allocate each single query part new. M.B.
	@Override
	public void installQueriesOnNewPeer(PeerID failedPeer, PeerID newPeer, ID sharedQueryId, String pql) {

		Preconditions.checkNotNull(failedPeer);
		Preconditions.checkNotNull(newPeer);
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(pql);

		if (!cPeerCommunicator.isPresent()) {

			LOG.error("No peer communicator bound!");
			return;

		}

		// Send the add query message
		RecoveryInstructionMessage takeOverMessage = RecoveryInstructionMessage.createAddQueryMessage(pql,
				sharedQueryId);
		sendMessage(newPeer, takeOverMessage);

		// Give this peer the backup-info from the peer which he recovers
		List<IRecoveryBackupInformation> infos = BuddyHelper.findBackupInfoForBuddy(failedPeer);
		for (IRecoveryBackupInformation info : infos) {
			// Now this is located on the new peer, the failed peer does not
			// exist anymore
			info.setLocationPeer(newPeer);
			BackupInformationMessage backupMessage = new BackupInformationMessage(info,
					BackupInformationMessage.NEW_INFO);
			sendMessage(newPeer, backupMessage);
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof RecoveryInstructionMessage) {
			RecoveryInstructionMessage instruction = (RecoveryInstructionMessage) message;
			RecoveryInstructionHandler.handleInstruction(senderPeer, instruction);

		} else if (message instanceof BackupInformationMessage) {

			// Store the backup information
			BackupInformationMessage backupInfoMessage = (BackupInformationMessage) message;
			RecoveryBackupMessageHandler.handleInstruction(senderPeer, backupInfoMessage);
			

		} else if (message instanceof RemoveQueryMessage) {

			// Remove stored backup information
			LocalBackupInformationAccess.getStore().remove(((RemoveQueryMessage) message).getSharedQueryID());

		} else if (message instanceof RecoveryAgreementMessage) {
			RecoveryAgreementMessage agreementMessage = (RecoveryAgreementMessage) message;
			RecoveryAgreementHandler.handleAgreementMessage(senderPeer, agreementMessage);

		}
	}

	@Override
	public void distributeUpdatedBackupInfo(IRecoveryBackupInformation info) {
		for (PeerID peer : cPeerDictionary.get().getRemotePeerIDs()) {
			sendBackupInformation(peer, info, false);
		}
	}

	@Override
	public void sendBackupInformation(PeerID destination, IRecoveryBackupInformation info, boolean isNewInfo) {

		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(info);
		int messageType;
		if (isNewInfo)
			messageType = BackupInformationMessage.NEW_INFO;
		else
			messageType = BackupInformationMessage.UPDATE_INFO;
		BackupInformationMessage message = new BackupInformationMessage(info, messageType);

		sendMessage(destination, message);

	}

	@Override
	public void sendRecoveryAgreementMessage(PeerID failedPeer, ID sharedQueryId) {

		if (!cPeerDictionary.isPresent()) {
			LOG.error("No P2P dictionary bound!");
			return;
		}

		// Send this to all other peers we know
		RecoveryAgreementMessage message = RecoveryAgreementMessage.createRecoveryAgreementMessage(failedPeer,
				sharedQueryId);
		for (PeerID destinationPeer : cPeerDictionary.get().getRemotePeerIDs())
			sendMessage(destinationPeer, message);
	}

	@Override
	public void sendUpdateReceiverMessage(PeerID receiverPeer, PeerID newSenderPeer, PipeID pipeId, ID sharedQueryId) {

		// 1. Tell the peer to update the receiver
		RecoveryInstructionMessage message = RecoveryInstructionMessage.createUpdateReceiverMessage(newSenderPeer,
				pipeId, sharedQueryId);
		sendMessage(receiverPeer, message);
	}

	@Override
	public void sendGoOnMessage(PeerID receiverPeer, PipeID pipeId) {
		RecoveryInstructionMessage message = RecoveryInstructionMessage.createGoOnMessage(pipeId);
		sendMessage(receiverPeer, message);
	}

	@Override
	public void chooseBuddyForQuery(ID sharedQueryId) {

		// Preconditions
		if (!cP2PNetworkManager.isPresent()) {

			LOG.error("No P2P network manager bound!");
			return;

		} else if (!cPeerDictionary.isPresent()) {

			LOG.error("No P2P dictionary bound!");
			return;

		}

		// TODO Buddy allocator
		if (cPeerDictionary.get().getRemotePeerIDs().iterator().hasNext()) {
			PeerID buddy = cPeerDictionary.get().getRemotePeerIDs().iterator().next();

			// 2. Get the necessary backup-information
			ImmutableCollection<String> infos = LocalBackupInformationAccess.getLocalPQL(sharedQueryId);
			List<String> pql = infos.asList();

			// 3. Send this to the buddy
			RecoveryInstructionMessage buddyMessage = RecoveryInstructionMessage.createBeBuddyMessage(sharedQueryId,
					pql);
			sendMessage(buddy, buddyMessage);

			// 4. Give the buddy the backup-information I have stored so that he
			// can do the recovery I was responsible for (well, I still am
			// responsible for it now, but when I fail there has to be someone
			// else)
			List<IRecoveryBackupInformation> backupInfosForOthers = BuddyHelper
					.findBackupInfoForBuddy(cP2PNetworkManager.get().getLocalPeerID());
			for (IRecoveryBackupInformation info : backupInfosForOthers) {
				sendBackupInformation(buddy, info, true);
			}

			// 5. Save, that this is my buddy so that we can find a new buddy if
			// that one fails
			LocalBackupInformationAccess.addMyBuddy(buddy, sharedQueryId);
		} else {
			noBuddyList.add(sharedQueryId);
			LOG.error("I am the last man standing - can't find a buddy. Saved id to list.");
		}

	}

	/**
	 * Sends a message to the given peer
	 * 
	 * @param receiverPeer
	 *            The peer which has to receive the message
	 * @param message
	 *            The message which you want to send
	 */
	private void sendMessage(PeerID receiverPeer, IMessage message) {

		// Preconditions
		if (!cPeerCommunicator.isPresent()) {
			LOG.error("No peer communicator bound!");
			return;
		}

		try {
			cPeerCommunicator.get().send(receiverPeer, message);
		} catch (PeerCommunicationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void peerAdded(PeerID peer) {
		if (cPeerDictionary.isPresent()) {
			LOG.debug("Found new peer: {}", cPeerDictionary.get().getRemotePeerName(peer.toString()));
		}
		// If we have sharedQueryIds where we don't have a buddy for - let the
		// new peer be the buddy for us
		for (ID sharedQueryId : noBuddyList) {
			chooseBuddyForQuery(sharedQueryId);
			LOG.debug("Chose the new peer as my buddy for {}", sharedQueryId);
		}
	}

	@Override
	public void peerRemoved(PeerID peer) {
		Preconditions.checkNotNull(peer);
		if (!cRecoveryStrategyManager.isPresent()) {
			LOG.error("No Recovery strategy manager bound!");
			return;
		}

		if (cPeerDictionary.isPresent()) {
			LOG.debug("Lost peer: {}", peer.toString());
		}

		// Start recovery
		cRecoveryStrategyManager.get().startRecovery(peer);
	}

}