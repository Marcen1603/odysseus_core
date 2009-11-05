package de.uniol.inf.is.odysseus.p2p;

import de.uniol.inf.is.odysseus.logicaloperator.base.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class P2PSourceAO extends P2PAO implements OutputSchemaSettable {

	private static final long serialVersionUID = -8494531216632839437L;
	SDFAttributeList outputSchema = null;

	public P2PSourceAO(String adv) {
		super(adv);
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

}
