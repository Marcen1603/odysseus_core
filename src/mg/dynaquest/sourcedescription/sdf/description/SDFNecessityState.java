package mg.dynaquest.sourcedescription.sdf.description;

import mg.dynaquest.sourcedescription.sdf.SDFElement;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

public class SDFNecessityState extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -206499892417834148L;
	/**
	 * @uml.property  name="optional"
	 */
    boolean optional = false;


	public SDFNecessityState(String URI) {
		super(URI);
		if (URI
				.equals(SDFIntensionalDescriptions.Optional)) {
			optional = true;
		}
	}

    /**
     * 
     * @uml.property name="optional"
     */
    public boolean isOptional() {
        return this.optional;
    }

}