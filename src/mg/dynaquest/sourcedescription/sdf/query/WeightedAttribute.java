package mg.dynaquest.sourcedescription.sdf.query;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Ein gewichtetes Attribut weist einem existierenden Attribut eine Gewichtung
 * zu. Auf diese Weise k�nnen beispielsweise die R�ckgabeattribute in der
 * Anfrage modelliert werden.
 *  
 */

public class WeightedAttribute extends Weighted {

    /**
	 * @uml.property  name="attribute"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private SDFAttribute attribute;


	public WeightedAttribute(SDFAttribute attribute, float weighting) {
		super(weighting);
		this.attribute = attribute;
	}

    /**
     * 
     * @uml.property name="attribute"
     */
    public void setAttribute(SDFAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * 
     * @uml.property name="attribute"
     */
    public SDFAttribute getAttribute() {
        return attribute;
    }

	public String toString() {
		return "(" + attribute.toString() + ", " + this.getWeighting() + ")";
	}

}