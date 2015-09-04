package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.medusa.datamodel;

import net.jxta.peer.PeerID;

public class MedusaPricingContract {
	private final PeerID contractPartner;

	private final double price;
	
	public PeerID getContractPartner() {
		return contractPartner;
	}

	public double getPrice() {
		return price;
	}


	
	
	public MedusaPricingContract(PeerID contractPartner, double price) {
		this.contractPartner = contractPartner;
		this.price = price;
		
	}
}
