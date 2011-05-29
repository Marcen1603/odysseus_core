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

import java.util.ArrayList;
import java.util.Random;

import de.uniol.inf.is.odysseus.aalso.exceptions.PlaceNotFoundException;
import de.uniol.inf.is.odysseus.aalso.model.Agent;
import de.uniol.inf.is.odysseus.aalso.model.World;
import de.uniol.inf.is.odysseus.aalso.types.Text;

/**
 * Utility class to generate an agent with randomized parameters. 
 * Adjusted by Jan-Benno Meyer zu Holte
 * 
 * @author Miquel Martin
 * 
 */
final class AgentGenerator {

	/**
	 * A random number generator.
	 */
	private static Random generator = new Random();

	/** Prevent the class from being instantiated. */
	private AgentGenerator() {
	}

	/**
	 * Create a random agent with language set either to english or to German.
	 * 
	 * @param world the world to create it in
	 * @return the new agent
	 */
	public static Agent createRandomAgent(final World world) {
		try {
			Agent a = new Agent(world.getRandomPlaceOfType("Haustuer").getPos(), "HumanYellow", world);
			a.set("Language", new Text(generator.nextInt(2) == 0 ? "Deutsch" : "English"));
			return a;
		} catch (PlaceNotFoundException e) {
			throw new RuntimeException("You didn't define the \"Haustuer\" type of places", e);
		}
	}

	/**
	 * Create a number of random agents.
	 * 
	 * @param amount the amount of agents to create
	 * @param world the world where the agents will dwell
	 * @return an ArrayList with the created agents
	 */
	public static ArrayList<Agent> createRandomPopulation(final int amount, final World world) {
		ArrayList<Agent> population = new ArrayList<Agent>(amount);
		for (int i = 0; i < amount; i++) {
			population.add(createRandomAgent(world));
		}
		return population;
	}

}
