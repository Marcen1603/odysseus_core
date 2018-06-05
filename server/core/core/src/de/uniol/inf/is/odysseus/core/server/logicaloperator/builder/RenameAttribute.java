package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public class RenameAttribute {

	private SDFAttribute attribute;
	private String newName;
	
	public RenameAttribute(SDFAttribute attribute, String newName) {
		this.setAttribute(attribute);
		this.setNewName(newName);
	}

	public SDFAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(SDFAttribute attribute) {
		this.attribute = attribute;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
}
