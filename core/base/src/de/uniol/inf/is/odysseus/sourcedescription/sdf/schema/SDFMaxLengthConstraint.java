package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;

public class SDFMaxLengthConstraint extends SDFDatatypeConstraint {

	private static final long serialVersionUID = 2626479781515770558L;
	private int maxLength;

	public SDFMaxLengthConstraint(String URI, int maxLength) {
		super(URI, SDFDatatypeConstraints.hasMaxLength);
		this.maxLength = maxLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

}
