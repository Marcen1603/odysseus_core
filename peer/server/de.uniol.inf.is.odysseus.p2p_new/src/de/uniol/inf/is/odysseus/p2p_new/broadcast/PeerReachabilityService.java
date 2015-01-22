package de.uniol.inf.is.odysseus.p2p_new.broadcast;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerReachabilityService;
import de.uniol.inf.is.odysseus.p2p_new.PeerReachabilityInfo;

public class PeerReachabilityService implements IPeerReachabilityService {

	private static PeerReachabilityService instance;
	
	private final Map<PeerID, PeerReachabilityInfo> infoMap = Maps.newHashMap();
	
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
	
	void setReachabilityMap( Map<PeerID, PeerReachabilityInfo> map ) {
		synchronized( infoMap ) {
			infoMap.clear();
			infoMap.putAll(map);
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
}
