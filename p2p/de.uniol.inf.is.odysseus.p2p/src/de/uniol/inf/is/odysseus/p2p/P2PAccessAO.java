package de.uniol.inf.is.odysseus.p2p;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;


public class P2PAccessAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = -3777262633714509945L;
	//Advertisement zu einer Quelle
	private String adv;
	
	public P2PAccessAO(String adv) {
		this.adv = adv;
	}

	public String getAdv() {
		return adv;
	}

	public void setAdv(String adv) {
		this.adv = adv;
	}
}
