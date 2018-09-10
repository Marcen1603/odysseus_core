package de.uniol.inf.is.odysseus.wrapper.inertiacube.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SDFInertiaCubeDatatype extends SDFDatatype {
	/** Auto generated serial UID. */
	private static final long serialVersionUID = -4344537831901881539L;

	/**
     * Constructor.
     * @param uri
     * URI.
     */
    public SDFInertiaCubeDatatype(String uri) {
        super(uri, true);
    }
    
    /** Data type: Yaw Pitch Roll. */
    public static final SDFDatatype YAW_PITCH_ROLL = new SDFInertiaCubeDatatype(
            "YawPitchRoll");
}
