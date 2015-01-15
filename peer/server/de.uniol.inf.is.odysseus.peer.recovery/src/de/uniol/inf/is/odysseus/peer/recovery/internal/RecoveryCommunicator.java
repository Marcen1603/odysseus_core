package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.AddQuerySender;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.AgreementSender;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.TupleSendSender;
import de.uniol.inf.is.odysseus.peer.recovery.protocol.UpdatePipeSender;

/**
 * A recovery communicator handles the communication between peers for recovery mechanisms.
 * 
 * @author Tobias Brandt & Michael Brand & Simon Kuespert
 * 
 */
public class RecoveryCommunicator implements IRecoveryCommunicator {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryCommunicator.class);

	private Map<UUID, RecoveryProcessState> recoveryProcessStates = new HashMap<UUID, RecoveryProcessState>();

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
	}

	/**
	 * Unregisters messages and listeners at the peer communicator.
	 */
	private void unregisterMessagesAndAddListeners() {
		Preconditions.checkArgument(cPeerCommunicator.isPresent());
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
	public boolean sendHoldOnMessage(PeerID receiverPeer, PipeID pipeId, PeerID failedPeer) {
		Preconditions.checkNotNull(receiverPeer);
		Preconditions.checkNotNull(pipeId);
		if (!cPeerCommunicator.isPresent()) {
			LOG.error("No peer communicator bound!");
			return false;
		}

		return TupleSendSender.getInstance().sendHoldOnInstruction(receiverPeer, pipeId, failedPeer,
				cPeerCommunicator.get());
	}

	@Override
	public void installQueriesOnNewPeer(PeerID failedPeer, PeerID newPeer, int localQueryId, QueryState queryState,
			String pql, UUID recoveryStateIdentifier, UUID subprocessID, ID sharedQuery, boolean master, PeerID masterId) {

		Preconditions.checkNotNull(failedPeer);
		Preconditions.checkNotNull(newPeer);
		Preconditions.checkNotNull(pql);

		if (!cPeerCommunicator.isPresent()) {
			LOG.error("No peer communicator bound!");
			return;
		}

		// Send the add query message
		AddQuerySender.getInstance().sendAddQueryPart(newPeer, pql, localQueryId, queryState, sharedQuery, master, masterId,
				recoveryStateIdentifier, subprocessID, cPeerCommunicator.get());
	}

	@Override
	public boolean sendRecoveryAgreementMessage(PeerID failedPeer, int localQueryId) {
		Preconditions.checkNotNull(failedPeer);
		if (!cPeerCommunicator.isPresent()) {
			LOG.error("No peer communicator bound!");
			return false;
		}

		boolean allFailed = true;
		for (PeerID destination : cPeerDictionary.get().getRemotePeerIDs()) {
			allFailed = allFailed
					&& !AgreementSender.getInstance().sendAgreement(destination, failedPeer, localQueryId,
							cPeerCommunicator.get());
		}
		return !allFailed;
	}

	@Override
	public boolean sendUpdateReceiverMessage(PeerID receiverPeer, PeerID newSenderPeer, PipeID pipeId, int localQueryId) {
		Preconditions.checkNotNull(receiverPeer);
		Preconditions.checkNotNull(newSenderPeer);
		Preconditions.checkNotNull(pipeId);
		if (!cPeerCommunicator.isPresent()) {
			LOG.error("No peer communicator bound!");
			return false;
		}

		return UpdatePipeSender.getInstance().sendReceiverUpdateInstruction(receiverPeer, pipeId, newSenderPeer,
				localQueryId, cPeerCommunicator.get());
	}

	@Override
	public boolean sendUpdateSenderMessage(PeerID receiverPeer, PeerID newReceiver, PipeID pipeId, int localQueryId) {
		Preconditions.checkNotNull(receiverPeer);
		Preconditions.checkNotNull(newReceiver);
		Preconditions.checkNotNull(pipeId);
		if (!cPeerCommunicator.isPresent()) {
			LOG.error("No peer communicator bound!");
			return false;
		}

		return UpdatePipeSender.getInstance().sendSenderUpdateInstruction(receiverPeer, pipeId, newReceiver,
				localQueryId, cPeerCommunicator.get());
	}

	@Override
	public boolean sendGoOnMessage(PeerID receiverPeer, PipeID pipeId) {
		Preconditions.checkNotNull(receiverPeer);
		Preconditions.checkNotNull(pipeId);
		if (!cPeerCommunicator.isPresent()) {
			LOG.error("No peer communicator bound!");
			return false;
		}
		LOG.debug("Send Go-on instruction.");
		return TupleSendSender.getInstance().sendGoOnInstruction(receiverPeer, pipeId, cPeerCommunicator.get());
	}

	@Override
	public void peerAdded(PeerID peer) {
		if (cPeerDictionary.isPresent()) {
			LOG.debug("Found new peer: {}", cPeerDictionary.get().getRemotePeerName(peer.toString()));
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
		RecoveryProcessState state = new RecoveryProcessState(peer);
		recoveryProcessStates.put(state.getIdentifier(), state);
		cRecoveryStrategyManager.get().startRecovery(peer, state.getIdentifier());
	}

	@Override
	public RecoveryProcessState getRecoveryProcessState(UUID identifier) {
		if (!recoveryProcessStates.containsKey(identifier)) {
			LOG.error("No recovery state for identifier {} found.", identifier);
			return null;
		}
		return recoveryProcessStates.get(identifier);
	}

	@Override
	public void removeRecoveryProcessState(UUID identifier) {
		if (recoveryProcessStates.containsKey(identifier)) {
			recoveryProcessStates.remove(identifier);
		}
	}

	@Override
	public void setRecoveryStrategyManager(IRecoveryStrategyManager strategyManager) {
		cRecoveryStrategyManager = Optional.of(strategyManager);
	}

}