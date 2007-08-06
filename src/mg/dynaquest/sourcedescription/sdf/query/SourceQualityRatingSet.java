package mg.dynaquest.sourcedescription.sdf.query;

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.quality.SDFSourceQualityRating;

public class SourceQualityRatingSet {

    /**
	 * @uml.property  name="ratings"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="mg.dynaquest.sourcedescription.sdf.quality.SDFSourceQualityRating"
	 */
    private ArrayList<SDFSourceQualityRating> ratings;

	public SourceQualityRatingSet() {
		ratings = new ArrayList<SDFSourceQualityRating>();
	}

	public void addRating(SDFSourceQualityRating rating) {
		ratings.add(rating);
	}

	public SDFSourceQualityRating getrating(int pos) {
		return (SDFSourceQualityRating) ratings.get(pos);
	}

	public int getratingCount() {
		return ratings.size();
	}
}