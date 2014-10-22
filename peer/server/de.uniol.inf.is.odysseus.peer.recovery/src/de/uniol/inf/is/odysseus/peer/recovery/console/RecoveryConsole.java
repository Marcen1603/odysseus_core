package de.uniol.inf.is.odysseus.peer.recovery.console;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryP2PListener;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * 
 * @author Tobias Brandt, Simon Kuespert
 *
 */
public class RecoveryConsole implements CommandProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryConsole.class);

	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static Collection<IRecoveryAllocator> recoveryAllocators = Lists
			.newArrayList();
	/**
	 * Executor to get queries
	 */
	private static IServerExecutor executor;
	private static IRecoveryP2PListener peerFailureDetector;

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
	public static void bindRecoveryP2PListener(IRecoveryP2PListener serv) {
		peerFailureDetector = serv;
	}

	// called by OSGi-DS
	public static void unbindRecoveryP2PListener(IRecoveryP2PListener serv) {
		if (peerFailureDetector == serv) {
			peerFailureDetector = null;
		}
	}

	/**
	 * called by OSGi-DS to bind Executor
	 * 
	 * @param exe
	 *            Executor to bind.
	 */
	public static void bindExecutor(IExecutor exe) {
		executor = (IServerExecutor) exe;
	}

	/**
	 * called by OSGi-DS to unbind Executor
	 * 
	 * @param exe
	 *            Executor to unbind.
	 */
	public static void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional
			.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a recovery communicator.", communicator
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to unbind must be not null!");
		if (cCommunicator.isPresent()
				&& cCommunicator.get().equals(communicator)) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", communicator
					.getClass().getSimpleName());

		}

	}

	// called by OSGi-DS
	public static void bindRecoveryAllocator(IRecoveryAllocator allocator) {

		RecoveryConsole.recoveryAllocators.add(allocator);

	}

	// called by OSGi-DS
	public static void unbindRecoveryAllocator(IRecoveryAllocator allocator) {

		if (allocator != null) {

			RecoveryConsole.recoveryAllocators.remove(allocator);

		}

	}

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Recovery console activated.");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Recovery console deactivated.");
	}

	// ------------------------------------------

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Recovery commands---\n");
		sb.append("	recover <PeerID from failed peer> - Starts recovery for the peer <PeerID from failed peer>\n");
		sb.append("	lsBackupStore - Lists the stored sharedQueryIds with a list of peers which have parts of this sharedQuery. sharedQueryId: peer1, peer2, peer3\n");
		sb.append("	lsRecoveryAllocators - Lists the available recovery allocators.\n");
		sb.append("	showPeerPQL <PeerName> - Shows the PQL that this peer knows from <PeerName>.\n");
		sb.append("	sendHoldOn <PeerName from receiver> <sharedQueryId> - Send a hold-on message to <PeerName from receiver>, so that this should stop sending the tuples from query <sharedQueryId> further.\n");
		sb.append("	sendUpdateReceiver <PeerName from receiver> <PeerName from new sender> <pipeId> - Send an updateReceiver-message to <PeerName from receiver>, so that this should receive the tuples fir pipe <pipeId> from the new sender <PeerName from new sender>.\n");
		sb.append("	sendAddQueriesFromPeer <PeerName from receiver> <PeerName from failed peer> <sharedQueryId> - The <PeerName from receiver> will get a message which tells that the peer has to install the query <sharedQueryId> from <PeerName from failed peer>. \n");
		sb.append("	recoveryAllocation <AllocatorName> - Gets the id and name of a peer to allocate to (for testing) \n");
		sb.append("	startPeerFailureDetection <AllocatorName> - Starts detection of peer failures with the given allocator. \n");
		sb.append("	stopPeerFailureDetection - Stops detection of peer failures. \n");
		return sb.toString();
	}

	public void _recover(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		PeerID failedPeer = getPeerIdFromCi(ci);
		if (failedPeer == null) {
			System.out.println("Don't know this peer id");
			return;
		}
		
		cCommunicator.get().recover(failedPeer);

	}

	public void _showPeerPQL(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		String peerName = ci.nextArgument();
		Optional<PeerID> peerId = RecoveryHelper.determinePeerID(peerName);

		if (peerId.isPresent()) {
			Set<ID> queryIds = LocalBackupInformationAccess
					.getStoredSharedQueryIdsForPeer(peerId.get());
			System.out.println("PQL-Queries for peer with peerId "
					+ peerId.get());
			for (ID queryId : queryIds) {
				System.out.println("Shared Query ID: " + queryId);
				ImmutableCollection<String> pqls = LocalBackupInformationAccess
						.getStoredPQLStatements(queryId, peerId.get());

				for (String pql : pqls) {
					System.out.println("Query: ");
					System.out.println("-------");
					System.out.println(pql);
				}
			}
		}

	}

	public void _sendAddQueriesFromPeer(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		if (!cCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		String newPeerName = ci.nextArgument();
		String failedPeerName = ci.nextArgument();
		String queryId = ci.nextArgument();

		Optional<PeerID> optNewPeer = RecoveryHelper.determinePeerID(newPeerName);
		PeerID newPeer;
		if(!optNewPeer.isPresent()) {
			System.out.println("Don't know new peer. Take myself instead.");
			newPeer = p2pNetworkManager.getLocalPeerID();
		} else {
			newPeer = optNewPeer.get();
		}
		
		Optional<PeerID> optFailedPeer = RecoveryHelper.determinePeerID(failedPeerName);
		PeerID failedPeer;
		if(!optFailedPeer.isPresent()) {
			System.out.println("Don't know failed peer. Take myself instead.");
			failedPeer = p2pNetworkManager.getLocalPeerID();
		} else {
			failedPeer = optFailedPeer.get();
		}

		URI queryUri;
		try {
			queryUri = new URI(queryId);
			ID sharedQueryId = ID.create(queryUri);
			cCommunicator.get().installQueriesOnNewPeer(failedPeer, newPeer,
					sharedQueryId);
		} catch (URISyntaxException e) {
			System.out.println("Can't parse the queryId.");
		}
	}

	public void _sendHoldOn(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		if (!cCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		PeerID peerId = getPeerIdFromCi(ci);
		ID sharedQueryId = getSharedQueryIdFromCi(ci);

		List<PeerID> peers = new ArrayList<PeerID>();
		peers.add(peerId);

		List<ID> queryIds = new ArrayList<ID>();
		queryIds.add(sharedQueryId);
		cCommunicator.get().sendHoldOnMessages(peers, queryIds);
	}

	public void _sendUpdateReceiver(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		if (!cCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		PeerID receiverPeerId = getPeerIdFromCi(ci);
		PeerID newSendrePeerId = getPeerIdFromCi(ci);
		PipeID pipeId = getPipeIDFromCi(ci);

		if (receiverPeerId == null) {
			System.out
					.println("Don't know new receiver peer. Take myself insead.");
			receiverPeerId = RecoveryCommunicator.getP2pNetworkManager()
					.getLocalPeerID();
		}

		if (newSendrePeerId == null) {
			System.out
					.println("Don't know new sender peer. Take myself insead.");
			newSendrePeerId = RecoveryCommunicator.getP2pNetworkManager()
					.getLocalPeerID();
		}

		cCommunicator.get().sendUpdateReceiverMessage(receiverPeerId,
				newSendrePeerId, pipeId);
	}

	public void _startPeerFailureDetection(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must be not null!");

		String allocatorName = ci.nextArgument();
		if (Strings.isNullOrEmpty(allocatorName)) {

			System.out
					.println("usage: startPeerFailureDetection <AllocatorName>");
			return;

		}

		Optional<IRecoveryAllocator> optAllocator = RecoveryConsole
				.determineAllocator(allocatorName);
		if (!optAllocator.isPresent()) {

			System.out.println("No recovery allocator found with the name "
					+ allocatorName);
			return;

		}
		IRecoveryAllocator allocator = optAllocator.get();

		RecoveryCommunicator.setRecoveryAllocator(allocator);

		peerFailureDetector.startPeerFailureDetection();

		System.out
				.println("Peer failure detection is now switched on on this peer with usage of "
						+ allocator.getName() + " allocator.");
	}

	public void _stopPeerFailureDetection(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must be not null!");

		peerFailureDetector.stopPeerFailureDetection();

		System.out
				.println("Peer failure detection is now switched off on this peer.");
	}

	public void _lsBackupStore(CommandInterpreter ci) {
		StringBuilder sb = new StringBuilder();

		ImmutableCollection<ID> storedIds = LocalBackupInformationAccess
				.getStoredIDs();

		if (storedIds.isEmpty()) {
			System.out.println("No shared query ids stored.");
			return;
		}

		for (ID id : storedIds) {
			sb.append(id.toString() + " : ");
			ImmutableCollection<PeerID> peersForThisQueryId = LocalBackupInformationAccess
					.getStoredPeersForSharedQueryId(id);
			boolean isFirst = true;
			for (PeerID peer : peersForThisQueryId) {
				if (!isFirst)
					sb.append(", ");
				isFirst = false;
				String peerName = RecoveryHelper.determinePeerName(peer);
				sb.append(peerName);
			}

			sb.append("\n");
		}

		System.out.println(sb.toString());
	}

	/**
	 * Lists all available {@link IRecoveryAllocator}s bound via OSGI-DS.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _lsRecoveryAllocators(CommandInterpreter ci) {

		System.out.println("Available recovery allocators:");
		for (IRecoveryAllocator allocator : RecoveryConsole.recoveryAllocators) {

			System.out.println(allocator.getName());

		}

	}

	/**
	 * Console command to test allocation.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _recoveryAllocation(CommandInterpreter ci) {

		String allocatorName = ci.nextArgument();
		if (Strings.isNullOrEmpty(allocatorName)) {

			System.out.println("usage: recoveryAllocation <AllocatorName>");
			return;

		}

		Optional<IRecoveryAllocator> optAllocator = RecoveryConsole
				.determineAllocator(allocatorName);
		if (!optAllocator.isPresent()) {

			System.out.println("No recovery allocator found with the name "
					+ allocatorName);
			return;

		}
		IRecoveryAllocator allocator = optAllocator.get();

		PeerID peer = null;
		try {
			peer = allocator.allocate(p2pDictionary.getRemotePeerIDs(),
					p2pNetworkManager.getLocalPeerID());
			System.out.println("Allocator has chosen "
					+ p2pDictionary.getRemotePeerName(peer) + " as target");
		} catch (QueryPartAllocationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * If your next argument should be the PeerName, you can get the PeerID it
	 * with this method
	 * 
	 * @param ci
	 * @return PeerID which is made from the next argument from the ci
	 */
	private PeerID getPeerIdFromCi(CommandInterpreter ci) {
		String peerNameString = ci.nextArgument();
		PeerID peerId = null;
		if (!Strings.isNullOrEmpty(peerNameString)) {
			peerId = RecoveryHelper.determinePeerID(peerNameString).isPresent() ? RecoveryHelper
					.determinePeerID(peerNameString).get() : null;
		}

		return peerId;
	}

	/**
	 * If your next argument should be the PipeId, you can get the PipeId with
	 * this method
	 * 
	 * @param ci
	 * @return
	 */
	private PipeID getPipeIDFromCi(CommandInterpreter ci) {
		String pipeIdString = ci.nextArgument();
		PipeID pipeId = null;
		if (!Strings.isNullOrEmpty(pipeIdString)) {
			URI pipeIdURI;
			try {
				pipeIdURI = new URI(pipeIdString);
				pipeId = PipeID.create(pipeIdURI);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pipeId;
	}

	/**
	 * If your next argument should be the sharedQueryID, you can get it with
	 * this method
	 * 
	 * @param ci
	 * @return SharedQueryId which is made from the next argument from the ci
	 */
	private ID getSharedQueryIdFromCi(CommandInterpreter ci) {
		String sharedQueryIdString = ci.nextArgument();
		ID sharedQueryId = null;
		if (!Strings.isNullOrEmpty(sharedQueryIdString)) {
			URI sharedIdUri;
			try {
				sharedIdUri = new URI(sharedQueryIdString);
				sharedQueryId = ID.create(sharedIdUri);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sharedQueryId;
	}

	/**
	 * Determines the {@link IRecoveryAllocator} by name.
	 * 
	 * @param allocatorName
	 *            The name of the allocator.
	 * @return An {@link IRecoveryAllocator}, if there is one bound with
	 *         <code>allocatorName</code> as name.
	 */
	private static Optional<IRecoveryAllocator> determineAllocator(
			String allocatorName) {

		Preconditions.checkNotNull(allocatorName,
				"The name of the recovery allocator must be not null!");

		for (IRecoveryAllocator allocator : RecoveryConsole.recoveryAllocators) {

			if (allocator.getName().equalsIgnoreCase(allocatorName)) {

				return Optional.of(allocator);

			}

		}

		return Optional.absent();

	}

}
