/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.service.sensor;

import de.uniol.inf.is.odysseus.service.sensor.data.Schema;
import de.uniol.inf.is.odysseus.service.sensor.webservice.SensorAttribute;
import de.uniol.inf.is.odysseus.service.sensor.webservice.SensorRegistryService;
import de.uniol.inf.is.odysseus.service.sensor.webservice.SensorRegistryServiceService;
import de.uniol.inf.is.odysseus.service.sensor.webservice.SensorSchema;
import de.uniol.inf.is.odysseus.service.sensor.webservice.StringResponse;

public class SensorService implements ISensorService {

	private String username;
	private String password;

	@Override
    public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
    public ISensor createSensor(String name, Schema schema, boolean register) {
		ISensor sensor = null;
		if (register) {			
			SensorRegistryServiceService srss = new SensorRegistryServiceService();
			SensorRegistryService srs = srss.getSensorRegistryServicePort();
			StringResponse sr = srs.login(username, password);
			if (sr.isSuccessful()) {
				sensor = SensorDictionary.getInstance().createAndAddSensor(name, schema);
				String securityToken = sr.getResponseValue();
				SensorSchema sensorSchema = new SensorSchema();
				for(String attributeName :schema.getNameOrder()){
					SensorAttribute sa = new SensorAttribute();
					sa.setName(attributeName);
					sa.setType(schema.getDatatype(attributeName).toString());					
					sensorSchema.getAttributes().add(sa);
				}
						
				boolean result = srs.registerSensor(securityToken, name, sensor.getOwnHost(), sensor.getPort(), sensorSchema);
				if (!result) {
					SensorDictionary.getInstance().stopAndRemoveSensor(name);
					return null;
				}
			}
		} else {
			sensor = SensorDictionary.getInstance().createAndAddSensor(name, schema);
		}
		return sensor;
	}

	@Override
    public ISensor getSensor(String name) {
		return SensorDictionary.getInstance().getSensor(name);
	}

	@Override
    public boolean isSensorExistent(String name) {
		if (SensorDictionary.getInstance().getSensor(name) == null) {
			return false;
		}
        return true;
	}

	@Override
    public boolean removeSensor(String name) {
		if (isSensorExistent(name)) {
			SensorRegistryServiceService srss = new SensorRegistryServiceService();
			SensorRegistryService srs = srss.getSensorRegistryServicePort();
			StringResponse sr = srs.login(username, password);
			if (sr.isSuccessful()) {
				SensorDictionary.getInstance().stopAndRemoveSensor(name);
				String securityToken = sr.getResponseValue();
				boolean result = srs.unregisterSensor(securityToken, name);
				return result;
			}
		}
		return false;
	}

}
