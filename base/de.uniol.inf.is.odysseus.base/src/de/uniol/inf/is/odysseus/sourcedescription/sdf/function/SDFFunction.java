package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFFunction extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7664982077640439530L;
	/**
	 * @uml.property  name="reverseFunction"
	 * @uml.associationEnd  
	 */
    SDFFunction reverseFunction = null;


	public SDFFunction(String URI) {
		super(URI);
	}

    /**
     * 
     * @uml.property name="reverseFunction"
     */
    public void setReverseFunction(SDFFunction reverseFunction) {
        this.reverseFunction = reverseFunction;
    }

    /**
     * 
     * @uml.property name="reverseFunction"
     */
    public SDFFunction getReverseFunction() {
        return reverseFunction;
    }

	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		// Achtung Gefahr von Endlosschleifen, wenn hier nicht getURI verwendet
		// wird!
		if (this.getReverseFunction() != null)
			ret.append("Umkehrfunktion "
					+ this.getReverseFunction().getURI(true));
		return ret.toString();
	}
}