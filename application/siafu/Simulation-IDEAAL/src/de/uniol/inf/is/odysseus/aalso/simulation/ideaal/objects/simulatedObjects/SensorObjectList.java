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

import java.util.HashMap;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.aalso.exceptions.PlaceTypeUndefinedException;
import de.uniol.inf.is.odysseus.aalso.model.Agent;
import de.uniol.inf.is.odysseus.aalso.model.Place;
import de.uniol.inf.is.odysseus.aalso.model.World;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.SensorObject;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.simulatedObjects.Bed;

/**
 * This class holds all SensorObject instances of the simulation 
 * 
 * @author Horizon
 *
 */
public class SensorObjectList {
	
	/**
	 * HashMap identifying all SensorObject instances of the simulation
	 */
	private HashMap<String, SensorObject> sensorObjects = new HashMap<String, SensorObject>();
	
	/**
	 * Default Agent with default contextVariables
	 */
	private Agent defaultAgent;

	/**
	 * Instance of the simulation world
	 */
	private World world;
	
	/**
	 * Just sets the global variables and calls the generateSensorObject method
	 * 
	 * @see #generateSensorObjects()
	 * 
	 * @param world World-Object of the current Simulation
	 * @param agent The agent with the default values for the Sensors
	 */
	public SensorObjectList(World world, Agent agent){
		this.setWorld(world);
		this.setDefaultAgent(agent);
		generateSensorObjects();
	}

	/**
	 * All SensorObect instances that should produce a datastream must be initialized here
	 */
	private void generateSensorObjects() {
		// Load Bed-SensorObject
		Iterator<Place> bed;
		try {
			bed = world.getPlacesOfType("Bed").iterator();
		} catch (PlaceTypeUndefinedException e) {
			throw new RuntimeException(e);
		}
		// add the instance to the HashMap
		sensorObjects.put("Bed", new Bed(bed.next(), 4, 10, defaultAgent));
		// Load Ergometer-SensorObject
		Iterator<Place> ergometer;
		try {
			ergometer = world.getPlacesOfType("Ergometer").iterator();
		} catch (PlaceTypeUndefinedException e) {
			throw new RuntimeException(e);
		}
		// add the instance to the HashMap
		sensorObjects.put("Ergometer", new Ergometer(ergometer.next(), 1, 5, defaultAgent));
	}

	public void setSensorObjects(HashMap<String, SensorObject> sensorObjects) {
		this.sensorObjects = sensorObjects;
	}

	public HashMap<String, SensorObject> getSensorObjects() {
		return sensorObjects;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}
	
	private void setDefaultAgent(Agent agent) {
		this.defaultAgent = agent;
	}
}
