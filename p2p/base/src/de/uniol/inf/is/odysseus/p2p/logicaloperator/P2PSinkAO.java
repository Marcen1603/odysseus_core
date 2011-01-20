package de.uniol.inf.is.odysseus.p2p.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class P2PSinkAO extends P2PAO{

	private static final long serialVersionUID = -4079953031372502548L;
		
	public P2PSinkAO(String adv) {
		super(adv);
	}

	public P2PSinkAO(P2PSinkAO p2pSinkAO) {
		super(p2pSinkAO);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new P2PSinkAO(this);
	}

}
