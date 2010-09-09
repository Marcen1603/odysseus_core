package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping.SDFSchemaMapping;

public class SDFSchemaMappingList {

    /**
	 * @uml.property  name="mappings"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping.SDFSchemaMapping"
	 */
    ArrayList<SDFSchemaMapping> mappings = new ArrayList<SDFSchemaMapping>();

	public void addSchemaMapping(SDFSchemaMapping schemaMapping) {
		mappings.add(schemaMapping);
	}

	public SDFSchemaMapping getSchemaMapping(int pos) {
		return mappings.get(pos);
	}

	public int getMappingCount() {
		return mappings.size();
	}

}