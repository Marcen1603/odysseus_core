package de.uniol.inf.is.odysseus.sourcedescription.sdf.quality;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class SDFSourceQualityRating {

    /**
	 * @uml.property  name="source"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFSource source = null;

    /**
	 * @uml.property  name="qualityAspect"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFQuality qualityAspect = null;

    /**
	 * @uml.property  name="qualityLevel"
	 */
    double qualityLevel = -1;


	public SDFSourceQualityRating(SDFSource source, SDFQuality qualityAspect,
			double qualityLevel) {

		this.source = source;
		this.qualityAspect = qualityAspect;
		this.qualityLevel = qualityLevel;
	}

    /**
     * 
     * @uml.property name="source"
     */
    public void setSource(SDFSource source) {
        this.source = source;
    }

    /**
     * 
     * @uml.property name="source"
     */
    public SDFSource getSource() {
        return source;
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
     * @uml.property name="qualityLevel"
     */
    public void setQualityLevel(double qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    /**
     * 
     * @uml.property name="qualityLevel"
     */
    public double getQualityLevel() {
        return qualityLevel;
    }

	public String toString() {
		return "(" + source.toString() + ", " + qualityAspect.toString() + ", "
				+ qualityLevel + ")";
	}

}