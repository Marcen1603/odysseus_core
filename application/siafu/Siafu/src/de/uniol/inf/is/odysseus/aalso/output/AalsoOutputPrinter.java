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
package de.uniol.inf.is.odysseus.aalso.output;

import java.util.ArrayList;
import org.apache.commons.configuration.Configuration;

import de.uniol.inf.is.odysseus.aalso.model.Place;
import de.uniol.inf.is.odysseus.aalso.model.World;

/**
 * Holds the servers for the datastreams
 * 
 * @author Horizon
 *
 */
public class AalsoOutputPrinter implements SimulatorOutputPrinter {

	/**
	 * The world object of the simulation
	 */
	private World world;
	/**
	 * Holds all servers so they can be controlled
	 */
	private ArrayList<StreamServer> server = new ArrayList<StreamServer>();
	/**
	 * The port on which the first server is sending data
	 */
	private int startingPort = 20000;
	/**
	 * The host of the server
	 */
	private String host = "localhost";
	
	/**
	 * Creates a server for each place in the simulation that has info values (context variables). 
	 * (The SensorObject instances save the current sensorvalues as context variables of the referenced Place object.)
	 * 
	 * @param world The world object of the simulation
	 * @param config The configuration file of Siafu containing the startingport and host of the simulation
	 * @throws Exception
	 */
	public AalsoOutputPrinter(World world, final Configuration config) throws Exception {
		this.setWorld(world);
		this.setStartingPort(config.getInt("output.odys.startingPort"));
		this.setHost(config.getString("output.odys.host"));
		for(Place place : world.getPlaces()){
			try {
				if(place.getInfoKeys().size() > 0){
					String placeName = (place.getName().split("-"))[0];
					this.getServer().add(new StreamServer(startingPort, host, AalsoDataProvider.class, world, placeName));
					startingPort++;
				}
			} catch (Exception exc) {
				System.out.println(exc.getMessage());
			}
		}
//		this.getServer().add(new StreamServer(startingPort + 1, host, AalsoDataProvider.class, world, "Bed"));
//		this.getServer().add(new StreamServer(startingPort + 2, host, AalsoDataProvider.class, world, "Ergometer"));
		for(StreamServer singleServer : this.getServer()){
			singleServer.start();
			Thread.sleep(50);
		}
	}
	
	@Override
	public void notifyIterationConcluded() {
		// Takes no further action in this case
	}

	/**
	 * closes the StreamServer connections
	 */
	@Override
    public void cleanup() {
		for(StreamServer singleServer : this.getServer()){
			singleServer.close();
		}
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void setServer(ArrayList<StreamServer> server) {
		this.server = server;
	}

	public ArrayList<StreamServer> getServer() {
		return server;
	}

	public void setStartingPort(int startingPort) {
		this.startingPort = startingPort;
	}

	public int getStartingPort() {
		return startingPort;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}
}
