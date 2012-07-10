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
package de.uniol.inf.is.odysseus.aalso.exceptions;

/**
 * Thrown when a sprite change is requested for an agent, but the sprite is
 * not available in this simulation.
 * 
 * @author Miquel Martin
 * 
 */
public class UnexistingSpriteException extends RuntimeException {

	/** Serial version UID for serialization. */
	private static final long serialVersionUID = -1156466788145693932L;

	/**
	 * Create the exception including the sprite that is not available.
	 * 
	 * @param sprite the sprite that is not available
	 */
	public UnexistingSpriteException(final String sprite) {
		super(sprite);
	}
}
