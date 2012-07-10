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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.util.Map;

public interface IInputHandler<T> {

	/**
	 * Is called to initialize an input (e.g. to open a file or a connection)
	 */
	void init();

	/**
	 * Returns true if there is another object to read, false else
	 * @return
	 */
	boolean hasNext();

	
	/**
	 * Returns the next value. Should only be called if hasNext() was true!
	 * @return
	 */
	T getNext();

	/**
	 * Is called to clean up the input
	 */
	void terminate();

	/**
	 * Get the unique name for this handler. Needed von registration and retrieval 
	 * @return
	 */
	String getName();


	IInputHandler<T> getInstance(Map<String, String> options);
	
}
