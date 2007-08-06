package mg.dynaquest.sourcedescription.sdf.schema;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;

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
