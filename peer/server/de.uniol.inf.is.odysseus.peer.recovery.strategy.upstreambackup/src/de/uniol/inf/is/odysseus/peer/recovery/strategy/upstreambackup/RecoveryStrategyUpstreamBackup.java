package de.uniol.inf.is.odysseus.peer.recovery.strategy.upstreambackup;

import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryDynamicBackup;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;

public class RecoveryStrategyUpstreamBackup implements IRecoveryStrategy{
	
	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryStrategyUpstreamBackup.class);	
	
	
	// called by OSGi-DS
	public void activate() {
		LOG.debug("Recovery strategy Upstream-Backup activated.");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Recovery strategy Upstream-Backup deactivated.");
	}

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional.absent();
	
	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P network manager to bind. <br />
	 * Must be not null.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		
		Preconditions.checkNotNull(serv);
		cP2PNetworkManager = Optional.of(serv);
		LOG.debug("Bound {} as a P2P network manager.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a P2P network manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P network manager to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {
			
			cP2PNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	/**
	 * The P2P dictionary, if there is one bound.
	 */
	private static Optional<IP2PDictionary> cP2PDictionary = Optional.absent();
	
	/**
	 * Binds a P2P dictionary. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P dictionary to bind. <br />
	 * Must be not null.
	 */
	public static void bindP2PDictionary(IP2PDictionary serv) {
		
		Preconditions.checkNotNull(serv);
		cP2PDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a P2P dictionary.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a P2P dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P dictionary to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cP2PDictionary.isPresent() && cP2PDictionary.get() == serv) {
			
			cP2PDictionary = Optional.absent();
			LOG.debug("Unbound {} as a P2P dictionary.", serv
					.getClass().getSimpleName());
			
		}
		
	}	
	
	/**
	 * The recovery allocator, if there is one bound.
	 */
	private static Optional<IRecoveryAllocator> cRecoveryAllocator = Optional.absent();
	
	/**
	 * Binds a recovery allocator. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery allocator to bind. <br />
	 * Must be not null.
	 */
	public static void bindRecoveryAllocator(IRecoveryAllocator serv) {
		
		Preconditions.checkNotNull(serv);
		cRecoveryAllocator = Optional.of(serv);
		LOG.debug("Bound {} as a recovery allocator.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a recovery allocator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery allocator to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindRecoveryAllocator(IRecoveryAllocator serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cRecoveryAllocator.isPresent() && cRecoveryAllocator.get() == serv) {
			
			cRecoveryAllocator = Optional.absent();
			LOG.debug("Unbound {} as a recovery allocator.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	/**
	 * The recovery dynamic backup, if there is one bound.
	 */
	private static Optional<IRecoveryDynamicBackup> cRecoveryDynamicBackup = Optional.absent();
	
	/**
	 * Binds a recovery dynamic backup. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery dynamic backup to bind. <br />
	 * Must be not null.
	 */
	public static void bindRecoveryDynamicBackup(IRecoveryDynamicBackup serv) {
		
		Preconditions.checkNotNull(serv);
		cRecoveryDynamicBackup = Optional.of(serv);
		LOG.debug("Bound {} as a recovery dynamic backup.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a recovery dynamic backup, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The recovery dynamic backup to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindRecoveryDynamicBackup(IRecoveryDynamicBackup serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cRecoveryDynamicBackup.isPresent() && cRecoveryDynamicBackup.get() == serv) {
			
			cRecoveryDynamicBackup = Optional.absent();
			LOG.debug("Unbound {} as a recovery dynamic backup.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	
	@Override
	public void recover(PeerID failedPeer) {

		// Preconditions
		if(!cP2PNetworkManager.isPresent()) {
			
			LOG.error("No P2P network manager bound!");
			return;
			
		} else if(!cP2PDictionary.isPresent()) {
			
			LOG.error("No P2P dictionary bound!");
			return;
			
		} else if(!cRecoveryAllocator.isPresent()) {
			
			LOG.error("No recovery allocator bound!");
			return;
			
		} else if(!cRecoveryDynamicBackup.isPresent()) {
			
			LOG.error("No recovery dynamic backup bound!");
			return;
			
		}
		
		LOG.debug("Want to start recovery for {}", cP2PDictionary.get().getRemotePeerName(failedPeer));

		// To update the affected senders
		int i = 0;

		// Reallocate each query to another peer
		// TODO Refactor allocation process
		
		for (ID sharedQueryId : cRecoveryDynamicBackup.get().getSharedQueryIdsForRecovery(failedPeer)) {
			LOG.debug("Have info for {} -> I can do recovery.", cP2PDictionary.get().getRemotePeerName(failedPeer));
			// 4. Search for another peer who can take the parts from the failed
			// peer

			// TODO find a good place to reallocate if the peer doesn't accept
			// the query or is unable to install it

			PeerID peer = null;

			if (!cRecoveryAllocator.isPresent()) {
				LOG.error("No allocator for recovery allocation set.");
			} else {
				try {
					peer = cRecoveryAllocator.get().allocate(cP2PDictionary.get().getRemotePeerIDs(),
							cP2PNetworkManager.get().getLocalPeerID());
					LOG.debug("Peer ID for recovery allocation found.");
				} catch (QueryPartAllocationException e) {
					LOG.error("Peer ID search for recovery allocation failed.");
					e.printStackTrace();
				}
			}

			// If the peer is null, we don't know any other peer so we have to
			// install it on ourself
			if (peer == null)
				peer = cP2PNetworkManager.get().getLocalPeerID();

			cRecoveryDynamicBackup.get().determineAndSendHoldOnMessages(sharedQueryId, failedPeer);

			// 5. Tell the new peer to install the parts from the failed peer
			cRecoveryDynamicBackup.get().initiateAgreement(failedPeer, sharedQueryId, peer);

			// 6. Update our sender so it knows the new peerId
			List<JxtaSenderPO<?>> affectedSenders = cRecoveryDynamicBackup.get().getAffectedSenders(failedPeer);
			if (i < affectedSenders.size()) {
				affectedSenders.get(i).setPeerIDString(peer.toString());
			}

		}

	}

	@Override
	public String getName() {
		return "upstreambackup";
	}

}
