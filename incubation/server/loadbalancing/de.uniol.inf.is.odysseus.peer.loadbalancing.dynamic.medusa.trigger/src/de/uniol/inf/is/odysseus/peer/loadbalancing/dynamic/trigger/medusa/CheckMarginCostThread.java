package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.trigger.medusa;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.PriceCalculator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.ContractRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel.MedusaPricingContract;

public class CheckMarginCostThread extends Thread {
	
	private IMedusaTriggerListener callback;
	
	
	public CheckMarginCostThread(IMedusaTriggerListener callback) {
		this.callback = callback;
	}
	
	private final int UPDATE_INTERVAL = 10000;

	boolean isActive;
	
	@Override
	public void run() {
		isActive = true;
		while(isActive) {
			try {
				Thread.sleep(UPDATE_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(isActive) {
				double marginCost = PriceCalculator.calcCurrentMarginCost();
				PeerID cheapestPeer = ContractRegistry.getCheapestOffer();
				if(cheapestPeer!=null) {
					MedusaPricingContract contract = ContractRegistry.getContractforPeer(cheapestPeer);
				
					if(marginCost>contract.getPrice()) {
						callback.loadBalancingTriggered();
					}
				}
			}
			
		}
	}
	
	public void setInactive() {
		this.isActive = false;
	}
	
}
