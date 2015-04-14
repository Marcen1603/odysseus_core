package de.uniol.inf.is.odysseus.peer.dictionary;

import net.jxta.peer.PeerID;

public interface IPeerDictionaryListener {
	
	public void peerAdded(PeerID peer);
	
	public void peerRemoved(PeerID peer);

}