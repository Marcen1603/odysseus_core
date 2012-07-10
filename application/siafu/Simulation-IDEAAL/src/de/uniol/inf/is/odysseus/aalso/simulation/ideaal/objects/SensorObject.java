/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.aalso.model.Agent;
import de.uniol.inf.is.odysseus.aalso.model.Place;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.Sensor;
import de.uniol.inf.is.odysseus.aalso.types.IntegerNumber;
import de.uniol.inf.is.odysseus.aalso.types.Publishable;

/**
 * An object encapsulating a specific amount of sensors, pre-processing the sensor-values for the actual datastream.
 * Has a "Place"-Object as reference in the simulation.
 * 
 * @author Jan Meyer zu Holte
 * 
 */
public abstract class SensorObject {

	/**
	 * Represents the actual position of the SensorObject in the simulation and holds the values of the sensors for the datastream as context variables
	 */
	private Place reference;
	/**
	 * The current value of the SensorObject - in most cases a value produced by pre-processed sensor values
	 */
	private Publishable currentValue;
	/**
	 * The number of sensors in this SensorObject implementation
	 */
	private int numOfSensors = 0;
	/**
	 * The frequency of the datastream produced by this SensorObject
	 */
	private int sensorFrequency = 5; 

	/**
	 * ArralyList with all sensors used by this SensorObject
	 */
	private ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
	
	/**
	 * 
	 * Generates instance with default values
	 * 
	 * @param reference @see {@link #reference}
	 * @param numOfSensors @see {@link #numOfSensors}
	 * @param frequency @see {@link #sensorFrequency}
	 */
	public SensorObject(Place reference, int numOfSensors, int frequency) {
		this.setReference(reference);
		this.setNumOfSensors(numOfSensors);
		this.setSensorFrequency(frequency);
		this.getReference().set("frequency", new IntegerNumber(this.getSensorFrequency()));
	}
	
	/**
	 * Generates an overall SensorObject-value (e.g. by given sensor values)
	 * 
	 * @return the current value of the SensorObject
	 */
	public abstract Publishable generateCurrentValues(Agent agent);

	public void setSensorList(ArrayList<Sensor> sensorList) {
		this.sensorList = sensorList;
	}

	public ArrayList<Sensor> getSensorList() {
		return sensorList;
	}

	public void setReference(Place reference) {
		this.reference = reference;
	}

	public Place getReference() {
		return reference;
	}

	public void setCurrentValue(Publishable currentValue) {
		this.currentValue = currentValue;
	}

	public Publishable getCurrentValue() {
		return currentValue;
	}

	public void setNumOfSensors(int numOfSensors) {
		this.numOfSensors = numOfSensors;
	}

	public int getNumOfSensors() {
		return numOfSensors;
	}

	public void setSensorFrequency(int sensorFrequency) {
		this.sensorFrequency = sensorFrequency;
	}

	public int getSensorFrequency() {
		return sensorFrequency;
	}
}
