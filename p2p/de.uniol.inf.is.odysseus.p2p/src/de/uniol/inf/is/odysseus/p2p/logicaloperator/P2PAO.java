package de.uniol.inf.is.odysseus.p2p.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;

abstract public class P2PAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 2451774121232984603L;
	private String adv;
	
	public P2PAO(String adv) {
		this.adv = adv;
		System.out.println("Advertisement im P2PAO Stream" +this.adv.toString());
	}

	public String getAdv() {
		return adv;
	}

	public void setAdv(String adv) {
		this.adv = adv;
	}

}
