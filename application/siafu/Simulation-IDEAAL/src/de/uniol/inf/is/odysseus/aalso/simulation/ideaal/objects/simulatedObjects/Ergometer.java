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

import de.uniol.inf.is.odysseus.aalso.model.Agent;
import de.uniol.inf.is.odysseus.aalso.model.Place;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.Sensor;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.SensorObject;
import de.uniol.inf.is.odysseus.aalso.types.IntegerNumber;
import de.uniol.inf.is.odysseus.aalso.types.Publishable;

/**
 * Represents the Ergometer in the simulation
 * 
 * @author Jan-Benno Meyer zu Holte
 *
 */
public class Ergometer extends SensorObject {

	/**
	 * Setting up the sensors and first values
	 * 
	 * @param reference @see {@link de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.SensorObject#SensorObject(Place, int, int)}
	 * @param numOfSensors @see {@link de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.SensorObject#SensorObject(Place, int, int)}
	 * @param frequency @see {@link de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.SensorObject#SensorObject(Place, int, int)}
	 * @param defaultAgent An agent object with default values for the sensors
	 */
	public Ergometer(Place reference, int numOfSensors, int frequency, Agent defaultAgent) {
		super(reference, numOfSensors, frequency);
		IntegerNumber defaultValueCasted = (IntegerNumber) defaultAgent.get("Pulse");
		this.getSensorList().add(new PulseSensor("Ergometer-Sensor 1", defaultValueCasted, frequency));
		this.generateCurrentValues(defaultAgent);
	}
	
	@Override
	public Publishable generateCurrentValues(Agent agent) {
		// getting the agents heart-beat-frequency
		IntegerNumber inputValueCasted = (IntegerNumber) agent.get("Pulse");
		int sensorValues = 0;
		// Generate the sensorObject value by calling the sensors generateCurrentValue method
		for(Sensor sensor : this.getSensorList()) {
			sensor.generateCurrentValue(inputValueCasted);
			sensorValues += ((IntegerNumber)sensor.getCurrentValue()).getNumber();
		}
		this.setCurrentValue(new IntegerNumber(sensorValues));
		// Set the context variables of the place
		this.getReference().set("Ergometer-Sensor", this.getCurrentValue());
		this.getReference().set("Ergometer-Sensor1", this.getSensorList().get(0).getCurrentValue());
		return this.getCurrentValue();
	}

}
