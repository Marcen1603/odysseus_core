package de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDataschema;

public class SDFCommonElementsMapping extends SDFOneToOneMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1219936407133441760L;

	protected SDFCommonElementsMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}
}