package de.uniol.inf.is.odysseus.peer.recovery.console;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * 
 * Console commands for Backup-Related things
 * 
 * @author Tobias Brandt, Simon Kuespert
 *
 */
public class RecoveryConsole implements CommandProvider {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryConsole.class);

	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerDictionary peerDictionary;
	private static Collection<IRecoveryAllocator> recoveryAllocators = Lists.newArrayList();
	private static IBackupInformationAccess backupInformationAccess;

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional.absent();
	/**
	 * Executor to get queries
	 */
	private static IServerExecutor executor;
	private static Collection<IRecoveryStrategy> recoveryStrategies = Lists.newArrayList();
	private static Collection<IRecoveryStrategyManager> recoveryStrategyManagers = Lists.newArrayList();

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

	public static void bindBackupInformationAccess(IBackupInformationAccess infoAccess) {
		backupInformationAccess = infoAccess;
	}

	public static void unbindBackupInformationAccess(IBackupInformationAccess infoAccess) {
		if (backupInformationAccess == infoAccess) {
			backupInformationAccess = null;
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
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator, "The recovery communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a recovery communicator.", communicator.getClass().getSimpleName());

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

		Preconditions.checkNotNull(communicator, "The recovery communicator to unbind must be not null!");
		if (cCommunicator.isPresent() && cCommunicator.get().equals(communicator)) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", communicator.getClass().getSimpleName());

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
	public static void bindRecoveryStrategy(IRecoveryStrategy recoveryStrategy) {
		RecoveryConsole.recoveryStrategies.add(recoveryStrategy);
	}

	// called by OSGi-DS
	public static void unbindRecoveryStrategy(IRecoveryStrategy recoveryStrategy) {
		if (recoveryStrategy != null) {
			RecoveryConsole.recoveryStrategies.remove(recoveryStrategy);
		}
	}

	// called by OSGi-DS
	public static void bindRecoveryStrategyManager(IRecoveryStrategyManager recoveryStrategyManager) {
		RecoveryConsole.recoveryStrategyManagers.add(recoveryStrategyManager);
	}

	// called by OSGi-DS
	public static void unbindRecoveryStrategyManager(IRecoveryStrategyManager recoveryStrategyManager) {
		if (recoveryStrategyManager != null) {
			RecoveryConsole.recoveryStrategyManagers.remove(recoveryStrategyManager);
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
		sb.append("	lsRecoveryStrategies - Lists the available recovery strategies.\n");
		sb.append("	lsRecoveryStrategyManagers - Lists the available recovery strategy managers.\n");
		sb.append("	sendHoldOn <PeerName from receiver> <sharedQueryId> - Send a hold-on message to <PeerName from receiver>, so that this should stop sending the tuples from query <sharedQueryId> further.\n");
		sb.append("	sendUpdateReceiver <PeerName from receiver> <PeerName from new sender> <pipeId> <sharedQueryId> - Send an updateReceiver-message to <PeerName from receiver>, so that this should receive the tuples fir pipe <pipeId> from the new sender <PeerName from new sender>.\n");
		sb.append("	sendAddQueriesFromPeer <PeerName from receiver> <PeerName from failed peer> <sharedQueryId> - The <PeerName from receiver> will get a message which tells that the peer has to install the query <sharedQueryId> from <PeerName from failed peer>. \n");
		sb.append("	holdOn <PipeId> - Let this peer hold on\n");
		sb.append("	goOn <PipeId> - Let this peer go on\n");
		return sb.toString();
	}

	public void _recover(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		PeerID failedPeer = getPeerIdFromCi(ci);
		if (failedPeer == null) {
			ci.println("Don't know this peer id");
			return;
		}
		if (!cCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		cCommunicator.get().peerRemoved(failedPeer);
	}

	public void _sendHoldOn(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		if (!cCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		}

		ci.println("I do nothing for now.");
	}

	public void _sendUpdateReceiver(CommandInterpreter ci) {
		Preconditions.checkNotNull(ci, "Command interpreter must not be null!");

		if (!cCommunicator.isPresent()) {

			LOG.error("No recovery communicator bound!");
			return;

		} else if (!RecoveryCommunicator.getP2PNetworkManager().isPresent()) {

			LOG.error("No P2P network manager bound!");
			return;

		}

		PeerID receiverPeerId = getPeerIdFromCi(ci);
		PeerID newSendrePeerId = getPeerIdFromCi(ci);
		PipeID pipeId = getPipeIDFromCi(ci);
		int localQueryId = Integer.parseInt(ci.nextArgument());

		if (receiverPeerId == null) {
			ci.println("Don't know new receiver peer. Take myself insead.");
			receiverPeerId = RecoveryCommunicator.getP2PNetworkManager().get().getLocalPeerID();
		}

		if (newSendrePeerId == null) {
			ci.println("Don't know new sender peer. Take myself insead.");
			newSendrePeerId = RecoveryCommunicator.getP2PNetworkManager().get().getLocalPeerID();
		}

		cCommunicator.get().sendUpdateReceiverMessage(receiverPeerId, newSendrePeerId, pipeId, localQueryId);
	}

	public void _lsBackupStore(CommandInterpreter ci) {
		List<String> backupPeerIds = backupInformationAccess.getBackupPeerIds();
		if (backupPeerIds.isEmpty()) {
			System.out.println("No information about any peer");
		}
		for (String peerId : backupPeerIds) {
			String peerName = peerDictionary.getRemotePeerName(peerId);
			if (peerId.equals(p2pNetworkManager.getLocalPeerID().toString())) {
				peerName = peerName + " (me)";
			}
			System.out.println("Information about " + peerName + "\n");
			HashMap<Integer, BackupInfo> infoMap = backupInformationAccess.getBackupInformation(peerId);
			if (infoMap != null) {
				for (Integer key : infoMap.keySet()) {
					BackupInfo info = infoMap.get(key);
					System.out.println("Local query id: " + key + " | Is master: " + info.master + " | Strategy: " + info.strategy + " | " + info.sharedQuery + "\n" + info.pql + " (" + info.state + ") \n");
				}
			} else {
				System.out.println("No Backup-Information about " + peerId);
			}
			System.out.println("\n\n");
		}
	}

	/**
	 * Lists all available {@link IRecoveryAllocator}s bound via OSGI-DS.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _lsRecoveryAllocators(CommandInterpreter ci) {

		ci.println("Available recovery allocators:");
		for (IRecoveryAllocator allocator : RecoveryConsole.recoveryAllocators) {

			ci.println(allocator.getName());

		}

	}

	/**
	 * Lists all available {@link IRecoveryStrategyManager}s bound via OSGI-DS.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _lsRecoveryStrategyManagers(CommandInterpreter ci) {

		ci.println("Available recovery strategy managers:");
		for (IRecoveryStrategyManager strategyManager : RecoveryConsole.recoveryStrategyManagers) {

			ci.println(strategyManager.getName());

		}

	}

	/**
	 * Lists all available {@link IRecoveryStrategy}s bound via OSGI-DS.
	 * 
	 * @param ci
	 *            The {@link CommandInterpreter} instance.
	 */
	public void _lsRecoveryStrategies(CommandInterpreter ci) {

		ci.println("Available recovery strategies:");
		for (IRecoveryStrategy strategy : RecoveryConsole.recoveryStrategies) {

			ci.println(strategy.getName());

		}

	}

	public void _holdOn(CommandInterpreter ci) {
		PipeID pipe = getPipeIDFromCi(ci);
		try {
			RecoveryHelper.startBuffering(pipe.toString());
		} catch (Exception e) {
			LOG.error("Error while holdOn.", e);
		}
	}

	public void _goOn(CommandInterpreter ci) {
		PipeID pipe = getPipeIDFromCi(ci);
		try {
			RecoveryHelper.resumeFromBuffering(pipe.toString());
		} catch (Exception e) {
			LOG.error("Error while goOn.", e);
		}
	}

	/**
	 * If your next argument should be the PeerName, you can get the PeerID it with this method
	 * 
	 * @param ci
	 * @return PeerID which is made from the next argument from the ci
	 */
	private PeerID getPeerIdFromCi(CommandInterpreter ci) {
		String peerNameString = ci.nextArgument();
		PeerID peerId = null;
		if (!Strings.isNullOrEmpty(peerNameString)) {
			peerId = RecoveryHelper.determinePeerID(peerNameString).isPresent() ? RecoveryHelper.determinePeerID(
					peerNameString).get() : null;
		}

		return peerId;
	}

	/**
	 * If your next argument should be the PipeId, you can get the PipeId with this method
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
	 * Determines the {@link IRecoveryAllocator} by name.
	 * 
	 * @param allocatorName
	 *            The name of the allocator.
	 * @return An {@link IRecoveryAllocator}, if there is one bound with <code>allocatorName</code> as name.
	 */
	@SuppressWarnings("unused")
	private static Optional<IRecoveryAllocator> determineAllocator(String allocatorName) {

		Preconditions.checkNotNull(allocatorName, "The name of the recovery allocator must be not null!");

		for (IRecoveryAllocator allocator : RecoveryConsole.recoveryAllocators) {

			if (allocator.getName().equalsIgnoreCase(allocatorName)) {

				return Optional.of(allocator);

			}

		}

		return Optional.absent();

	}

}
