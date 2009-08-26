package de.uniol.inf.is.odysseus.p2p;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;


public class P2PPipeAO extends UnaryLogicalOp{
	
	private static final long serialVersionUID = 4504556208585245530L;
	//PipeAdvertisement auf dem das Socket gebunden wird, um P2PAccessAOs mit Daten zu versorgen
	String adv;

	public P2PPipeAO(String adv) {
		this.adv = adv;
	}

	public String getAdv() {
		return adv;
	}

	public void setAdv(String adv) {
		this.adv = adv;
	}
	
}
