package de.uniol.inf.is.odysseus.scars.base;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class JDVEAccessMVPOAsList<M extends IProbability> extends JDVEAccessMVPO<M> {

	private SDFAttributeList outputSchema;
	
	public JDVEAccessMVPOAsList(int pPort) {
		super(pPort);
//		this.outputSchema = outputSchema;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.outputSchema;
	}

	@Override
	public void transferNext() {
		if (buffer != null) {
			transfer(buffer);
		}
		
		buffer = null;
	}
}
