package de.uniol.inf.is.odysseus.scars.testdata.provider;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface IProvider {

	public void init();
	
	public Object nextTuple();
	
	public SDFAttributeList getSchema(String sourceName);
	
}
