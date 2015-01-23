package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import java.util.Collection;

import net.jxta.peer.PeerID;
import net.jxta.protocol.PeerAdvertisement;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface IPeerDictionary {
	
	public void addListener(IPeerDictionaryListener listener);
	public void removeListener(IPeerDictionaryListener listener);
	
	public ImmutableCollection<PeerID> getRemotePeerIDs();
	public Collection<PeerAdvertisement> getPeerAdvertisements();
	
	public String getRemotePeerName( PeerID peerID );
	public String getRemotePeerName( String peerIDString );
	
	public Optional<String> getRemotePeerAddress( PeerID peerID );

}