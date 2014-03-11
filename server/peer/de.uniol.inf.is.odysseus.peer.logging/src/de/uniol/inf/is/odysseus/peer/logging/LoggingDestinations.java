package de.uniol.inf.is.odysseus.peer.logging;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class LoggingDestinations {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingDestinations.class);
	private static final LoggingDestinations INSTANCE = new LoggingDestinations();
	
	private final Collection<PeerID> destinations = Lists.newArrayList();
	
	private LoggingDestinations() {
		
	}
	
	public static LoggingDestinations getInstance() {
		return INSTANCE;
	}
	
	public synchronized void add( PeerID peerID ) {
		Preconditions.checkNotNull(peerID, "PeerID must not be null!");
		
		if( !destinations.contains(peerID)) {
			LOG.debug("Added destination {}", peerID);
			destinations.add(peerID);
		}
	}
	
	public synchronized void remove( PeerID peerID ) {
		Preconditions.checkNotNull(peerID, "peerID must not be null!");
		
		if( destinations.contains(peerID)) {
			destinations.remove(peerID);
			LOG.debug("Removed destination {}", peerID);
		}
	}
	
	public synchronized Collection<PeerID> getAll() {
		return Lists.newArrayList(destinations);
	}
}
