package de.uniol.inf.is.odysseus.peer.recovery.console;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;

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
		sb.append("sendHoldOn <PeerName from receiver> <sharedQueryId> - Send a hold-on message to <PeerName from receiver>, so that this should stop sending the tuples from query <sharedQueryId> further.\n");
		return sb.toString();
	}

	@SuppressWarnings("unused")
	public void _sendHoldOn(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must be not null!");

		String peerNameString = ci.nextArgument();
		String sharedQueryIdString = ci.nextArgument();

		PeerID peerId = null;
		int sharedQueryId = 0;

		if (!Strings.isNullOrEmpty(peerNameString)) {

			URI peerUri;
			peerId = determinePeerID(peerNameString).isPresent() ? determinePeerID(
					peerNameString).get()
					: null;
					System.out.println(peerId);
		}

		if (!Strings.isNullOrEmpty(sharedQueryIdString)) {
			sharedQueryId = Integer.parseInt(sharedQueryIdString);
		}
		
		List<PeerID> peers = new ArrayList<PeerID>();
		peers.add(peerId);
		
		List<Integer> queryIds = new ArrayList<Integer>();
		queryIds.add(sharedQueryId);
		RecoveryCommunicator.getInstance().sendHoldOnMessages(peers, queryIds);

	}

	/**
	 * If you need the PeerId for a given PeerName Copied from PeerConsole.
	 * 
	 * @param peerName
	 *            Name of the peer you want to have the ID from
	 * @return PeerID
	 */
	private static Optional<PeerID> determinePeerID(String peerName) {
		for (PeerID pid : p2pDictionary.getRemotePeerIDs()) {
			if (p2pDictionary.getRemotePeerName(pid).equals(peerName)) {
				return Optional.of(pid);
			}
		}
		return Optional.absent();
	}

}
