package de.uniol.inf.is.odysseus.s100.common.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class SDFS100DataType extends SDFDatatype {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5267386900216831975L;

	public SDFS100DataType (final String URI) {
		super(URI);
	}
	
	public SDFS100DataType (final SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}
	
	public SDFS100DataType (final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
		super(datatypeName, type, schema);
	}
	
	public static final SDFDatatype GM_POINT = new SDFDatatype("GM_Point");
	
	
	/**
	 * {@inheritDoc}
	 */
	
	public static List<SDFDatatype> getTypes() {
		final List<SDFDatatype> types = new ArrayList<>();
		types.addAll(SDFDatatype.getTypes());
		types.add(SDFS100DataType.GM_POINT);
		
		return types;
	}
}
