package de.uniol.inf.is.odysseus.temporaltypes.types;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This class lists all available temporal datatypes.
 * 
 * @author Tobias Brandt
 *
 */
public class TemporalDatatype extends SDFDatatype {

	private static final long serialVersionUID = 4911387440959109155L;

	public TemporalDatatype(String URI) {
		super(URI);
	}

	public TemporalDatatype(String datatypeName, KindOfDatatype type, SDFSchema schema) {
		super(datatypeName, type, schema, true);
	}

	public TemporalDatatype(String datatypeName, KindOfDatatype type, SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}

	public static final SDFDatatype TEMPORAL_INTEGER = new TemporalDatatype("TemporalInteger", KindOfDatatype.BASE,
			SDFDatatype.INTEGER);

}
