package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;

public class P2PDictionaryAdapter implements IP2PDictionaryListener {

	@Override
	public void sourceAdded(IP2PDictionary sender, SourceAdvertisement advertisement) {

	}

	@Override
	public void sourceRemoved(IP2PDictionary sender, SourceAdvertisement advertisement) {

	}

	@Override
	public void sourceImported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		
	}

	@Override
	public void sourceExported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		
	}

	@Override
	public void peerAdded(IP2PDictionary sender, PeerID id, String name) {
		
	}

	@Override
	public void peerRemoved(IP2PDictionary sender, PeerID id, String name) {
		
	}

}
