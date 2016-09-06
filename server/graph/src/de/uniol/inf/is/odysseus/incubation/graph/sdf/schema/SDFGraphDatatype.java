package de.uniol.inf.is.odysseus.incubation.graph.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class SDFGraphDatatype extends SDFDatatype {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -603222024545731230L;

	public SDFGraphDatatype(final String URI) {
		super(URI);
	}
	
	public SDFGraphDatatype (final SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}
	
	public SDFGraphDatatype(final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
		super(datatypeName, type, schema);
	}
	
	public static final SDFDatatype GRAPH = new SDFDatatype("Graph");
	
	public static List<SDFDatatype> getTypes() {
		final List<SDFDatatype> types = new ArrayList<>();
		types.addAll(SDFDatatype.getTypes());
		types.add(SDFGraphDatatype.GRAPH);
		
		return types;
	}
}
