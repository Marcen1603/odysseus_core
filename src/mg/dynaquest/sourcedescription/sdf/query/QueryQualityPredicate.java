package mg.dynaquest.sourcedescription.sdf.query;

import mg.dynaquest.sourcedescription.sdf.quality.SDFQuality;

public class QueryQualityPredicate extends Weighted {

    /**
	 * @uml.property  name="qualityAspect"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    // Um welche Qualtiätseigenschaft geht es
    private SDFQuality qualityAspect;

    /**
	 * @uml.property  name="lowerBound"
	 */
    // Gibt es eine untere Schranke, und wenn ja welche
    private Double lowerBound;

    /**
	 * @uml.property  name="upperBound"
	 */
    // Gibt es eine obere Schranke, und wenn ja welche
    private Double upperBound;


	public QueryQualityPredicate(SDFQuality qualityAspect, Double lowerBound,
			Double upperBound, float weighting) {
		super(weighting);
		this.qualityAspect = qualityAspect;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public String toString() {
		return "(" + qualityAspect.toString() + ", " + lowerBound + ", "
				+ upperBound + ", " + this.getWeighting() + ")";
	}

    /**
     * 
     * @uml.property name="qualityAspect"
     */
    public void setQualityAspect(SDFQuality qualityAspect) {
        this.qualityAspect = qualityAspect;
    }

    /**
     * 
     * @uml.property name="qualityAspect"
     */
    public SDFQuality getQualityAspect() {
        return qualityAspect;
    }

    /**
     * 
     * @uml.property name="lowerBound"
     */
    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * 
     * @uml.property name="lowerBound"
     */
    public Double getLowerBound() {
        return lowerBound;
    }

    /**
     * 
     * @uml.property name="upperBound"
     */
    public void setUpperBound(Double upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * 
     * @uml.property name="upperBound"
     */
    public Double getUpperBound() {
        return upperBound;
    }

}