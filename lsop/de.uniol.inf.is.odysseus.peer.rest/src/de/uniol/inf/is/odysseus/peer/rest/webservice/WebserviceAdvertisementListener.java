package de.uniol.inf.is.odysseus.peer.rest.webservice;


import java.util.HashMap;
import java.util.Map;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;



import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;


/**
 * Listener for WebserviceAdvertisement
 * @author Thore Stratmann
 *
 */
public class WebserviceAdvertisementListener implements IAdvertisementDiscovererListener {

	private Map<String, Integer> restPorts = new HashMap<String, Integer>();
	
	
	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if( advertisement instanceof WebserviceAdvertisement ) {
			WebserviceAdvertisement adv = (WebserviceAdvertisement)advertisement;			
			PeerID pid = adv.getPeerID();
			int restPort = adv.getRestPort();
			restPorts.put(pid.toString(), restPort);			
		}
	}

	@Override
	public void updateAdvertisements() {
	}
	
	public Integer getRestPort(PeerID peerID) {
		return this.restPorts.get(peerID.toString());
	}
}