package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * this implementation of the {@link ISchemaGenerator} interface provides the
 * default schema used by the project group StreamCars. It matches the schema of
 * the jdve radar sensor
 * 
 * @author tommy
 * 
 */
public class DefaultSchemaGenerator implements ISchemaGenerator {

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
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue }));

		SDFAttribute posy = new SDFAttribute(sourceName, "posy");
		posy.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posy.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvValue, mvCorrelationValue, mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue }));

		SDFAttribute posz = new SDFAttribute(sourceName, "posz");
		posz.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posz.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvValue, mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue }));

		SDFAttribute roll = new SDFAttribute(sourceName, "roll");
		roll.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		roll.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue }));

		SDFAttribute pitch = new SDFAttribute(sourceName, "pitch");
		pitch.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		pitch.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue,
				mvValue, mvCorrelationValue, mvCorrelationValue }));

		SDFAttribute heading = new SDFAttribute(sourceName, "heading");
		heading.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		heading.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue,
				mvCorrelationValue, mvValue, mvCorrelationValue }));

		SDFAttribute velocity = new SDFAttribute(sourceName, "velocity");
		velocity.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		velocity.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvValue }));

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

}
