package de.uniol.inf.is.odysseus.peer.rest.webservice;

import java.io.IOException;
import java.util.Collection;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionaryListener;
import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.network.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class WebserviceAdvertisementPeerListener implements IPeerDictionaryListener, IAdvertisementDiscovererListener {
	
	private IPeerDictionary peerDictionary;
	private IJxtaServicesProvider jxtaServicesProvider;
	private IP2PNetworkManager p2pNetworkManager;
	
	@Override
	public void peerAdded(PeerID peer) {
		
	}

	@Override
	public void peerRemoved(PeerID peer) {
		removeAdv(peer);
	}
	
	private void removeAdv(PeerID peer){
		if(jxtaServicesProvider == null){
			return;
		}
		Collection<WebserviceAdvertisement> webAdvs = jxtaServicesProvider.getLocalAdvertisements(WebserviceAdvertisement.class);
		for (WebserviceAdvertisement adv : webAdvs) {
			if(((WebserviceAdvertisement) adv).getPeerID().equals(peer)){
				try {
					waitForJxtaServicesProvider(jxtaServicesProvider);
					jxtaServicesProvider.flushAdvertisement(adv);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void bindPeerDictionary(IPeerDictionary peerDictionary){
		this.peerDictionary = peerDictionary;
		if(this.peerDictionary != null){
			peerDictionary.addListener(this);
		}
	}
	
	public void unbindPeerDictionary(IPeerDictionary peerDictionary){
		if(this.peerDictionary != null){
			peerDictionary.removeListener(this);
		}
		peerDictionary = null;
	}
	
	public void bindJxtaServicesProvider(IJxtaServicesProvider jxtaServicesProvider){
		this.jxtaServicesProvider = jxtaServicesProvider;
	}

	public void unbindJxtaServicesProvider(IJxtaServicesProvider jxtaServicesProvider){
		this.jxtaServicesProvider = null;
	}
	
	public void bindP2PNetworkManager(IP2PNetworkManager p2pNetworkManager){
		this.p2pNetworkManager = p2pNetworkManager;
		if(this.p2pNetworkManager != null){
			this.p2pNetworkManager.addAdvertisementListener(this);
		}
	}
	
	public void unbindP2PNetworkManager(IP2PNetworkManager p2pNetworkManager){
		this.p2pNetworkManager = null;
	}

	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		
	}

	@Override
	public void updateAdvertisements() {
		if(jxtaServicesProvider == null || p2pNetworkManager == null){
			return;
		}
		Collection<PeerID> peerIDs = this.peerDictionary.getRemotePeerIDs();
		Collection<WebserviceAdvertisement> webAdvs = jxtaServicesProvider.getLocalAdvertisements(WebserviceAdvertisement.class);
		for (WebserviceAdvertisement adv : webAdvs) {
			if(!peerIDs.contains(adv.getPeerID())){
				if(!p2pNetworkManager.getLocalPeerID().equals(adv.getPeerID())){
					try {
						waitForJxtaServicesProvider(jxtaServicesProvider);
						jxtaServicesProvider.flushAdvertisement(adv);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static void waitForJxtaServicesProvider(IJxtaServicesProvider provider) {
		while( !provider.isActive() ) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
}
