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

import de.uniol.inf.is.odysseus.aalso.types.FlatData;
import de.uniol.inf.is.odysseus.aalso.types.Publishable;
import de.uniol.inf.is.odysseus.aalso.types.Text;

/**
 * A list of the constants used by this simulation. Defines the Activities.
 * 
 * @author Miquel Martin
 */
public class Constants {
	/**
	 * Population size, that is, how many agents should inhabit this simulation.
	 */
	public static final int POPULATION = 2;

	/** A small maximum distance to wander off a main point when wanderng. */
	public static final int SMALL_WANDER = 20;

	/** A big distance to wander off a main point when wanderng. */
	public static final int BIG_WANDER = 20;
	
	/** 120min time blur. */
	public static final int TWO_HOUR_BLUR = 120;

	/** 60min time blur. */
	public static final int ONE_HOUR_BLUR = 60;

	/** 30min time blur. */
	public static final int HALF_HOUR_BLUR = 30;


	/**
	 * The names of the fields in each agent object.
	 */
	public static class Fields {
		/** The agent's current activity. */
		public static final String ACTIVITY = "Activity";
	}

	/* FIXME the activity doesn't show the actual description */
	/**
	 * List of possible activities. This is implemented as an enum because it
	 * helps us in switch statements. Like the rest of the constants in this
	 * class, they could also have been coded directly in the model
	 */
	public enum Activity implements Publishable {
		/** The agent is walking. */
		GEHEN("Gehen"),
		/** The agent is sleeping in the bed. */
		DUSCHEN("Duschen"),
		/** The agent is sitting on the sofa. */
		SCHLAFEN("Schlafen"),
		/** The agent is sitting on the toilet doing small business. */
		URINIEREN("Kleines Gesch�ft verrichten"),
		/** The agent's sitting. */
		SITZEN("Sitzen"),
		/** The agent's standing. */
		STEHEN("Stehen"),
		/** The agent's washing his hands. */
		HAENDEWASCHEN("Haendewaschen"),
		/** The agent is sitting on the toilet doing big business. */
		STUHLGANG("Gro�es Gesch�ft verrichten"),
		/** The agent's sitting in front of the TV. */
		SITZENUNDFERNSEHEN("Sitzen und Fernsehen"),
		/** The agent's sitting and eating. */
		SITZENUNDESSEN("Sitzen und essen"),
		/** The agent's making the dishes. */
		SPUELEN("Sp�len"),
		/** The agent's doing cardio. */
		KARDIO("Kardio Training"),
		/** The agent's reading a book in the bed. */
		LESEN("Lesen"),
		/** The agent's is not at home. */
		WEGGEHEN("Nicht zu Hause");

		/** Human readable desription of the activity. */
		private String description;

		/**
		 * Get the description of the activity.
		 * 
		 * @return a string describing the activity
		 */
		public String toString() {
			return description;
		}

		/**
		 * Build an instance of Activity which keeps a human readable
		 * description for when it's flattened.
		 * 
		 * @param description the humanreadable description of the activity
		 */
		private Activity(final String description) {
			this.description = description;
		}

		/**
		 * Flatten the description of the activity.
		 * 
		 * @return a flatenned text with the description of the activity
		 */
		public FlatData flatten() {
			return new Text(description).flatten();
		}
	}
}
