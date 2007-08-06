package mg.dynaquest.sourcedescription.sdf.mapping;

import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;

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