package mg.dynaquest.sourcedescription.sdf.schema;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;

public class SDFGranularityConstraint extends SDFDatatypeConstraint {

	private static final long serialVersionUID = -3795271035835243780L;
	private boolean isInt;

	public SDFGranularityConstraint(String URI, boolean isInt) {
		super(URI, SDFDatatypeConstraints.hasGranularity);
		this.isInt = isInt;
	}

	public boolean isInt() {
		return isInt;
	}

}
