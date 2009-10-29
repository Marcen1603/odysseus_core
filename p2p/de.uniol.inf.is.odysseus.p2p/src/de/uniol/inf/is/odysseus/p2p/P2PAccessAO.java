package de.uniol.inf.is.odysseus.p2p;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class P2PAccessAO extends AbstractLogicalOperator implements OutputSchemaSettable{
	
	private static final long serialVersionUID = -3777262633714509945L;
	//Advertisement zu einer Quelle
	private String adv;
	private SDFAttributeList outputSchema;
	
	public P2PAccessAO(String adv) {
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
