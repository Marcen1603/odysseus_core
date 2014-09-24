package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;

public class RecoveryHelper {

	/**
	 * Installs and executes a query from PQL.
	 * 
	 * @param pql
	 *            PQL to execute.
	 */
	public static Collection<Integer> installAndRunQueryPartFromPql(String pql) {

		IServerExecutor executor = RecoveryCommunicator.getExecutor();
		ISession session = RecoveryCommunicator.getActiveSession();

		Collection<Integer> installedQueries = executor.addQuery(pql, "PQL",
				session, "Standard", Context.empty());
		for (int query : installedQueries) {
			executor.startQuery(query, session);
		}
		return installedQueries;
	}

	/**
	 * If you need the PeerId for a given PeerName Copied from PeerConsole.
	 * 
	 * @param peerName
	 *            Name of the peer you want to have the ID from
	 * @return PeerID
	 */
	public static Optional<PeerID> determinePeerID(String peerName) {
		for (PeerID pid : RecoveryCommunicator.getP2pDictionary()
				.getRemotePeerIDs()) {
			if (RecoveryCommunicator.getP2pDictionary().getRemotePeerName(pid)
					.equals(peerName)) {
				return Optional.of(pid);
			}
		}
		return Optional.absent();
	}
	
	public static String determinePeerName(PeerID peerId) {
		return RecoveryCommunicator.getP2pDictionary().getRemotePeerName(peerId);
	}

}
