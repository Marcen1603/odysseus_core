package mg.dynaquest.sourcedescription.sdf.mapping;

import mg.dynaquest.sourcedescription.sdf.function.SDFOneToOneFunction;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;

public class SDFOneToOneMapping extends SDFSchemaMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = 614867670818825240L;

	protected SDFOneToOneMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}

	public void setMappingFunction(SDFOneToOneFunction mappingFunction) {
		this.mappingFunction = mappingFunction;
	}

}