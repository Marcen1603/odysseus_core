package de.uniol.inf.is.odysseus.p2p;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class P2PSinkAO extends P2PAO {

	private static final long serialVersionUID = -4079953031372502548L;
	
	public P2PSinkAO(String adv) {
		super(adv);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

}
