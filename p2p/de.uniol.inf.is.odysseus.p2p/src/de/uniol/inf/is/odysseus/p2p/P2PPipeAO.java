package de.uniol.inf.is.odysseus.p2p;

import de.uniol.inf.is.odysseus.logicaloperator.base.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class P2PPipeAO extends UnaryLogicalOp implements OutputSchemaSettable{
	
	private static final long serialVersionUID = 4504556208585245530L;
	//PipeAdvertisement auf dem das Socket gebunden wird, um P2PAccessAOs mit Daten zu versorgen
	String adv;
	private SDFAttributeList outputSchema;

	public P2PPipeAO(String adv) {
		this.adv = adv;
	}

	public String getAdv() {
		return adv;
	}

	public void setAdv(String adv) {
		this.adv = adv;
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
