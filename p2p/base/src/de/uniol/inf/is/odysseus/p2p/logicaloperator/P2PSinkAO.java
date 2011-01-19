package de.uniol.inf.is.odysseus.p2p.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class P2PSinkAO extends P2PAO{

	private static final long serialVersionUID = -4079953031372502548L;
	
	private String adv;
	
	public P2PSinkAO(String adv) {
		super(adv);
		this.adv = adv;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new P2PSinkAO(this.adv);
	}

}
