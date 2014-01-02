package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.util.Collection;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;

import de.uniol.inf.is.odysseus.peer.resource.IPingTable;

public class PingTable implements IPingTable {

	private final Table<PeerID, PeerID, Long> pingTable = HashBasedTable.create();
	
	@Override
	public Collection<PeerID> getPeerIDs() {
		return ImmutableSet.copyOf(pingTable.rowKeySet());
	}

	public void setPing( PeerID source, PeerID destination, long ping ) {
		pingTable.put(source, destination, ping);
	}
	
	public void removePing( PeerID source, PeerID destination ) {
		pingTable.remove(source, destination);
	}

	@Override
	public Optional<Long> getPing(PeerID source, PeerID destination) {
		return Optional.fromNullable(pingTable.get(source, destination));
	}

}
