package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

@SuppressWarnings("serial")
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