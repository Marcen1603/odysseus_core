/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.List;

import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * OSGI Service Interface for ActuatorManagers.
 * These are responsible for creation and access to each Actuator
 * @author Simon Flandergan
 *
 */
public interface IActuatorManager {
	
	/**
	 * Create a new Actuator with given name and a description to parse
	 * @param name 
	 * @param description
	 * @throws ActuatorException thrown if Actuator with given name already exists
	 */
	public IActuator createActuator(String name, String description) throws ActuatorException;
	
	/**
	 * Returns the actuator with given name.
	 * @param name
	 * @throws ActuatorException thrown if Actuator does not exist
	 * @return
	 */
	public IActuator getActuator(String name) throws ActuatorException;
	
	/**
	 * Returns name of ActuatorManager. Should be unique among all
	 * ActuatorManagers
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns names of all registered Actuators
	 * @return
	 */
	public List<String> getRegisteredActuatorNames();
	
	/**
	 * Removes Actuator with given name if it exists.
	 * @param name
	 * @throws ActuatorException thrown if Actuator with given name does not exist
	 */
	public IActuator removeActuator(String name) throws ActuatorException;
	
}
