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

package de.uniol.inf.is.odysseus.aalso.exceptions;

/**
 * This exception is thrown when a place is searched by name, and not found.
 * 
 * @author Miquel Martin
 * 
 */
public class PlaceNotFoundException extends TrackableNotFoundException {

	/** Default serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** Throw the exception plus the name of the place that has not been found.
	 * 
	 * @param name the agent that was being searched for
	 */
	public PlaceNotFoundException(final String name) {
		super("Person not found: \"" + name + "\".");
	}
}
