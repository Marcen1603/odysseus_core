package de.uniol.inf.is.odysseus.scars.testdata.provider;

import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class Provider implements IProvider {
	
	public Provider() {
		
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
