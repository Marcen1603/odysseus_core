package de.uniol.inf.is.odysseus.scars.testdata.provider;

import java.util.Map;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema.ISchemaGenerator;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema.SchemaGeneratorFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ExtendedProvider implements IProvider {

	// options for extended provider:
	public static final String SCHEMA = "schema";
	public static final String CALCMODEL = "calcmodel";

	// option values: schema
	public static final String SCHEMA_SCARS_DEFAULT = "schema.scars.default";
	public static final String SCHEMA_SCARS_ALTERNATIVE = "schema.scars.alternative";

	// option values calculation model
	public static final String CALCMODEL_SCARS_OVERTAKE = "calcmodel.scars.overtake";

	// local fields:
	private ISchemaGenerator schemaGenerator;

	/**
	 * creates a new object instance according to the given options
	 * 
	 * @param options
	 *            map defining the parameters (schema, calculation model). not
	 *            null. use empty map for default.
	 */
	public ExtendedProvider(Map<String, String> options) {
		String val = options.get(SCHEMA);
		if (val != null) {
			this.schemaGenerator = SchemaGeneratorFactory.getInstance()
					.buildSchemaGenerator(val);
		}
		if (this.schemaGenerator == null) {
			this.schemaGenerator = SchemaGeneratorFactory.getInstance()
					.buildSchemaGenerator(SCHEMA_SCARS_DEFAULT);
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object nextTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDFAttributeList getSchema(String sourceName) {
		return this.schemaGenerator.getSchema(sourceName);
	}

}
