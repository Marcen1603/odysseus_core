package de.uniol.inf.is.odysseus.wrapper.urg.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SDFUrgDatatype extends SDFDatatype {
	/** Auto generated serial UID. */
	private static final long serialVersionUID = 6111056186996148097L;

	/**
     * Constructor.
     * @param uri
     * URI.
     */
    public SDFUrgDatatype(String uri) {
        super(uri, true);
    }
    
    /** Data type: URG Scann. */
    public static final SDFDatatype URG_SCANN = new SDFUrgDatatype(
            "UrgScann");
}
