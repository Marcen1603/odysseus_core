package mg.dynaquest.sourcedescription.sdf.description;

import mg.dynaquest.sourcedescription.sdf.SDFElement;

public abstract class SDFAttributeBindung extends SDFElement {

	public SDFAttributeBindung(String URI) {
		super(URI);
	}

    /**
     * @param leftAap
     * @param rightAap
     * @return
     */
    public abstract SDFAttributeBindung createMerge(SDFAttributeBindung leftAttributeBinding, SDFAttributeBindung rightAttributeBinding)
    throws SDFAttributeBindingNotMergeableException;
}