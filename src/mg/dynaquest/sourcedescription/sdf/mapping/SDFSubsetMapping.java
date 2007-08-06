package mg.dynaquest.sourcedescription.sdf.mapping;

import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;

public class SDFSubsetMapping extends SDFOneToOneMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7543218963910219371L;

	protected SDFSubsetMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}
}