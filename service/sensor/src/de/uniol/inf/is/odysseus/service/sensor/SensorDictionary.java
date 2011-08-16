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

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.service.sensor.connection.SensorStreamServer;
import de.uniol.inf.is.odysseus.service.sensor.data.DataType;

/**
 * The Dictionary for all sensors.
 */
public class SensorDictionary {

	/** The current instance for singleton. */
	private static SensorDictionary instance;

	/** All current registered sensors. */
	private Map<String, ISensor> sensors = new HashMap<String, ISensor>();

	/** A counter that provides the next port for a new sensor. */
	private int startPort = 55500;

	/**
	 * Instantiates a new sensor dictionary.
	 */
	private SensorDictionary() {
		// intentionally left blank
	}

	/**
	 * Gets the single instance of SensorDictionary.
	 *
	 * @return single instance of SensorDictionary
	 */
	public static synchronized SensorDictionary getInstance() {
		if (instance == null) {
			instance = new SensorDictionary();
		}
		return instance;
	}

	/**
	 * Adds a sensor to the dictionary.
	 *
	 * @param name the name of the sensor
	 * @param sensor the sensor to add
	 */
	public void addSensor(String name, ISensor sensor) {
		this.sensors.put(name, sensor);
	}

	/**
	 * Gets a sensor from the dictionary.
	 *
	 * @param name the name of the sensor
	 * @return the sensor or null if not exists
	 */
	public ISensor getSensor(String name) {
		return this.sensors.get(name);
	}

	/**
	 * Creates and adds a new sensor.
	 *
	 * @param name the name of the sensor
	 * @param attributes the attributes for this sensor
	 * @return the sensor 
	 */
	public ISensor createAndAddSensor(String name,Map<String, DataType> attributes) {
		try {
			SensorStreamServer sss = new SensorStreamServer(startPort, attributes);
			sss.start();
			addSensor(name, sss);
			startPort++;			
			return sss;
		} catch (Exception e) {
			System.err.println("Server could not be created...");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Stops all threads and remove the sensor from client-host
	 * 
	 * @param name
	 */
	public void stopAndRemoveSensor(String name){
		ISensor sensor = this.sensors.get(name);
		if(sensor instanceof SensorStreamServer){
			SensorStreamServer sss = (SensorStreamServer) sensor;
			sss.interrupt();
		}		
		removeSensor(name);
	}
	
	public void removeSensor(String name){
		this.sensors.remove(name);
	}

}
