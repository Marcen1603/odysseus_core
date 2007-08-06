package mg.dynaquest.sourcedescription.sdf.schema;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;

public class SDFRangeConstraint extends SDFDatatypeConstraint {

	private static final long serialVersionUID = -664452157949709859L;
	private SDFIntervall range;

	public SDFRangeConstraint(String URI, SDFIntervall range) {
		super(URI, SDFDatatypeConstraints.hasRange);
		this.range = range;
	}

	public SDFIntervall getRange() {
		return range;
	}

}
