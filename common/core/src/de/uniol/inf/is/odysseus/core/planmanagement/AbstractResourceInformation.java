package de.uniol.inf.is.odysseus.core.planmanagement;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

abstract public class AbstractResourceInformation {

	Resource name;
	String type;
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

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return getName().toString();
	}

}
