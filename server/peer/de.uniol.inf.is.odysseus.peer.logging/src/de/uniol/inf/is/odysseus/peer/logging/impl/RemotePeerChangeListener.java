package de.uniol.inf.is.odysseus.peer.logging.impl;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;

public class RemotePeerChangeListener extends P2PDictionaryAdapter {

	@Override
	public void remotePeerAdded(IP2PDictionary sender, PeerID id, String name) {
		LoggingDestinations.getInstance().addKnownPeer(id);
	}

	@Override
	public void remotePeerRemoved(IP2PDictionary sender, PeerID id, String name) {
		LoggingDestinations.getInstance().removeKnownPeer(id);
	}

}
