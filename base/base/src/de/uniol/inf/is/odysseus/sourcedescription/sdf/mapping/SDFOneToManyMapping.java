package de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.SDFOneToManyFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDataschema;

public class SDFOneToManyMapping extends SDFSchemaMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1542489822607979543L;

	protected SDFOneToManyMapping(String URI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		super(URI, localSchema, globalSchema);
	}

	public void setMappingFunction(SDFOneToManyFunction mappingFunction) {
		this.mappingFunction = mappingFunction;
	}

}