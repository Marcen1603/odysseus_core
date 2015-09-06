package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.communication;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.PriceCalculator;

public class PriceUpdaterThread extends Thread {
	
	
	private boolean isActive;
	private final int PRICE_UPDATE_INTERVAL;
	private IPeerDictionary peerDictionary;
	
	
	public PriceUpdaterThread(IPeerDictionary peerDictionary,int updateInterval) {
		this.peerDictionary = peerDictionary;
		this.PRICE_UPDATE_INTERVAL = updateInterval;
	}
	
	public void setInactive() {
		isActive = false;
	}
		
	
	@Override
	public void run() {
		isActive = true;
		while(isActive) {
			//No usage manager? Don't do anything...
			if(PriceCalculator.isUsageManagerBound()) {
				for(PeerID peer : peerDictionary.getRemotePeerIDs()) {
					PeerContractCommunicator.sendContractToPeer(peer);
				}
			}
			
			
			try {
				Thread.sleep(PRICE_UPDATE_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
