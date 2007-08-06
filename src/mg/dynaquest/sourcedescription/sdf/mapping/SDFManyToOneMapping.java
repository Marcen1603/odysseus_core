package mg.dynaquest.sourcedescription.sdf.mapping;

import mg.dynaquest.sourcedescription.sdf.function.SDFManyToOneFunction;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;

public class SDFManyToOneMapping extends SDFSchemaMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3805698255007592987L;

	protected SDFManyToOneMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}

	public void setMappingFunction(SDFManyToOneFunction mappingFunction) {
		this.mappingFunction = mappingFunction;
	}

}