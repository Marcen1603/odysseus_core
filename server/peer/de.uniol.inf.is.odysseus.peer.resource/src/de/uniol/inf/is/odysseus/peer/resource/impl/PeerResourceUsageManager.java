package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.util.Collection;
import java.util.Map;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManagerListener;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public final class PeerResourceUsageManager implements IPeerResourceUsageManager, IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceUsageCheckThread.class);
	
	private static PeerResourceUsageManager instance;

	private final Map<PeerID, IResourceUsage> usageMap = Maps.newHashMap();
	private final Collection<IPeerResourceUsageManagerListener> listeners = Lists.newArrayList();
	
	private IResourceUsage localUsage;
	
	private static IP2PNetworkManager p2pNetworkManager;

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
	public void activate() {
		AdvertisementFactory.registerAdvertisementInstance(ResourceUsageAdvertisement.getAdvertisementType(), new ResourceUsageAdvertisementInstantiator());

		instance = this;
		LOG.debug("Activated");
	}
	
	// called by OSGi-DS
	public void deactivate() {
		instance = null;
		LOG.debug("Deactivated");
	}
	
	public static PeerResourceUsageManager getInstance() {
		return instance;
	}
	
	@Override
	public Optional<IResourceUsage> getRemoteResourceUsage(PeerID peerID) {
		Preconditions.checkNotNull( peerID, "PeerID to get current resource usage must not be null!");
				
		IResourceUsage resourceUsage = usageMap.get(peerID);
		return Optional.fromNullable(resourceUsage);
	}

	@Override
	public boolean hasRemoteResourceUsage(PeerID peerID) {
		return usageMap.containsKey(peerID);
	}

	@Override
	public Collection<PeerID> getRemotePeers() {
		return Lists.newArrayList(usageMap.keySet());
	}

	@Override
	public Optional<IResourceUsage> getLocalResourceUsage() {
		return Optional.fromNullable(localUsage);
	}
	
	void setLocalResourceUsage( IResourceUsage localUsage ) {
		Preconditions.checkNotNull(localUsage);
		
		this.localUsage = localUsage;
		fireChangeEvent(localUsage);
	}
	
	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if( adv instanceof ResourceUsageAdvertisement ) {
			ResourceUsageAdvertisement resourceUsageAdv = (ResourceUsageAdvertisement)adv;
			IResourceUsage usage = resourceUsageAdv.getResourceUsage();
			PeerID peerID = usage.getPeerID();
			if( !peerID.equals(p2pNetworkManager.getLocalPeerID())) {
				LOG.debug("Got resource usage {} from peerid {}", usage, peerID);
				
				if( usageMap.containsKey(peerID)) {
					IResourceUsage oldUsage = usageMap.get(peerID);
					if( isOlder(oldUsage, usage)) {
						usageMap.put(peerID, usage);
						fireChangeEvent(usage);
					}
				} else {
					usageMap.put(peerID, usage);
					fireChangeEvent(usage);
				}
			}
		}
	}

	private void fireChangeEvent(IResourceUsage usage) {
		synchronized( listeners ) {
			for( IPeerResourceUsageManagerListener listener : listeners ) {
				try {
					if( p2pNetworkManager.getLocalPeerID().equals(usage.getPeerID()) ) {
						listener.localResourceUsageChanged(this, usage);
					} else {
						listener.remoteResourceUsageChanged(this, usage);
					}
				} catch( Throwable t ) {
					LOG.error("Exception in peer resource usage manager listener", t);
				}
			}
		}
	}

	private static boolean isOlder(IResourceUsage oldUsage, IResourceUsage usage) {
		return oldUsage.getTimestamp() < usage.getTimestamp();
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		// do nothing
	}

	@Override
	public void addListener(IPeerResourceUsageManagerListener listener) {
		Preconditions.checkNotNull(listener, "Peer resoucre usage manager listener must not be null!");
		
		synchronized(listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IPeerResourceUsageManagerListener listener) {
		Preconditions.checkNotNull(listener, "Peer resoucre usage manager listener must not be null!");
		
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
}
