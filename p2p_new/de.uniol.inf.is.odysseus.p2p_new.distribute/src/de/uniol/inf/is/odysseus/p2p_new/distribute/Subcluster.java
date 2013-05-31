package de.uniol.inf.is.odysseus.p2p_new.distribute;

import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

// TODO javaDoc
public class Subcluster {
	
	private static int SUBCLUSTER_ID_COUNTER = 0;
	
	private final int subclusterID;
	
	public final int getSubClusterID() {
		
		return subclusterID;
		
	}
	
	/**
	 * Returns the base name for acceptor operators.
	 */
	public String getAccessName() {
		
		return "JxtaReceiver_" + subclusterID + "_";
		
	}

	/**
	 * Returns the base name for sender operators.
	 */
	public String getSenderName() {
		
		return "JxtaSender_" + subclusterID + "_";
		
	}
	
	private Map<QueryPart, PeerID> subclusterMap;
	
	public final ImmutableMap<QueryPart, PeerID> getSubclusterMap() {
		
		return ImmutableMap.copyOf(subclusterMap);
		
	}
	
	private Subcluster next;
	
	public final Optional<Subcluster> getNext() {
		
		return Optional.fromNullable(next);
		
	}
	
	public void setNext(Subcluster next) {
		
		this.next = next;
		
	}
	
	public Subcluster(Map<QueryPart, PeerID> subclusterMap) {
		
		Preconditions.checkArgument(!subclusterMap.isEmpty(), "Subcluster map must not be empty!");
		
		this.subclusterID = SUBCLUSTER_ID_COUNTER++;
		this.subclusterMap = subclusterMap;
		this.next = null;
		
	}

}