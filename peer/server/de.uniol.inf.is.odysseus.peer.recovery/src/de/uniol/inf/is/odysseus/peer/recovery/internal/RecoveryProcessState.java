package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

public class RecoveryProcessState {
	
	private UUID identifier;
	private PeerID failedPeerID;
	private List<ILogicalQueryPart> notProcessedQueryParts;
	private Map<ID ,Map<ILogicalQueryPart, List<PeerID>>> inadequatePeersForQueryParts;
	
	public RecoveryProcessState(PeerID failedPeerID){
		this.failedPeerID = failedPeerID;
		inadequatePeersForQueryParts = new HashMap<ID, Map< ILogicalQueryPart, List<PeerID>>>();
		identifier = UUID.randomUUID();
	}
	
	public UUID getIdentifier(){
		return identifier;
	}
	
	public PeerID getFailedPeerId(){
		return failedPeerID;
	}
	
	public List<PeerID> getInadequatePeers(ID sharedQueryID, ILogicalQueryPart queryPart){
		if (inadequatePeersForQueryParts.containsKey(sharedQueryID)){
			if (inadequatePeersForQueryParts.get(sharedQueryID).containsKey(queryPart)){
				inadequatePeersForQueryParts.get(sharedQueryID).get(queryPart);
			} 
		}
		return new ArrayList<PeerID>();
	}
	
	public void addInadequatePeer(PeerID peerId, ID sharedQueryID, ILogicalQueryPart queryPart){
		if (!inadequatePeersForQueryParts.containsKey(sharedQueryID)){
			inadequatePeersForQueryParts.put(sharedQueryID, new HashMap<ILogicalQueryPart, List<PeerID>>());
		}
		if (!inadequatePeersForQueryParts.get(sharedQueryID).containsKey(queryPart)){
			inadequatePeersForQueryParts.get(sharedQueryID).put(queryPart, new ArrayList<PeerID>());
		}
		
		inadequatePeersForQueryParts.get(sharedQueryID).get(queryPart).add(failedPeerID);
	}

	public void queryPartIsProcessed(ILogicalQueryPart queryPart){
		notProcessedQueryParts.remove(queryPart);
	}
	
	public List<ILogicalQueryPart> getCurrentlyNotProcessedQueryParts() {
		return notProcessedQueryParts;
	}

	public void initNotProcessedQueryParts(List<ILogicalQueryPart> notProcessedQueryParts) {
		this.notProcessedQueryParts = notProcessedQueryParts;
	}
	
}

