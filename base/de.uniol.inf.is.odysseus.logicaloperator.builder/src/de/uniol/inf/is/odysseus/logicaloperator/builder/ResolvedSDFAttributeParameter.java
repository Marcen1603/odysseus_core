package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class ResolvedSDFAttributeParameter extends
		AbstractParameter<SDFAttribute> {

	public ResolvedSDFAttributeParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	protected void internalAssignment() {
		if (getAttributeResolver() == null) {
			throw new RuntimeException("missing attribute resolver");
		}
		SDFAttribute attribute = getAttributeResolver().getAttribute(
				(String) this.inputValue);
		setValue(attribute);
	}

}
