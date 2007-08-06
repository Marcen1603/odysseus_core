package mg.dynaquest.sourcedescription.sdf.description;

import mg.dynaquest.sourcedescription.sdf.SDFElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;

/**
 * @author  Marco Grawunder
 */
public class SDFAttributeAttributeBindingPair extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8332356474402713433L;

	/**
	 * @uml.property  name="attribute"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFAttribute attribute = null;

    /**
	 * @uml.property  name="attributeBinding"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFAttributeBindung attributeBinding = null;


	public SDFAttributeAttributeBindingPair(String URI, SDFAttribute attribute,
			SDFAttributeBindung attributeBinding) {
		super(URI);
		this.attribute = attribute;
		this.attributeBinding = attributeBinding;
	}

	public String toString() {
		//StringBuffer ret = new StringBuffer("(" + this.attribute.getURI(true));
		StringBuffer ret = new StringBuffer("(" + this.attribute.toString());
		if (this.attributeBinding != null)
			ret.append("," + this.attributeBinding.toString());
		return ret.toString() + ")";
	}

    /**
     * 
     * @uml.property name="attribute"
     */
    public SDFAttribute getAttribute() {
        return attribute;
    }

    /**
     * 
     * @uml.property name="attributeBinding"
     */
    public SDFAttributeBindung getAttributeBinding() {
        return attributeBinding;
    }

    /**
	 * @param  newAttributeBinding
	 * @uml.property  name="attributeBinding"
	 */
    public void setAttributeBinding(SDFAttributeBindung attributeBinding) {
        this.attributeBinding = attributeBinding;
    }

}