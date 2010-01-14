package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * Interface describing the factory service for the management of {@link IActuator}s.
 * @author Simon Flandergan
 *
 */
public interface IActuatorFactory {
	
	/**
	 * Create a new Actuator
	 * @param name name of the Actuator, must be unique for the chosen {@link IActuatorManager}!
	 * @param actuatorDescription description for actuator, will be parsed by {@link IActuatorManager}
	 * @param managerName name of the manager responsible for the new Actuator
	 * @throws ActuatorException
	 */
	public IActuator createActuator(String name, String actuatorDescription, String managerName)
		throws ActuatorException;
	
	/**
	 * Removes an existing Actuator
	 * @param name
	 */
	public void removeActuator(String actuatorName, String managerName) throws ActuatorException;
	
	/**
	 * Looks up for specified Actuator and returns it
	 * @param actuatorName
	 * @param managerName
	 * @return
	 * @throws ActuatorException
	 */
	public IActuator getActuator(String actuatorName, String managerName) throws ActuatorException;

	/**
	 * Fetches and returns the schema of specified Actuator
	 * @param actuatorName
	 * @param managerName
	 * @return
	 * @throws ActuatorException
	 */
	public List<ActionMethod> getSchema(String actuatorName, String managerName) throws ActuatorException;
	
	/**
	 * Returns all registered ActuatorManagers
	 * @return
	 */
	public HashMap<String, IActuatorManager> getActuatorManagers();
}
