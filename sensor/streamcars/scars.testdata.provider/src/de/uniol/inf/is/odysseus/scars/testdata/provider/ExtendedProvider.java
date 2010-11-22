package de.uniol.inf.is.odysseus.scars.testdata.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.CalcModelFactory;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IGenericCalcModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.CarModelFactory;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.entrance.EntranceFactory;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.entrance.IEntrance;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema.ISchemaGenerator;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema.SchemaGeneratorFactory;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.tuple.ITupleGenerator;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.tuple.TupleGeneratorFactory;
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
	private IEntrance entrance;
	private ITupleGenerator tupleGenerator;
	private Map<String, String> options;
	private Map<String, Object> calcModelParams;

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
	public ExtendedProvider(Map<String, String> options, Map<String, Object> calcModelParams) {
		this.options = options;
		this.calcModelParams = calcModelParams;
		String schemaID = options.get(SCHEMA);
		String calcModelID = options.get(CALCMODEL);
		if (schemaID != null && calcModelID != null) {
			this.schemaGenerator = SchemaGeneratorFactory.getInstance()
					.buildSchemaGenerator(schemaID);
			this.entrance = EntranceFactory.getInstance().buildEntrance(schemaID, calcModelID);
			this.tupleGenerator = TupleGeneratorFactory.getInstance().buildTupleGenerator(schemaID);
		}
		
		
		if (this.schemaGenerator == null || this.entrance == null || this.tupleGenerator == null) {
			throw new RuntimeException("unable to initialize test data provider: (" + this.schemaGenerator + ", " + this.entrance + ", " + this.tupleGenerator);
		}
	}

	@Override
	public void init() {
		if (this.numOfCars <= 0) {
			this.numOfCars = 5;
		}
		this.state = new ArrayList<ICarModel>();
		this.idQueue = new LinkedList<Integer>();
		for (int i = 0; i < numOfCars * 2; i++) {
			idQueue.offer(i);
		}
	}

	/**
	 * updates the state and generates a new tuple from it
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
		
		// fill up with new cars
		if (this.state.size() < this.numOfCars) {
			if (this.entrance.freeEntranceSlot(this.state)) {
				String schemaID = this.options.get(SCHEMA);
				String calcModelID = this.options.get(CALCMODEL);
				IGenericCalcModel calcModel =  CalcModelFactory.getInstance().buildCalcModel(schemaID, calcModelID);
				calcModel.init(this.calcModelParams);
				calcModel.setDelay(this.delay);
				Integer id = idQueue.poll();
				ICarModel carModel = CarModelFactory.getInstance().buildCarModel(schemaID, id, calcModel);
				this.state.add(carModel);
			}
		}
		
//		this.printState();
		
		// build tuple from updated state
		return this.tupleGenerator.nextTuple(this.state, this.currentTimeStamp);
	}

	private void printState() {
		for (ICarModel cm : this.state)
			System.out.println(cm);
	}

	@Override
	public SDFAttributeList getSchema(String sourceName) {
		return this.schemaGenerator.getSchema(sourceName);
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getNumOfCars() {
		return numOfCars;
	}

	public void setNumOfCars(int numOfCars) {
		this.numOfCars = numOfCars;
	}
	
	

}
