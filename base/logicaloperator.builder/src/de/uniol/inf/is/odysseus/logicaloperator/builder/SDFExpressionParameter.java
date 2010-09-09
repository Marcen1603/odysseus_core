package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class SDFExpressionParameter extends AbstractParameter<SDFExpression> {

	public SDFExpressionParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	protected void internalAssignment() {
		if (getAttributeResolver() != null) {
			setValue(new SDFExpression("", (String)inputValue,
					getAttributeResolver()));
		} else {
			throw new RuntimeException(
					"missing expression or attribute resolver");
		}
	}

}
