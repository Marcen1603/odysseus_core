package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;
import net.jxta.peer.PeerID;

public interface IP2PDictionaryListener {

	void viewAdded( IP2PDictionary sender, ViewAdvertisement advertisement );
	void viewRemoved( IP2PDictionary sender, ViewAdvertisement advertisement );
	
	void viewImported( IP2PDictionary sender, ViewAdvertisement advertisement, String viewName );
	
	void peerAdded( IP2PDictionary sender, PeerID id );
	void peerRemoved( IP2PDictionary sender, PeerID id );
}
