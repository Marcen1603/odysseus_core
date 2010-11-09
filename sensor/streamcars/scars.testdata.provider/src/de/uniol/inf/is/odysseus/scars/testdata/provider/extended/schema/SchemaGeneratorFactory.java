package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;

public class SchemaGeneratorFactory {

	public static SchemaGeneratorFactory instance;

	private SchemaGeneratorFactory() {

	}

	public static synchronized SchemaGeneratorFactory getInstance() {
		if (instance == null) {
			instance = new SchemaGeneratorFactory();
		}
		return instance;
	}

	/**
	 * builds a new SchemaGenerator instance according to the given id
	 * 
	 * @param schemaID
	 *            not null.
	 * @return a new SchemaGenerator instance or null iff the schemaID is
	 *         invalid
	 */
	public ISchemaGenerator buildSchemaGenerator(String schemaID) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT)) {
			return new DefaultSchemaGenerator();
		} else {
			return null;
		}
	}

}
