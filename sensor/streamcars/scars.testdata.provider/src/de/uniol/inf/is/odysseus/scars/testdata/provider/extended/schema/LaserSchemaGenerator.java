/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * this implementation of the {@link ISchemaGenerator} interface provides the
 * schema of the laserscanner used by the project group StreamCars.
 * 
 * @author tommy
 * 
 */
public class LaserSchemaGenerator implements ISchemaGenerator {

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
				mvCorrelationValue}));

		SDFAttribute posy = new SDFAttribute(sourceName, "posy");
		posy.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posy.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvValue, mvCorrelationValue, mvCorrelationValue,
				mvCorrelationValue }));

		SDFAttribute posz = new SDFAttribute(sourceName, "posz");
		posz.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		posz.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvValue, mvCorrelationValue,
				mvCorrelationValue }));

		SDFAttribute heading = new SDFAttribute(sourceName, "heading");
		heading.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		heading.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvValue, mvCorrelationValue }));

		SDFAttribute velocity = new SDFAttribute(sourceName, "velocity");
		velocity.setDatatype(SDFDatatypeFactory.getDatatype("MV Float"));
		velocity.setCovariance(Arrays.asList(new Double[] { mvCorrelationValue,
				mvCorrelationValue, mvCorrelationValue, mvCorrelationValue,
				mvValue }));

		SDFAttribute posx_np = new SDFAttribute(sourceName, "posx_np");
		posx_np.setDatatype(SDFDatatypeFactory.getDatatype("Float"));

		SDFAttribute posy_np = new SDFAttribute(sourceName, "posy_np");
		posy_np.setDatatype(SDFDatatypeFactory.getDatatype("Float"));
		
		SDFAttribute points_posx = new SDFAttribute(sourceName, "posx");
		points_posx.setDatatype(SDFDatatypeFactory.getDatatype("Float"));
		
		SDFAttribute points_posy = new SDFAttribute(sourceName, "posy");
		points_posy.setDatatype(SDFDatatypeFactory.getDatatype("Float"));
		
		SDFAttribute points = new SDFAttribute(sourceName, "points");
		points.setDatatype(SDFDatatypeFactory.getDatatype("List"));
		points.addSubattribute(points_posx);
		points.addSubattribute(points_posy);

		SDFAttribute car = new SDFAttribute(sourceName, "car");
		car.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		car.addSubattribute(type);
		car.addSubattribute(id);
		car.addSubattribute(laneid);
		car.addSubattribute(posx);
		car.addSubattribute(posy);
		car.addSubattribute(posz);
		car.addSubattribute(heading);
		car.addSubattribute(velocity);
		car.addSubattribute(posx_np);
		car.addSubattribute(posy_np);
		car.addSubattribute(points);

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
