package mg.dynaquest.sourcedescription.sdf.quality;

import mg.dynaquest.sourcedescription.sdf.SDFElement;

public class SDFQualityPair extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3225498771977738003L;

	/**
	 * @uml.property  name="qualityProperty"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFQuality qualityProperty = null;

    /**
	 * @uml.property  name="value"
	 */
    Object value = null;


	public SDFQualityPair(String URI, SDFQuality qualityProperty, Object value) {
		super(URI);
		this.qualityProperty = qualityProperty;
		this.value = value;
	}

    /**
     * 
     * @uml.property name="qualityProperty"
     */
    public void setQualityProperty(SDFQuality qualityProperty) {
        this.qualityProperty = qualityProperty;
    }

    /**
     * 
     * @uml.property name="qualityProperty"
     */
    public SDFQuality getQualityProperty() {
        return qualityProperty;
    }

    /**
     * 
     * @uml.property name="value"
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 
     * @uml.property name="value"
     */
    public Object getValue() {
        return value;
    }

}