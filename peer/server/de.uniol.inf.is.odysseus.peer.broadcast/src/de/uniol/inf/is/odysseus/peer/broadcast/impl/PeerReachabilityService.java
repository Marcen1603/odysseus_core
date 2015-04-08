package de.uniol.inf.is.odysseus.peer.broadcast.impl;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.broadcast.IPeerReachabilityService;
import de.uniol.inf.is.odysseus.peer.broadcast.PeerReachabilityInfo;

public class PeerReachabilityService implements IPeerReachabilityService {

	private static final Logger LOG = LoggerFactory.getLogger(PeerReachabilityService.class);
	
	private static PeerReachabilityService instance;
	
	private final Map<PeerID, PeerReachabilityInfo> infoMap = Maps.newHashMap();
	private final Collection<IPeerReachabilityListener> listeners = Lists.newArrayList();
	
	// called by OSGi-DS
	public void activate() {
		instance = this;
	}
	
	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}
	
	public static boolean isActivated() {
		return instance != null;
	}
	
	public static PeerReachabilityService getInstance() {
		return instance;
	} 
	
	public static void waitFor() {
		while( !isActivated ()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
	
	void setReachabilityMap( Map<PeerID, PeerReachabilityInfo> newMap ) {
		
		Map<PeerID, PeerReachabilityInfo> oldMap = Maps.newHashMap();
		
		synchronized( infoMap ) {
			oldMap.putAll(infoMap);
			
			infoMap.clear();
			infoMap.putAll(newMap);
		}
		
		for( PeerID newPeerID : newMap.keySet() ) {
			if( oldMap.containsKey(newPeerID)) {
				// already known
				oldMap.remove(newPeerID);
			} else {
				firePeerReachableEvent(newMap.get(newPeerID));
			}
		}
		
		for( PeerID oldPeerID : oldMap.keySet() ) {
			firePeerNotReachableEvent(oldMap.get(oldPeerID));
		}
	}
	
	@Override
	public boolean isPeerReachable(PeerID peerID) {
		return infoMap.containsKey(peerID);
	}
	
	@Override
	public Optional<PeerReachabilityInfo> getReachabilityInfo(PeerID peerID) {
		return Optional.fromNullable(infoMap.get(peerID));
	}

	@Override
	public Collection<PeerID> getReachablePeers() {
		return Lists.newArrayList(infoMap.keySet());
	}
	
	@Override
	public void addListener(IPeerReachabilityListener listener) {
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	@Override
	public void removeListener(IPeerReachabilityListener listener) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	private void firePeerReachableEvent( PeerReachabilityInfo info ) {
		synchronized( listeners ) {
			for( IPeerReachabilityListener listener : listeners ) {
				try {
					listener.peerReachable(info);
				} catch( Throwable t ) {
					LOG.error("Exception in peer reachability listener", t);
				}
			}
		}
	}
	
	private void firePeerNotReachableEvent( PeerReachabilityInfo info ) {
		synchronized( listeners ) {
			for( IPeerReachabilityListener listener : listeners ) {
				try {
					listener.peerNotReachable(info);
				} catch( Throwable t ) {
					LOG.error("Exception in peer reachability listener", t);
				}
			}
		}
	}
}
