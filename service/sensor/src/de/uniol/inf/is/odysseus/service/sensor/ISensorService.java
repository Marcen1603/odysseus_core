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

/**
 * The Interface ISensorService represents the service for creating new sensors
 */
public interface ISensorService {
	
	/**
	 * Sets the username and password that are used for the login into Odysseus
	 * These credentials have to be set first!
	 * 
	 * @param username
	 * @param password
	 */
	public void setCredentials(String username, String password);
	/**
	 * Creates a new sensor for the given name an datatypes.
	 * datatypes is a map where the String (key) is the attribute name and the DataType (value) specifies the according data type.
	 *
	 * @param name the name for the sensor
	 * @param datatypes the attributes and data types that the sensor provides
	 * @param register tries to register the sensor via the webservice in odysseus
	 * @return the created sensor
	 */
	public ISensor createSensor(String name, Schema schema, boolean register);
	
	/**
	 * Gets a sensor by its name.
	 *
	 * @param name the name of the sensor
	 * @return the sensor or null if it does not exists
	 */
	public ISensor getSensor(String name);
	
	/**
	 * Checks if sensor is existent.
	 *
	 * @param name the name to check
	 * @return true, if sensor is existent
	 */
	public boolean isSensorExistent(String name);
	
	/**
	 * Removes the sensor permanently
	 * 
	 * @param name
	 * @return
	 */
	public boolean removeSensor(String name);
}
