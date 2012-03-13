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
package de.uniol.inf.is.odysseus.service.test;

import java.util.Random;

import de.uniol.inf.is.odysseus.service.sensor.ISensorService;
import de.uniol.inf.is.odysseus.service.sensor.data.DataTuple;
import de.uniol.inf.is.odysseus.service.sensor.data.DataType;
import de.uniol.inf.is.odysseus.service.sensor.data.Schema;

/**
 * The Class SimpleSensor.
 */
public class SimpleSensor extends Thread{	
	
	/** The sensor service that is bound by the service. */
	private ISensorService sensorService;

	/** The current x_value. */
	private double x_value = 0.0;
	
	/** The current y_value. */
	private double y_value = 0.0;
	
	/** The counter for a number. */
	private int number = 0;
	
	/** The Constant SENSOR_NAME is the name of the sensor. */
	private final static String SENSOR_NAME = "simpleSensor";
	
	/** The Constant ATTRIBUTE_TIME is the name for a simple timestamp attribute in milliseconds. */
	private final static String ATTRIBUTE_TIME = "timestamp";
	
	/** The Constant ATTRIBUTE_NAME is the name for a string. */
	private final static String ATTRIBUTE_NAME = "name";
	
	/** The Constant ATTRIBUTE_ID is the name for a long*/
	private final static String ATTRIBUTE_ID = "id";
	
	/** The Constant ATTRIBUTE_X_VALUE is the name for a double value */
	private final static String ATTRIBUTE_X_VALUE = "x_value";
	
	/** The Constant ATTRIBUTE_Y_VALUE is the name for another double value */
	private final static String ATTRIBUTE_Y_VALUE = "y_value";
	
	/** The Constant ATTRIBUTE_NUMBER is the name for an integer attribute */
	private final static String ATTRIBUTE_NUMBER = "number";
	
	/** The Constant ODYSSEUS_USERNAME is the username for the login */
	private final static String ODYSSEUS_USERNAME = "System";
	
	/** The Constant ODYSSEUS_PASSWORD is the password for the login */
	private final static String ODYSSEUS_PASSWORD = "manager";
	
	/**
	 * Bindet den Service.
	 *
	 * @param sensorService der sensor-service der gestartet wurde
	 */
	public void bindSensorService(ISensorService sensorService){
		this.sensorService = sensorService;
		sensorService.setCredentials(ODYSSEUS_USERNAME, ODYSSEUS_PASSWORD);
		System.out.println("SensorService bound!");
		
		// Datentypen setzen, damit man weiß, was für ein Datentyp ein Attribut hat.
		// nennt sich dann schema des sensors
		// Das Schema darf sich auch nicht während der Laufzeit ändern.				
		Schema schema = new Schema();
		schema.addAttribute(ATTRIBUTE_TIME, DataType.STARTTIMESTAMP);
		schema.addAttribute(ATTRIBUTE_NAME, DataType.STRING);
		schema.addAttribute(ATTRIBUTE_ID, DataType.LONG);
		schema.addAttribute(ATTRIBUTE_X_VALUE, DataType.DOUBLE);
		schema.addAttribute(ATTRIBUTE_Y_VALUE, DataType.DOUBLE);
		schema.addAttribute(ATTRIBUTE_NUMBER, DataType.INTEGER);
		//erstelle einen neuen Sensor
		sensorService.createSensor(SENSOR_NAME, schema, false);
		System.out.println("Sensor created.");
		this.start();
		System.out.println("Sensor is running.");
		//generate first values
		this.generateValues();
	}
	
	/**
	 * Löst den Service wieder von diesem Bundle.
	 *
	 * @param sensorService der Service, der gestoppt wird
	 */
	public void unbindSensorService(ISensorService sensorService){
		sensorService.removeSensor(SENSOR_NAME);
		this.sensorService = null;
		System.out.println("SensorService unbound!");
	}
	
	/**
	 * Prüft ob der Sensor-Service gebunden wurde.
	 *
	 * @return true falls gebunden
	 */
	public boolean isSensorServiceBound(){
		if(this.sensorService!=null){
			return true;
		}
        return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {			
		while(true){
			//... wenn der sensor-service gebunden ist
			if(isSensorServiceBound()){
				
				// Erzeuge ein neues Tuple
				DataTuple tuple = new DataTuple();
				
				// setze danach die werte des Tuples
				
				// Zeit soll aktuelle systemzeit sein
				tuple.addAttribute(ATTRIBUTE_TIME, new Long(System.currentTimeMillis()));
				// ein name als string
				tuple.addAttribute(ATTRIBUTE_NAME, "ein name "+this.number);
				//die id ist immer 1 (aber ein Long, weil es oben als Long auch angegeben wurde!)
				tuple.addAttribute(ATTRIBUTE_ID, new Long(1));
				//ein random double als x-wert
				tuple.addAttribute(ATTRIBUTE_X_VALUE, this.x_value);
				//ein random double als y-wert
				tuple.addAttribute(ATTRIBUTE_Y_VALUE, this.y_value);
				//eine fortlaufende nummer
				tuple.addAttribute(ATTRIBUTE_NUMBER, this.number);
				
				//existiert der sensor?
				if(this.sensorService.isSensorExistent(SENSOR_NAME)){
					//dann sende das tupel an odysseus				
					this.sensorService.getSensor(SENSOR_NAME).sendTuple(tuple);
				}
				//System.out.println(tuple);
				// generiere ein paar neue Werte zur Simulation eines Sensors
				this.generateValues();
				
				// warte ein bisschen
				try {
					Thread.sleep(1);					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * Generate some example values.
	 */
	private void generateValues() {
		Random rand = new Random();
		this.x_value = rand.nextDouble()+rand.nextInt(100);
		this.y_value = rand.nextDouble()+rand.nextInt(1000);
		this.number++;
		
	}
}
