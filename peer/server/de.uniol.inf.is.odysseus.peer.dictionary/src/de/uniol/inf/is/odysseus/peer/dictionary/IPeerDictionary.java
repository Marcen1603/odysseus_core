package de.uniol.inf.is.odysseus.peer.dictionary;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface IPeerDictionary {
	
	public void addListener(IPeerDictionaryListener listener);
	
	public void removeListener(IPeerDictionaryListener listener);
	
	public ImmutableCollection<PeerID> getRemotePeerIDs();
	
	public String getRemotePeerName( PeerID peerID );
	
	public String getRemotePeerName( String peerIDString );
	
	public Optional<String> getRemotePeerAddress( PeerID peerID );

}