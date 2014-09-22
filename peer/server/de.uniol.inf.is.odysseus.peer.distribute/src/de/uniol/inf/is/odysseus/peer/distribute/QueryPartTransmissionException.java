package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;

public class QueryPartTransmissionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final Collection<PeerID> faultyPeers = Lists.newArrayList();

	public QueryPartTransmissionException(Collection<PeerID> faultyPeers) {
		super();

		this.faultyPeers.addAll(faultyPeers);
	}

	public QueryPartTransmissionException(Collection<PeerID> faultyPeers, String message, Throwable cause) {
		super(message, cause);
		
		this.faultyPeers.addAll(faultyPeers);
	}

	public QueryPartTransmissionException(Collection<PeerID> faultyPeers, String message) {
		super(message);

		this.faultyPeers.addAll(faultyPeers);
	}

	public QueryPartTransmissionException(Collection<PeerID> faultyPeers, Throwable cause) {
		super(cause);

		this.faultyPeers.addAll(faultyPeers);
	}

	public Collection<PeerID> getFaultyPeers() {
		return faultyPeers;
	}
}
