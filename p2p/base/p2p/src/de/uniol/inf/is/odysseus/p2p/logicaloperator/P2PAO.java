package de.uniol.inf.is.odysseus.p2p.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;

abstract public class P2PAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 2451774121232984603L;
	private String adv;
	
	public P2PAO(String adv) {
		this.adv = adv;
	}

	public P2PAO(P2PAO p2pAO) {
		this.adv = p2pAO.adv;
	}

	public String getAdv() {
		return adv;
	}

	public void setAdv(String adv) {
		this.adv = adv;
	}

}
