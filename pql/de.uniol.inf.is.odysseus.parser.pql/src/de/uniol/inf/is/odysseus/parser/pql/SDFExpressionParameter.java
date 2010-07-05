package de.uniol.inf.is.odysseus.parser.pql;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class SDFExpressionParameter extends AbstractParameter<SDFExpression> {

	public SDFExpressionParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	public void setValueOf(Object object) {
		setValue(new SDFExpression("", (String) object, getAttributeResolver()));
	}

}
