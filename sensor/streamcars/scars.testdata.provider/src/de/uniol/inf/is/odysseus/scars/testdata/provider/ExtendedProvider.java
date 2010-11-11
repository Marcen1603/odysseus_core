package de.uniol.inf.is.odysseus.scars.testdata.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;
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
	 * max number of cars in a scan
	 */
	private int numOfCars;
	/**
	 * delay between initialization of two cars (in ms)
	 */
	private int delay;
	private long currentTimeStamp;

	/**
	 * returns a unique id for a car and makes sure, that a car with a certain
	 * id doesn't return to the visual range directly after leaving it
	 */
	private Queue<Integer> idQueue;

	private ArrayList<ICarModel> state;

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

	/**
	 * TODO not implemented yet
	 */
	@Override
	public Object nextTuple() {
		// calculate new state
		for (ICarModel model : this.state) {
			model.getCalcModel().calculateAll();
		}
		
		// check if cars are in visual range
		Iterator<ICarModel> iterator = this.state.iterator();
		while (iterator.hasNext()) {
			ICarModel carModel = iterator.next();
			if (!carModel.isVisible()) {
				this.idQueue.offer(carModel.getId());
				iterator.remove();
			}
		}
		
		// ggf. mit neuen Autos auffüllen
		// tupel aus carModel bauen

		return null;
	}

	@Override
	public SDFAttributeList getSchema(String sourceName) {
		return this.schemaGenerator.getSchema(sourceName);
	}

}
