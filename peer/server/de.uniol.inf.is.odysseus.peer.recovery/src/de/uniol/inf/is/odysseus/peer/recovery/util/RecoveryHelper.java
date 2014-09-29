package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
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

	/**
	 * If you have the peerId and want to have the name of the peer
	 * 
	 * @param peerId
	 *            Peer you want to have the name from
	 * @return Name of the peer
	 */
	public static String determinePeerName(PeerID peerId) {
		return RecoveryCommunicator.getP2pDictionary()
				.getRemotePeerName(peerId);
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Get Physical JxtaOperator (Sender or Receiver) by PipeID.
	 * Copied from LoadBalancingHelper
	 * @param lookForSender true if we look for a sender, false if we look for receiver.
	 * @param pipeID Pipe id of Sender we look for.
	 * @return Operator (if found) or null.
	 */
	public static IPhysicalOperator getPhysicalJxtaOperator(
			boolean lookForSender, String pipeID) {

		IServerExecutor executor = RecoveryCommunicator.getExecutor();

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

	public static PipeID getReceiverPipeId(String pql) {
		String pipeSearch = "JXTARECEIVER({PIPE='";
		int pipeIdLength = 80;

		if (!pql.contains(pipeSearch))
			return null;

		int index = pql.indexOf(pipeSearch);
		int beginIndex = index + pipeSearch.length();

		String pipeIdString = pql.substring(beginIndex, beginIndex + pipeIdLength);

		try {
			URI pipeUri = new URI(pipeIdString);
			PipeID pipeId = PipeID.create(pipeUri);
			return pipeId;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
