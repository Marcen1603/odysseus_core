package mg.dynaquest.sourcedescription.sdf.description;

import mg.dynaquest.sourcedescription.sdf.SDFElement;

public class SDFAccessPattern extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3298970851864201598L;

	/**
	 * @uml.property  name="inputPatt"
	 * @uml.associationEnd  
	 */
    private SDFInputPattern inputPatt = null;

    /**
	 * @uml.property  name="outputPatt"
	 * @uml.associationEnd  
	 */
    private SDFOutputPattern outputPatt = null;


	public SDFAccessPattern(String URI) {
		super(URI);
	}

    /**
     * 
     * @uml.property name="inputPatt"
     */
    public void setInputPatt(SDFInputPattern inputPatt) {
        this.inputPatt = inputPatt;
    }

    /**
     * 
     * @uml.property name="inputPatt"
     */
    public SDFInputPattern getInputPatt() {
        return inputPatt;
    }

    /**
     * 
     * @uml.property name="outputPatt"
     */
    public void setOutputPatt(SDFOutputPattern outputPatt) {
        this.outputPatt = outputPatt;
    }

    /**
     * 
     * @uml.property name="outputPatt"
     */
    public SDFOutputPattern getOutputPatt() {
        return outputPatt;
    }

	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + ": {");
		if (this.inputPatt != null)
			ret.append(this.inputPatt.toString());
		ret.append(" --> ");
		if (this.outputPatt != null)
			ret.append(this.outputPatt.toString());
		return ret.toString() + "}";
	}

}