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
	 * Removes Actuator with given name if it exists.
	 * @param name
	 * @throws ActuatorException thrown if Actuator with given name does not exist
	 */
	public IActuator removeActuator(String name) throws ActuatorException;
	
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
	
}
