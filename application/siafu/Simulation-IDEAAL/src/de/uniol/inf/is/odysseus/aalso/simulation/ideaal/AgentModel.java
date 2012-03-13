/*
 * Copyright NEC Europe Ltd. 2006-2007
 * 
 * This file is part of the context simulator called Siafu.
 * 
 * Siafu is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Siafu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.uniol.inf.is.odysseus.aalso.simulation.ideaal;

import static de.uniol.inf.is.odysseus.aalso.simulation.ideaal.Constants.POPULATION;
import static de.uniol.inf.is.odysseus.aalso.simulation.ideaal.Constants.Fields.ACTIVITY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.aalso.behaviormodels.BaseAgentModel;
import de.uniol.inf.is.odysseus.aalso.exceptions.PlaceTypeUndefinedException;
import de.uniol.inf.is.odysseus.aalso.model.Agent;
import de.uniol.inf.is.odysseus.aalso.model.Place;
import de.uniol.inf.is.odysseus.aalso.model.World;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.Constants.Activity;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.PlaceTimeActivity;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.SensorObject;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.objects.simulatedObjects.SensorObjectList;
import de.uniol.inf.is.odysseus.aalso.types.EasyTime;
import de.uniol.inf.is.odysseus.aalso.types.FloatNumber;
import de.uniol.inf.is.odysseus.aalso.types.Text;

import de.uniol.inf.is.odysseus.aalso.types.IntegerNumber;
import de.uniol.inf.is.odysseus.aalso.simulation.ideaal.AgentGenerator;



/**
 * The Agent Model defines the behavior of the Agents in this Simulation.
 * 
 * @author Jan-Benno Meyer zu Holte
 * 
 */
public class AgentModel extends BaseAgentModel {
	
	/**
	 * ArrayList containing all sensorobjects of the simulation (filled in constructor)
	 */
	private SensorObjectList sensorObjectList;

	/**
	 * The protagonist.
	 */
	private Agent protagonist;
	
	/**
	 * The default Agent
	 */
	private Agent defaultAgent;

	/**
	 * The current time.
	 */
	private EasyTime now;
	
	/**
	 * Arraylist of place, time andactivity where the agent has to go and what he is doing
	 */
	ArrayList<PlaceTimeActivity> scenario = new ArrayList<PlaceTimeActivity>();
	
	/**
	 * Constructor for the agent model.
	 * 
	 * @param world the simulation's world
	 */
	public AgentModel(final World world) {
		super(world);
		createScenario();
	}
	
	/**
	 * Reads the scenariofile and fills the scenario ArrayList
	 */
	public void createScenario(){
		try {
			File scenarioFile = new File("Simulations\\ideaalScenario1.aalso");
			BufferedReader f = new BufferedReader(new FileReader(scenarioFile));
			String line = "";
			while((line = f.readLine()) != null){
				this.scenario.add(new PlaceTimeActivity(line));
			}
			f.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Creates an ArrayList with agents and sets some context variables
	 * 
	 * @return the created agents
	 */
	@Override
    public ArrayList<Agent> createAgents() {
		System.out.println("Creating " + POPULATION + " people.");
		ArrayList<Agent> people = AgentGenerator.createRandomPopulation(POPULATION, world);

		Iterator<Agent> peopleIt = people.iterator();
		
		// Set the values of the default agent 
		defaultAgent = peopleIt.next();
		defaultAgent.setName("DefaultAgent");
		defaultAgent.setImage("HumanYellow");
		defaultAgent.getControl();
		defaultAgent.set(ACTIVITY, Activity.STEHEN);
		defaultAgent.set("Language", new Text("Deutsch"));
		defaultAgent.set("Weight", new FloatNumber(0.5));
		defaultAgent.set("Age", new IntegerNumber(1));	
		defaultAgent.set("Pulse", new IntegerNumber(1));
		defaultAgent.setVisible(false);
		
		// Set the values of the simulations protagonist
		protagonist = peopleIt.next();
		protagonist.setName("Haribert");
		protagonist.setImage("HumanGreen");
		protagonist.setVisible(true);
		protagonist.setSpeed(45);
		protagonist.getControl();
		protagonist.set(ACTIVITY, Activity.STEHEN);
		protagonist.set("Language", new Text("Deutsch"));
		protagonist.set("Weight", new FloatNumber(85.2));
		protagonist.set("Age", new IntegerNumber(76));		
		protagonist.set("Pulse", new IntegerNumber(64));

		
		try {
			protagonist.setPos(world.getPlacesOfType("Haustuer").iterator().next().getPos());
		} catch (PlaceTypeUndefinedException e) {
			throw new RuntimeException(e);
		}
		

		sensorObjectList = new SensorObjectList(world, defaultAgent);
		return people;
	}

	/**
	 * Counter for the current index in the scenario ArrayList
	 */
	private int scenarioLineCounter = 0;
	
	/**
	 * Handles agents behavior iteration wise by setting the current time and calling handleProtagonist Method
	 * @see #handleProtagonist()
	 * 
	 * @param agents the list of agents
	 */
	@Override
    public void doIteration(final Collection<Agent> agents) {
		Calendar time = world.getTime();
		now = new EasyTime(time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
		handleProtagonist();
	}

	/**
	 * Handles the behaviour (Position and Activity) of the protagonist (Haribert) by reading the information of the current State.
	 * 
	 * @see #scenario
	 * @see #handleActivity(Agent)
	 */
	private void handleProtagonist(){
		PlaceTimeActivity currentState = scenario.get(scenarioLineCounter);
		if(now.getHour() == currentState.getHour() && now.getMinute() == currentState.getMinute()){
			Iterator<Place> currentPlace;
			Activity currentActivity;
			try {
				currentState.generateCurrentPlaceAndActivity();
				currentPlace = world.getPlacesOfType(currentState.getCurrentPlaceName()).iterator();
				currentActivity = currentState.getCurrentActivity();
				clearSensors();
			} catch (PlaceTypeUndefinedException e) {
				throw new RuntimeException(e);
			}
			// Define the next position for the agent
			protagonist.setDestination(currentPlace.next());
			// set the agents activity
			protagonist.set(ACTIVITY, currentActivity);
			// increase or reset lineCounter
			scenarioLineCounter++;
			if(scenarioLineCounter == scenario.size()){
				scenarioLineCounter = 0;
			}
		} else if(scenarioLineCounter != 0) {
			currentState = scenario.get(scenarioLineCounter - 1);
			// Change agent context variables if necessary
			handleActivity(protagonist);
			try {
				// Generate SensorObject values
				SensorObject currentSensorObject = sensorObjectList.getSensorObjects().get(currentState.getCurrentPlaceName());
				if(protagonist.getPos().equals(currentSensorObject.getReference().getPos())) {
					currentSensorObject.generateCurrentValues(protagonist);
					// System.out.println(currentSensorObject.getReference().getName());
				}
			} catch(Exception ex) {
				// Do nothing! Agent is just wandering around
				// System.out.println(ex.getMessage());
			}
		} else if(scenarioLineCounter == 0) {
			currentState = scenario.get(scenario.size() - 1);
			// Change agent context variables if necessary
			handleActivity(protagonist);
			try {
				// Generate SensorObject values
				SensorObject currentSensorObject = sensorObjectList.getSensorObjects().get(currentState.getCurrentPlaceName());
				if(protagonist.getPos().equals(currentSensorObject.getReference().getPos())) {
					currentSensorObject.generateCurrentValues(protagonist);
				}
			} catch(Exception ex) {
				// Do nothing! Agent is just wandering around
				// System.out.println(ex.getMessage());
			}
		}
	}
	
	/**
	 * Handels the agents context variables in regard of the performed activity
	 * Just wandering through a switch instruction calling specific Methods to handle Activity
	 * 
	 * @param a the agent whos context variables will be changed
	 */
	private void handleActivity(final Agent a) {
		switch ((Activity) a.get(ACTIVITY)) {
		case GEHEN:
			break;
		case DUSCHEN:
			break;
		case SCHLAFEN:
			break;
		case URINIEREN:
			break;
		case SITZEN:
			break;
		case STEHEN:
			break;
		case HAENDEWASCHEN:
			break;
		case STUHLGANG:
			break;
		case SITZENUNDFERNSEHEN:
			break;
		case SITZENUNDESSEN:
			break;
		case SPUELEN:
			break;
		case KARDIO:
			doCardio(a);
			break;
		case WEGGEHEN:
			break;
		case LESEN:
			break;
		default:
			throw new RuntimeException("Unable to handle activity " + (Activity) a.get(ACTIVITY));
		}

	}
	
	/**
	 * global variable for the doCardio method
	 * @see #doCardio(Agent)
	 */
	private boolean cardioCtrl = false;
	/**
	 * global variable for the doCardio method
	 * @see #doCardio(Agent)
	 */
	private boolean decreasePulse = false;
	/**
	 * global variable for the doCardio method
	 * @see #doCardio(Agent)
	 */
	private int iterationCounter = 0;
	
	/**
	 * simulates a higher heartbeat of the agent "doing cardio" setting the context variable "Pulse"
	 * 
	 * @param a the agent whos context variables will be changed
	 */
	private void doCardio(Agent a) {
		int currentPulse = ((IntegerNumber) a.get("Pulse")).getNumber();
		if(currentPulse >= 60 && currentPulse < 125 && !cardioCtrl){
			if(!decreasePulse){
				currentPulse++;
			} else {
				currentPulse--;
				if(currentPulse == 60){
					decreasePulse = false;
				}
			}
		} else if(currentPulse == 125 && !cardioCtrl) {
			cardioCtrl = true;
			currentPulse--;
		} else if(cardioCtrl) {
			iterationCounter++;
		} 
		if(iterationCounter == 15){
			iterationCounter = 0;
			cardioCtrl = false;
			decreasePulse = true;
			currentPulse--;
		}
		a.set("Pulse", new IntegerNumber(currentPulse));
	}

	/**
	 * Clears all sensorvalues and agent context variables
	 */
	private void clearSensors() {
		// clear Bed-Sensors
		sensorObjectList.getSensorObjects().get("Bed").generateCurrentValues(defaultAgent);
		// clear Ergometer Sensors and Protagonist Pulse
		sensorObjectList.getSensorObjects().get("Ergometer").generateCurrentValues(defaultAgent);
		protagonist.set("Pulse", new IntegerNumber(64));
	}

	public SensorObjectList getSensorObjectList() {
		return sensorObjectList;
	}

	public void setSensorObjectList(SensorObjectList sensorObjectList) {
		this.sensorObjectList = sensorObjectList;
	}
	
}
