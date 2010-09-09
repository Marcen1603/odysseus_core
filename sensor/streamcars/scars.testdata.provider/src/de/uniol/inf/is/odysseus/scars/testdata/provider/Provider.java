package de.uniol.inf.is.odysseus.scars.testdata.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.testdata.provider.model.CalculationModelFactory;
import de.uniol.inf.is.odysseus.scars.testdata.provider.model.CarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.model.ICalculationModel;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class Provider implements IProvider {

	/**
	 * anzahl der autos in einem scan
	 */
	private int numOfCars;
	/**
	 * zeitlicher abstand zwischen den einzelnen scans in ms
	 */
	private int delay;
	private long currentTimeStamp;

	/**
	 * Liefert eine eindeutige id für das auto und stellt gleichzeitig sicher,
	 * dass ein Auto mit der id x nicht sofort in den Sichtbereich zurückkehrt,
	 * wenn es ihn gerade erst an anderer Stelle verlassen hat
	 */
	private Queue<Integer> idQueue;

	private ArrayList<CarModel> state;

	public Provider() {
	}

	public int getNumOfCars() {
		return numOfCars;
	}

	public void setNumOfCars(int numOfCars) {
		this.numOfCars = numOfCars;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public void init() {
		if (this.numOfCars <= 0) {
			this.numOfCars = 5;
		}
		this.state = new ArrayList<CarModel>();
		this.idQueue = new LinkedList<Integer>();
		for (int i = 0; i < numOfCars * 2; i++) {
			idQueue.offer(i);
		}
	}

	@Override
	public MVRelationalTuple<?> nextTuple() {
		/**
		 * TODO
		 * Kollisionserkennung?
		 */
		this.currentTimeStamp += this.delay;

		// neuen zustand berechnen
		for (CarModel cm : this.state) {
			cm.getCalcModel().calculateAll();
		}

		//Prüfen ob Auto im Sichtbereich
		Iterator<CarModel> iterator = this.state.iterator();
		while (iterator.hasNext()) {
			CarModel cm = iterator.next();
			if (!this.isVisible(cm)) {
				idQueue.offer(cm.getId());
				iterator.remove();
			}
		}

		// ggf. mit neuen Autos auffüllen
		if (this.state.size() < this.numOfCars) {
			if (this.freeEntranceSlot()) {
				ICalculationModel calcModel = CalculationModelFactory.getInstance().getCalculationModel(
						CalculationModelFactory.OVERTAKE_CALCULATION_MODEL);
				calcModel.init(new Float(3));
				calcModel.setDelay(this.delay);
				Integer id = idQueue.poll();
				CarModel cm = new CarModel(id, calcModel);
				this.state.add(cm);
			}
		}

		MVRelationalTuple<?> root = createTuple(1);

		MVRelationalTuple<?> scan = createTuple(2);
		root.setAttribute(0, scan);

//		MVRelationalTuple<?> timestamp = createTuple(1);
//		timestamp.setAttribute(0, this.currentTimeStamp);

		MVRelationalTuple<?> cars = createTuple(this.state.size());
		for (int i = 0; i < this.state.size(); i++) {
			CarModel cm = this.state.get(i);

//			MVRelationalTuple<?> type = createTuple(1);
//			type.setAttribute(0, cm.getType());
//
//			MVRelationalTuple<?> id = createTuple(1);
//			id.setAttribute(0, cm.getId());
//
//			MVRelationalTuple<?> laneid = createTuple(1);
//			laneid.setAttribute(0, cm.getLaneid());
//
//			MVRelationalTuple<?> posx = createTuple(1);
//			posx.setAttribute(0, cm.getPosx());
//
//			MVRelationalTuple<?> posy = createTuple(1);
//			posy.setAttribute(0, cm.getPosy());
//
//			MVRelationalTuple<?> posz = createTuple(1);
//			posz.setAttribute(0, cm.getPosz());
//
//			MVRelationalTuple<?> roll = createTuple(1);
//			roll.setAttribute(0, cm.getRoll());
//
//			MVRelationalTuple<?> pitch = createTuple(1);
//			pitch.setAttribute(0, cm.getPitch());
//
//			MVRelationalTuple<?> heading = createTuple(1);
//			heading.setAttribute(0, cm.getHeading());
//
//			MVRelationalTuple<?> velocity = createTuple(1);
//			velocity.setAttribute(0, cm.getVelocity());
//
//			MVRelationalTuple<?> length = createTuple(1);
//			length.setAttribute(0, cm.getLength());
//
//			MVRelationalTuple<?> width = createTuple(1);
//			width.setAttribute(0, cm.getWidth());

			MVRelationalTuple<?> car = createTuple(12);
			car.setAttribute(0, cm.getType());
			car.setAttribute(1, cm.getId());
			car.setAttribute(2, cm.getLaneid());
			car.setAttribute(3, cm.getPosx());
			car.setAttribute(4, cm.getPosy());
			car.setAttribute(5, cm.getPosz());
			car.setAttribute(6, cm.getRoll());
			car.setAttribute(7, cm.getPitch());
			car.setAttribute(8, cm.getHeading());
			car.setAttribute(9, cm.getVelocity());
			car.setAttribute(10, cm.getLength());
			car.setAttribute(11, cm.getWidth());

			cars.setAttribute(i, car);
		}
		
		scan.setAttribute(0,  this.currentTimeStamp);
		scan.setAttribute(1, cars);
		
		//this.printCarModels();

		return root;
	}

	private void printCarModels() {
		if (this.currentTimeStamp % 1000 == 0) {
			System.out.println();
			for (int i = 0; i < 80; i++)
				System.out.print("*");
			System.out.println();
			System.out.println("Timestamp: " + this.currentTimeStamp);
			for (CarModel cm : this.state) {
				System.out.println(cm);
			}
		}
	}

	/**
	 * Prüft, ob genug Platz für ein weiteres Auto ist
	 * @return
	 */
	private boolean freeEntranceSlot() {
		for (CarModel cm : this.state) {
			if (cm.getPosx() < 20) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Diese Methode überprüft, ob ein Objekt noch innerhalb des sichtbaren
	 * Bereiches ist
	 * 
	 * Momentan wird nur die Entfernung zum eigenen Auto überprüft. Verdeckungen
	 * durch andere Autos werden nicht berücksichtigt
	 * 
	 * @param cm
	 *            das zu überprüfende CarModel
	 * @return true wenn das auto sichtbar ist, ansonsten false
	 */
	private boolean isVisible(CarModel cm) {
		if (cm.getPosx() > 150 || cm.getPosx() < 0 || cm.getPosy() < -100
				|| cm.getPosy() > 100) {
			return false;
		}
		return true;
	}

	private MVRelationalTuple<? extends IProbability> createTuple(
			int attributeCount) {
		MVRelationalTuple<StreamCarsMetaData<Object>> tuple = new MVRelationalTuple<StreamCarsMetaData<Object>>(
				attributeCount);
		return tuple;
	}

	@Override
	public SDFAttributeList getSchema(String sourceName) {
		SDFAttribute type = new SDFAttribute(sourceName, "type");
		type.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));

		SDFAttribute id = new SDFAttribute(sourceName, "id");
		id.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));

		SDFAttribute laneid = new SDFAttribute(sourceName, "laneid");
		laneid.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));

		SDFAttribute posx = new SDFAttribute(sourceName, "posx");
		posx.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posx.setCovariance(Arrays.asList(new Double[] {0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}));

		SDFAttribute posy = new SDFAttribute(sourceName, "posy");
		posy.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posy.setCovariance(Arrays.asList(new Double[] {0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0}));

		SDFAttribute posz = new SDFAttribute(sourceName, "posz");
		posz.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posz.setCovariance(Arrays.asList(new Double[] {0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0}));

		SDFAttribute roll = new SDFAttribute(sourceName, "roll");
		roll.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		roll.setCovariance(Arrays.asList(new Double[] {0.0, 0.0, 0.0, 0.1, 0.0, 0.0, 0.0}));

		SDFAttribute pitch = new SDFAttribute(sourceName, "pitch");
		pitch.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		pitch.setCovariance(Arrays.asList(new Double[] {0.0, 0.0, 0.0, 0.0, 0.1, 0.0, 0.0}));

		SDFAttribute heading = new SDFAttribute(sourceName, "heading");
		heading.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		heading.setCovariance(Arrays.asList(new Double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0}));

		SDFAttribute velocity = new SDFAttribute(sourceName, "velocity");
		velocity.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		velocity.setCovariance(Arrays.asList(new Double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1}));

		SDFAttribute length = new SDFAttribute(sourceName, "length");
		length.setDatatype(SDFDatatypeFactory.getDatatype("Float"));

		SDFAttribute width = new SDFAttribute(sourceName, "width");
		width.setDatatype(SDFDatatypeFactory.getDatatype("Float"));

		SDFAttribute car = new SDFAttribute(sourceName, "car");
		car.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		car.addSubattribute(type);
		car.addSubattribute(id);
		car.addSubattribute(laneid);
		car.addSubattribute(posx);
		car.addSubattribute(posy);
		car.addSubattribute(posz);
		car.addSubattribute(roll);
		car.addSubattribute(pitch);
		car.addSubattribute(heading);
		car.addSubattribute(velocity);
		car.addSubattribute(length);
		car.addSubattribute(width);

		SDFAttribute cars = new SDFAttribute(sourceName, "cars");
		cars.setDatatype(SDFDatatypeFactory.getDatatype("List"));
		cars.addSubattribute(car);
		
		SDFAttribute timestamp = new SDFAttribute(sourceName, "timestamp");
		timestamp.setDatatype(SDFDatatypeFactory.getDatatype("StartTimestamp"));

		SDFAttribute scan = new SDFAttribute(sourceName, "scan");
		scan.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		scan.addSubattribute(timestamp);
		scan.addSubattribute(cars);

		SDFAttributeListExtended schema = new SDFAttributeListExtended();
		schema.add(scan);

		
		return schema;
	}

	public Long getLastTimestamp() {
		return this.currentTimeStamp;
	}

}
