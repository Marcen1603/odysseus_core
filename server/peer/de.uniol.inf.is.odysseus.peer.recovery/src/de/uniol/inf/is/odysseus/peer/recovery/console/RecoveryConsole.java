package de.uniol.inf.is.odysseus.peer.recovery.console;

import java.net.URI;
import java.net.URISyntaxException;

import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

public class RecoveryConsole implements CommandProvider {

	// TODO Bind with xml
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

	// ------------------------------------------

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Recovery commands---\n");
		sb.append("sendHoldOn <PeerID from receiver> <sharedQueryId> - Send a hold-on message to <PeerID from receiver>, so that this should stop sending the tuples from query <sharedQueryId> further.\n");
		return sb.toString();
	}

	@SuppressWarnings("unused")
	public void _sendHoldOn(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must be not null!");

		String peerIdString = ci.nextArgument();
		String sharedQueryIdString = ci.nextArgument();

		PeerID peerId = null;
		int sharedQueryId = 0;
		
		if (!Strings.isNullOrEmpty(peerIdString)) {
			URI peerUri;
			try {
				peerUri = new URI(peerIdString);
				peerId = PeerID.create(peerUri);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (!Strings.isNullOrEmpty(sharedQueryIdString)) {
			sharedQueryId = Integer.parseInt(sharedQueryIdString);
		}
		
	}

}
