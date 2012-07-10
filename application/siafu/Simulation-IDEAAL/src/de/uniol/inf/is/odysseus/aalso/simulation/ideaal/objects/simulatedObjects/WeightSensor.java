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
package de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.simulatedObjects;

import java.util.Random;

import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.Sensor;
import de.uniol.inf.is.odysseus.aalso.types.FloatNumber;
import de.uniol.inf.is.odysseus.aalso.types.Publishable;

/**
 * Represents a weight sensor
 * 
 * @author Jan-Benno Meyer zu Holte
 *
 */
public class WeightSensor extends Sensor {

	/**
	 * Defines the maximal sensorvalue variance
	 */
	private double sensorVariance = 1.0f;
	
	/**
	 * Just calling the superclass constructor
	 * 
	 * @param name @see {@link de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.Sensor#Sensor(String, Publishable, int)}
	 * @param initialValue @see {@link de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.Sensor#Sensor(String, Publishable, int)}
	 * @param frequency  @see {@link de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.Sensor#Sensor(String, Publishable, int)}
	 */
	public WeightSensor(String name, Publishable initialValue, int frequency) {
		super(name, initialValue, frequency);
	}

	@Override
	public Publishable generateCurrentValue(Publishable inputValue) {
		FloatNumber inputValueCasted = (FloatNumber) inputValue;
		Random generator = new Random();
		double gaussian = generator.nextGaussian() * sensorVariance;
		// generate the sensor-variance by a gaussian-value in regard of the maximal sensor-variance
		double value = inputValueCasted.getNumber() + gaussian;
		this.setCurrentValue(new FloatNumber(value));
		return this.getCurrentValue();
	}
}
