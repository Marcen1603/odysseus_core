package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * interface for encapsulating the static creation of test schemata.
 * @author tommy
 *
 */
public interface ISchemaGenerator {

	/**
	 * builds the static schema
	 * @return a newly allocated schema
	 */
	public SDFAttributeList getSchema(String sourceName);
	
}
