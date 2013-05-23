package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;
import net.jxta.peer.PeerID;

public interface IP2PDictionaryListener {

	void sourceAdded( IP2PDictionary sender, SourceAdvertisement advertisement );
	void sourceRemoved( IP2PDictionary sender, SourceAdvertisement advertisement );
	
	void sourceImported( IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName );
	void sourceImportRemoved( IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName );
	void sourceExported( IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName);
	void sourceExportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName);
	
	void peerAdded( IP2PDictionary sender, PeerID id );
	void peerRemoved( IP2PDictionary sender, PeerID id );
}
