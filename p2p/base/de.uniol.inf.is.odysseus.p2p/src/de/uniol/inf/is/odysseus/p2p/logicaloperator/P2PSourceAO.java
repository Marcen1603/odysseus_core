package de.uniol.inf.is.odysseus.p2p.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class P2PSourceAO extends P2PAO {

	private static final long serialVersionUID = -8494531216632839437L;
	SDFAttributeList outputSchema = null;

	public P2PSourceAO(String adv) {
		super(adv);
	}

	public P2PSourceAO(P2PSourceAO p2pSourceAO) {
		super(p2pSourceAO);
		this.outputSchema = p2pSourceAO.outputSchema.clone();		
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	
	@Override
	public P2PSourceAO clone() {
		return new P2PSourceAO(this);
	}


}
