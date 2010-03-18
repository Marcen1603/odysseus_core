package de.uniol.inf.is.odysseus.broker.movingobjects.generator;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public enum MovingObjectsStreamType {

	VEHICLE("vehicle");

	public final String name;

	private MovingObjectsStreamType(String name) {
		this.name = name;
	}

	static public SDFAttributeList getSchema(MovingObjectsStreamType type) {
		SDFAttributeList schema = new SDFAttributeList();
		switch (type) {
		case VEHICLE:
			SDFAttribute a = new SDFAttribute("timestamp");
			a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
			schema.add(a);
			a = new SDFAttribute("id");
			a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			schema.add(a);
			a = new SDFAttribute("interval");
			a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			schema.add(a);
			a = new SDFAttribute("type");
			a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
			schema.add(a);
			a = new SDFAttribute("position_x");
			a.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			schema.add(a);
			a = new SDFAttribute("position_y");
			a.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			schema.add(a);
			break;
		}
		return schema;
	}

}
