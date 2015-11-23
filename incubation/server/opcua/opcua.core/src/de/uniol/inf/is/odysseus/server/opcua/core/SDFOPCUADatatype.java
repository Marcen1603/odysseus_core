package de.uniol.inf.is.odysseus.server.opcua.core;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SDFOPCUADatatype extends SDFDatatype {

	private static final long serialVersionUID = 2619246735313817401L;

	public static final SDFDatatype OPCVALUE = new SDFOPCUADatatype("OPCValue", SDFDatatype.KindOfDatatype.GENERIC,
			SDFDatatype.DOUBLE);

	public static final SDFDatatype[] types = new SDFDatatype[] { OPCVALUE };

	public SDFOPCUADatatype(final String datatypeName, final KindOfDatatype type, final SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}
}