package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;
import net.jxta.peer.PeerID;

public interface IP2PDictionaryListener {

	void publishedViewAdded( IP2PDictionary sender, ViewAdvertisement advertisement );
	void publishedViewRemoved( IP2PDictionary sender, ViewAdvertisement advertisement );
	
	void peerAdded( IP2PDictionary sender, PeerID id );
	void peerRemoved( IP2PDictionary sender, PeerID id );
}
