package de.uniol.inf.is.odysseus.parser.pql;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class SDFAttributeParameter extends AbstractParameter<SDFAttribute> {

	public SDFAttributeParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	public void setValueOf(Object object) {
		SDFAttribute attribute = getAttributeResolver().getAttribute(
				(String) object);
		setValue(attribute);
	}

}
