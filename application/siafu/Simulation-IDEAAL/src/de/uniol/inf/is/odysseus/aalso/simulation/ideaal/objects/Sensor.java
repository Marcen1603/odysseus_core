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

import de.uniol.inf.is.odysseus.aalso.types.Publishable;

/**
 * Represents a single simulated sensor
 * 
 * @author Jan-Benno Meyer zu Holte
 *
 */
public abstract class Sensor {
	/**
	 * The name of the sensor
	 */
	private String name = "";
	/**
	 * The current value generated by the sensor
	 * 
	 * @see #generateCurrentValue(Publishable)
	 */
	private Publishable currentValue;
	/**
	 * The frequency the sensor is sending the datastream - has no effect in this case!
	 */
	private int frequency;
	
	/**
	 * Sets name and generates current sensor value by calling generateCurrentValue() method with specific frequency
	 * 
	 * @param name Name of the sensor
	 * @param initialValue initial value of the sensor
	 * @param frequency frequency of the sensor
	 */
	public Sensor(String name, Publishable initialValue, int frequency) {
		this.setName(name);
		this.setCurrentValue(this.generateCurrentValue(generateCurrentValue(initialValue)));
		this.setFrequency(frequency);
	}
	
	/**
	 * Generates the sensor-value by a given input value
	 * 
	 * @return the current value of the sensor
	 */
	public abstract Publishable generateCurrentValue(Publishable inputValue);
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setCurrentValue(Publishable value) {
		this.currentValue = value;
	}
	public Publishable getCurrentValue() {
		return currentValue;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getFrequency() {
		return frequency;
	}
		
}
