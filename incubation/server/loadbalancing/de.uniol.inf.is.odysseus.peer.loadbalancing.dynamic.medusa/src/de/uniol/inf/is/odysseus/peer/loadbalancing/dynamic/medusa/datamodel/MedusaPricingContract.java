package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel;

import net.jxta.peer.PeerID;

public class MedusaPricingContract {
	private final PeerID contractPartner;

	private final double minPrice;
	private final double maxPrice;
	
	public PeerID getContractPartner() {
		return contractPartner;
	}


	public double getMinPrice() {
		return minPrice;
	}


	public double getMaxPrice() {
		return maxPrice;
	}


	
	
	public MedusaPricingContract(PeerID contractPartner, double minPrice, double maxPrice) {
		this.contractPartner = contractPartner;
		this.maxPrice = maxPrice;
		this.minPrice = minPrice;
		
	}
}
