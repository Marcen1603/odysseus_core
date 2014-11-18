package de.uniol.inf.is.odysseus.peer.recovery.strategy.upstreambackup;

import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryDynamicBackup;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;

/**
 * Implementation of the Upstream-Backup {@link IRecoveryStrategy}.
 * @author Simo
 *
 */
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

//	/**
//	 * The P2P network manager, if there is one bound.
//	 */
//	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional.absent();
//	
//	/**
//	 * Binds a P2P network manager. <br />
//	 * Called by OSGi-DS.
//	 * @param serv The P2P network manager to bind. <br />
//	 * Must be not null.
//	 */
//	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
//		
//		Preconditions.checkNotNull(serv);
//		cP2PNetworkManager = Optional.of(serv);
//		LOG.debug("Bound {} as a P2P network manager.", serv
//				.getClass().getSimpleName());
//		
//	}
//
//	/**
//	 * Unbinds a P2P network manager, if it's the bound one. <br />
//	 * Called by OSGi-DS.
//	 * @param serv The P2P network manager to unbind. <br />
//	 * Must be not null.
//	 */
//	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
//		
//		Preconditions.checkNotNull(serv);
//		
//		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {
//			
//			cP2PNetworkManager = Optional.absent();
//			LOG.debug("Unbound {} as a P2P network manager.", serv
//					.getClass().getSimpleName());
//			
//		}
//		
//	}
//	
	/**
	 * The Peer dictionary, if there is one bound.
	 */
	private static Optional<IPeerDictionary> cPeerDictionary = Optional.absent();
	
	/**
	 * Binds a Peer dictionary. <br />
	 * Called by OSGi-DS.
	 * @param serv The Peer dictionary to bind. <br />
	 * Must be not null.
	 */
	public static void bindPeerDictionary(IPeerDictionary serv) {
		
		Preconditions.checkNotNull(serv);
		cPeerDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a Peer dictionary.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a Peer dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The Peer dictionary to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cPeerDictionary.isPresent() && cPeerDictionary.get() == serv) {
			
			cPeerDictionary = Optional.absent();
			LOG.debug("Unbound {} as a Peer dictionary.", serv
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
		if(!cPeerDictionary.isPresent()) {
			
			LOG.error("No P2P dictionary bound!");
			return;
			
		} else if(!cRecoveryAllocator.isPresent()) {
			
			LOG.error("No recovery allocator bound!");
			return;
			
		} else if(!cRecoveryDynamicBackup.isPresent()) {
			
			LOG.error("No recovery dynamic backup bound!");
			return;
			
		}
		
		// Was this peer responsible for us? (Were we a buddy for him?) -> if so, search new buddies
		cRecoveryDynamicBackup.get().removeMyBuddyAndSearchNew(failedPeer);
		
		LOG.debug("Want to start recovery for {}", cPeerDictionary.get().getRemotePeerName(failedPeer));

		List<ID> sharedQueryIds = cRecoveryDynamicBackup.get().getSharedQueryIdsForRecovery(failedPeer);
		if (sharedQueryIds == null || sharedQueryIds.isEmpty()) {
			LOG.debug("Can't do recovery for {}. Don't have the information.", cPeerDictionary.get().getRemotePeerName(failedPeer));
		}
		
		// Reallocate each query to another peer
		
		for (ID sharedQueryId : sharedQueryIds) {
			LOG.debug("Have info for {} -> I can do recovery.", cPeerDictionary.get().getRemotePeerName(failedPeer));
			// 4. Search for another peer who can take the parts from the failed
			// peer

			// TODO find a good place to reallocate if the peer doesn't accept
			// the query or is unable to install it

			Map<ILogicalQueryPart, PeerID> allocationMap = null;
			
			if (!cRecoveryAllocator.isPresent()) {
				LOG.error("No allocator for recovery allocation set.");
			} else {
				try {
					// get a PeerID for allocation
					allocationMap = cRecoveryDynamicBackup.get().allocateToNewPeer(failedPeer, sharedQueryId, cRecoveryAllocator.get());
					if(allocationMap.values() != null){
						LOG.debug("Peer ID for recovery allocation found.");
					} else {
						LOG.debug("Unable to find Peer ID for recovery allocation.");
					}
					
				} catch (QueryPartAllocationException e) {
					// TODO reallocation
					LOG.error("Peer ID search for recovery allocation failed.");
				}
			}
			// use the found PeerID
			for(PeerID pid : allocationMap.values()){
				cRecoveryDynamicBackup.get().determineAndSendHoldOnMessages(sharedQueryId, failedPeer);

				// 5. Tell the new peer to install the parts from the failed peer
				cRecoveryDynamicBackup.get().initiateAgreement(failedPeer, sharedQueryId, pid);

				// 6. Update our sender so it knows the new peerId
				List<JxtaSenderPO<?>> affectedSenders = cRecoveryDynamicBackup.get().getAffectedSenders(failedPeer);
				for (int i = 0; i < affectedSenders.size(); i++) {
					affectedSenders.get(i).setPeerIDString(pid.toString());
				}
			}

		}

	}

	@Override
	public String getName() {
		return "upstreambackup";
	}

}
