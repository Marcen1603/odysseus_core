package mg.dynaquest.sourcedescription.sdf.mapping;

import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;

public class SDFSupersetMapping extends SDFOneToOneMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = -742131257152932966L;

	protected SDFSupersetMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}
}