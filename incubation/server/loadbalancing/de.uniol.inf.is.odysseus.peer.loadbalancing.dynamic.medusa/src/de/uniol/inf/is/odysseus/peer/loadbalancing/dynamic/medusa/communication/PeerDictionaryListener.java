package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.communication;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionaryListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.ContractRegistry;

public class PeerDictionaryListener implements IPeerDictionaryListener {
	

	private IPeerDictionary peerDictionary;
	
	
	public void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
		peerDictionary.addListener(this);
	}
	
	public void unbindPeerDictionary(IPeerDictionary serv) {
		if(peerDictionary==serv) {
			peerDictionary.removeListener(this);
			peerDictionary = null;
		}
	}

	@Override
	public void peerAdded(PeerID peer) {
		if(!ContractRegistry.hasContract(peer)) {
			PeerContractCommunicator.requestContractFromPeer(peer);
		}
		
	}

	@Override
	public void peerRemoved(PeerID peer) {
		if(ContractRegistry.hasContract(peer)) {
			ContractRegistry.removeContract(peer);
		}
	}

	
}
