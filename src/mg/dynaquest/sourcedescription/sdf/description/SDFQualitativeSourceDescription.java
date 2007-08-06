package mg.dynaquest.sourcedescription.sdf.description;

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.SDFElement;
import mg.dynaquest.sourcedescription.sdf.quality.SDFQualityPair;

public class SDFQualitativeSourceDescription extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -753955215474820017L;
	/**
	 * @uml.property  name="qualityPairs"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="mg.dynaquest.sourcedescription.sdf.quality.SDFQualityPair"
	 */
    ArrayList<SDFQualityPair> qualityPairs = new ArrayList<SDFQualityPair>();

	public SDFQualitativeSourceDescription(String URI) {
		super(URI);
	}

	public void addQualityPair(SDFQualityPair qualPair) {
		this.qualityPairs.add(qualPair);
	}

	public int getNoOfQualityPairs() {
		return this.qualityPairs.size();
	}

	public SDFQualityPair getQualityPair(int index) {
		return (SDFQualityPair) this.qualityPairs.get(index);
	}
}