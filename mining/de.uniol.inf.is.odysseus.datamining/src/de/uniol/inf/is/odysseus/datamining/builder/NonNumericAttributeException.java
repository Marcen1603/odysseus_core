package de.uniol.inf.is.odysseus.datamining.builder;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class NonNumericAttributeException extends Exception {
/**
	 * 
	 */
	private static final long serialVersionUID = -5590493141116859811L;
private SDFAttribute attribute;
public NonNumericAttributeException(SDFAttribute attribute) {
	this.attribute = attribute;
}
@Override
	public String getMessage() {
		
		return "The datatype of "+ attribute.getAttributeName()+" has to be numeric.";
	}
}
