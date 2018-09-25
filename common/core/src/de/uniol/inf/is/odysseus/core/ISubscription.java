/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * This interface represents a connection between a source and a sink.
 * 
 * @author Marco Grawunder
 *
 * @param <I,O>
 */

public interface ISubscription<I,O> {	
	
	/**
	 * Get the source of this connection
	 * @return
	 */
	I getSource();
	
	/**
	 * Get the sink of this connection
	 */
	O getSink();
	
	
	/**
	 * Get the targets input port
	 * @return
	 */
	public int getSinkInPort();
	
	/**
	 * Get the source output port 
	 * @return
	 */
	public int getSourceOutPort();
	
	/**
	 * Get the schema of the data that is delivered through this link 
	 * @return
	 */
	public SDFSchema getSchema();
	
}
