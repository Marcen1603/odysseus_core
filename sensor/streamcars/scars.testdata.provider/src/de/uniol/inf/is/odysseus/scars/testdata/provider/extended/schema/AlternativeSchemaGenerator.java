package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class AlternativeSchemaGenerator implements ISchemaGenerator {

	@Override
	public SDFAttributeList getSchema(String sourceName) {
		double mvValue = 1.0;
		double mvCorrelationValue = 0.0;
		SDFAttribute type = new SDFAttribute(sourceName, "type");
		type.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));

		SDFAttribute id = new SDFAttribute(sourceName, "id");
		id.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));

		SDFAttribute laneid = new SDFAttribute(sourceName, "laneid");
		laneid.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));

		SDFAttribute posx = new SDFAttribute(sourceName, "posx");
		posx.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posx.setCovariance(Arrays.asList(new Double[] { mvValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue, mvCorrelationValue }));

		SDFAttribute posy = new SDFAttribute(sourceName, "posy");
		posy.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posy.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvValue, mvCorrelationValue, mvCorrelationValue, mvCorrelationValue}));
		
		SDFAttribute heading = new SDFAttribute(sourceName, "heading");
		heading.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		heading.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvValue, mvCorrelationValue, mvCorrelationValue }));

		SDFAttribute velocity = new SDFAttribute(sourceName, "velocity");
		velocity.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		velocity.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvValue, mvCorrelationValue}));

		SDFAttribute acceleration = new SDFAttribute(sourceName, "acceleration");
		acceleration.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		acceleration.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue, mvValue }));

		SDFAttribute car = new SDFAttribute(sourceName, "car");
		car.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		car.addSubattribute(type);
		car.addSubattribute(id);
		car.addSubattribute(laneid);
		car.addSubattribute(posx);
		car.addSubattribute(posy);
		car.addSubattribute(heading);
		car.addSubattribute(velocity);
		car.addSubattribute(acceleration);

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

}
