package de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.SDFOneToOneFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDataschema;

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