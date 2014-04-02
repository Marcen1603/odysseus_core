package de.uniol.inf.is.odysseus.core.planmanagement;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

abstract public class AbstractResourceInformation {

	Resource name;
	SDFSchema outputSchema;
	
	public AbstractResourceInformation() {
	}
	
	public void setName(Resource name) {
		this.name = name;
	}
	
	public Resource getName() {
		return name;
	}
	
	public void setOutputSchema(SDFSchema outputSchema) {
		this.outputSchema = outputSchema;
	}
	
	public SDFSchema getOutputSchema() {
		return outputSchema;
	}
	
	@Override
	public String toString() {	
		return getName().toString();
	}

}
