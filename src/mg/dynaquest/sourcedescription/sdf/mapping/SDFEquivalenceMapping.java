package mg.dynaquest.sourcedescription.sdf.mapping;

import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;

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