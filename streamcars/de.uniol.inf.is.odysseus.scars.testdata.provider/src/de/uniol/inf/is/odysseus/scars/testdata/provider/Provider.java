package de.uniol.inf.is.odysseus.scars.testdata.provider;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.testdata.provider.model.CarModel;
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
	}

	@Override
	public MVRelationalTuple<?> nextTuple() {
		/**
		 * TODO
		 * Berechnung des neuen zustands
		 * Prüfen ob auto in sichtbereich
		 *   - wenn nicht entfernen
		 *   - neues auto mit neuer id hinzufügen
		 */
		Long tsValue = new Long(0);//TODO set correct value
		
		MVRelationalTuple<?> root = createTuple(1);
		
		MVRelationalTuple<?> scan = createTuple(2);
		root.setAttribute(0, scan);
		
		MVRelationalTuple<?> timestamp = createTuple(1);
		timestamp.setAttribute(0, tsValue);
		
		MVRelationalTuple<?> cars = createTuple(this.numOfCars);
		for (int i = 0; i < this.state.size(); i++) {
			CarModel cm = this.state.get(i);
			
			MVRelationalTuple<?> type = createTuple(1);
			type.setAttribute(0, cm.getType());
			
			MVRelationalTuple<?> id = createTuple(1);
			id.setAttribute(0, cm.getId());
			
			MVRelationalTuple<?> laneid = createTuple(1);
			laneid.setAttribute(0, cm.getLaneid());
			
			MVRelationalTuple<?> posx = createTuple(1);
			posx.setAttribute(0, cm.getPosx());
			
			MVRelationalTuple<?> posy = createTuple(1);
			posy.setAttribute(0, cm.getPosy());
			
			MVRelationalTuple<?> posz = createTuple(1);
			posz.setAttribute(0, cm.getPosz());
			
			MVRelationalTuple<?> roll = createTuple(1);
			roll.setAttribute(0, cm.getRoll());
			
			MVRelationalTuple<?> pitch = createTuple(1);
			pitch.setAttribute(0, cm.getPitch());
			
			MVRelationalTuple<?> heading = createTuple(1);
			heading.setAttribute(0, cm.getHeading());
			
			MVRelationalTuple<?> velocity = createTuple(1);
			velocity.setAttribute(0, cm.getVelocity());
			
			MVRelationalTuple<?> length = createTuple(1);
			length.setAttribute(0, cm.getLength());
			
			MVRelationalTuple<?> width = createTuple(1);
			width.setAttribute(0, cm.getWidth());
			
			MVRelationalTuple<?> car = createTuple(12);
			car.setAttribute(0, type);
			car.setAttribute(0, id);
			car.setAttribute(0, laneid);
			car.setAttribute(0, posx);
			car.setAttribute(0, posy);
			car.setAttribute(0, posz);
			car.setAttribute(0, roll);
			car.setAttribute(0, pitch);
			car.setAttribute(0, heading);
			car.setAttribute(0, velocity);
			car.setAttribute(0, length);
			car.setAttribute(0, width);
			
			cars.setAttribute(i, car);
		}
		
		return root;
	}
	
	private MVRelationalTuple<? extends IProbability> createTuple(int attributeCount) {
		MVRelationalTuple<StreamCarsMetaData<Object>> tuple = new MVRelationalTuple<StreamCarsMetaData<Object>>(attributeCount);
		return tuple;
	}

	@Override
	public SDFAttributeList getSchema(String sourceName) {
		SDFAttribute type = new SDFAttribute(sourceName, "type");
		type.setDatatype(SDFDatatypeFactory.getDatatype("INTEGER"));

		SDFAttribute id = new SDFAttribute(sourceName, "id");
		id.setDatatype(SDFDatatypeFactory.getDatatype("INTEGER"));

		SDFAttribute laneid = new SDFAttribute(sourceName, "laneid");
		laneid.setDatatype(SDFDatatypeFactory.getDatatype("INTEGER"));

		SDFAttribute posx = new SDFAttribute(sourceName, "posx");
		posx.setDatatype(SDFDatatypeFactory.getDatatype("MV FLOAT"));

		SDFAttribute posy = new SDFAttribute(sourceName, "posy");
		posy.setDatatype(SDFDatatypeFactory.getDatatype("MV FLOAT"));

		SDFAttribute posz = new SDFAttribute(sourceName, "posz");
		posz.setDatatype(SDFDatatypeFactory.getDatatype("MV FLOAT"));

		SDFAttribute roll = new SDFAttribute(sourceName, "roll");
		roll.setDatatype(SDFDatatypeFactory.getDatatype("MV FLOAT"));

		SDFAttribute pitch = new SDFAttribute(sourceName, "pitch");
		pitch.setDatatype(SDFDatatypeFactory.getDatatype("MV FLOAT"));

		SDFAttribute heading = new SDFAttribute(sourceName, "heading");
		heading.setDatatype(SDFDatatypeFactory.getDatatype("MV FLOAT"));

		SDFAttribute velocity = new SDFAttribute(sourceName, "velocity");
		velocity.setDatatype(SDFDatatypeFactory.getDatatype("MV FLOAT"));

		SDFAttribute length = new SDFAttribute(sourceName, "length");
		length.setDatatype(SDFDatatypeFactory.getDatatype("FLOAT"));

		SDFAttribute width = new SDFAttribute(sourceName, "width");
		width.setDatatype(SDFDatatypeFactory.getDatatype("FLOAT"));

		SDFAttribute car = new SDFAttribute(sourceName, "car");
		car.setDatatype(SDFDatatypeFactory.getDatatype("RECORD"));
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
		cars.setDatatype(SDFDatatypeFactory.getDatatype("LIST"));

		SDFAttribute timestamp = new SDFAttribute(sourceName, "timestamp");
		timestamp.setDatatype(SDFDatatypeFactory.getDatatype("STARTTIMESTAMP"));

		SDFAttribute scan = new SDFAttribute(sourceName, "scan");
		scan.setDatatype(SDFDatatypeFactory.getDatatype("RECORD"));
		scan.addSubattribute(timestamp);
		scan.addSubattribute(cars);

		SDFAttributeListExtended schema = new SDFAttributeListExtended();
		schema.add(scan);

		return schema;
	}

}
