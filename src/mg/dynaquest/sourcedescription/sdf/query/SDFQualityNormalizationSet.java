package mg.dynaquest.sourcedescription.sdf.query;

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.quality.SDFQualityNormalization;

public class SDFQualityNormalizationSet {

    /**
	 * @uml.property  name="qualityNormalitzations"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="mg.dynaquest.sourcedescription.sdf.quality.SDFQualityNormalization"
	 */
    private ArrayList<SDFQualityNormalization> qualityNormalitzations;

	public SDFQualityNormalizationSet() {
		qualityNormalitzations = new ArrayList<SDFQualityNormalization>();
	}

	public void addPredicate(SDFQualityNormalization qNorm) {
		qualityNormalitzations.add(qNorm);
	}

	public SDFQualityNormalization getQNorm(int pos) {
		return (SDFQualityNormalization) qualityNormalitzations.get(pos);
	}

	public int getQNormCount() {
		return qualityNormalitzations.size();
	}

}