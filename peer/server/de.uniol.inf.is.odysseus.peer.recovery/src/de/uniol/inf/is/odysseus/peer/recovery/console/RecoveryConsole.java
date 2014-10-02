package de.uniol.inf.is.odysseus.peer.recovery.console;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryP2PListener;
import de.uniol.inf.is.odysseus.peer.recovery.internal.JxtaInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.util.LocalBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * 
 * @author Tobias Brandt
 *
 */
public class RecoveryConsole implements CommandProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryConsole.class);

	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
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
	public static void bindRecoveryP2PListener(
			IRecoveryP2PListener serv) {
		peerFailureDetector = serv;
	}

	// called by OSGi-DS
	public static void unbindRecoveryP2PListener(
			IRecoveryP2PListener serv) {
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
		sb.append("	lsBackupStore - Lists the stored sharedQueryIds with a list of peers which have parts of this sharedQuery. sharedQueryId: peer1, peer2, peer3\n");
		sb.append("	lsJxtaBackup - Lists the stored Jxta-backup-info.\n");
		sb.append("	showPeerPQL <PeerName> - Shows the PQL that this peer knows from <PeerName>.\n");
		sb.append("	sendHoldOn <PeerName from receiver> <sharedQueryId> - Send a hold-on message to <PeerName from receiver>, so that this should stop sending the tuples from query <sharedQueryId> further.\n");
		sb.append("	sendNewReceiver <PeerName from receiver> <PeerName from new receiver> <sharedQueryId> - Send a newReceiver-message to <PeerName from receiver>, so that this should send the tuples from query <sharedQueryId> to the new receiver <PeerName from new receiver>.\n");
		sb.append("	sendAddQueriesFromPeer <PeerName from receiver> <PeerName from failed peer> - The <PeerName from receiver> will get a message which tells that the peer has to install all queries from <PeerName from failed peer>. \n");
		sb.append("	startPeerFailureDetection - Starts detection of peer failures. \n");
		sb.append("	stopPeerFailureDetection - Stops detection of peer failures. \n");
		return sb.toString();
	}

	public void _showPeerPQL(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		String peerName = ci.nextArgument();
		Optional<PeerID> peerId = RecoveryHelper.determinePeerID(peerName);

		if (peerId.isPresent()) {
			List<ID> queryIds = LocalBackupInformationAccess
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

		String newPeerName = ci.nextArgument();
		String failedPeerName = ci.nextArgument();

		PeerID newPeer = RecoveryHelper.determinePeerID(newPeerName).get();
		PeerID failedPeer = RecoveryHelper.determinePeerID(failedPeerName)
				.get();

		RecoveryCommunicator.getInstance().installQueriesOnNewPeer(failedPeer, newPeer);
	}

	public void _sendHoldOn(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		PeerID peerId = getPeerIdFromCi(ci);
		ID sharedQueryId = getSharedQueryIdFromCi(ci);

		List<PeerID> peers = new ArrayList<PeerID>();
		peers.add(peerId);

		List<ID> queryIds = new ArrayList<ID>();
		queryIds.add(sharedQueryId);
		RecoveryCommunicator.getInstance().sendHoldOnMessages(peers, queryIds);
	}

	public void _sendNewReceiver(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		PeerID senderPeerId = getPeerIdFromCi(ci);
		PeerID newReceiverPeerId = getPeerIdFromCi(ci);
		ID sharedQueryId = getSharedQueryIdFromCi(ci);

		if (senderPeerId == null) {
			System.out
					.println("Don't know new sender peer. Take myself insead.");
			senderPeerId = RecoveryCommunicator.getP2pNetworkManager()
					.getLocalPeerID();
		}

		if (newReceiverPeerId == null) {
			System.out
					.println("Don't know new receiver peer. Take myself insead.");
			newReceiverPeerId = RecoveryCommunicator.getP2pNetworkManager()
					.getLocalPeerID();
		}

		RecoveryCommunicator.getInstance().sendNewReceiverMessage(senderPeerId,
				newReceiverPeerId, sharedQueryId);
	}

	public void _startPeerFailureDetection(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must be not null!");

		peerFailureDetector.startPeerFailureDetection();

	}

	public void _stopPeerFailureDetection(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must be not null!");

		peerFailureDetector.stopPeerFailureDetection();

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

	public void _lsJxtaBackup(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		List<PeerID> peers = LocalBackupInformationAccess
				.getPeersFromJxtaInfoStore();

		if (peers.isEmpty()) {
			System.out.println("No information saved");
			return;
		}

		for (PeerID peer : peers) {
			List<JxtaInformation> jxtaInfo = LocalBackupInformationAccess
					.getJxtaInfoForPeer(peer);
			for (JxtaInformation info : jxtaInfo) {
				String peerName = RecoveryHelper.determinePeerName(peer);
				System.out.println("Jxta-information about the peer "
						+ peerName + " (" + peer + ")");
				System.out.println("QueryID: " + info.getSharedQueryID());
				System.out.println(info.getKey() + " : " + info.getValue());
			}

		}

	}

	/**
	 * If your next argument should be the PeerID, you can get it with this
	 * method
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

}
