package de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDataschema;

public class SDFEquivalenceMapping extends SDFOneToOneMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3688858665501908952L;

	protected SDFEquivalenceMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}
}