package de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.SDFManyToOneFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDataschema;

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