package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.output.ActionMethod;
import de.uniol.inf.is.odysseus.action.output.IActuator;

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
	 * Returns the schema, list of all Methods, of the Actuator with give name.
	 * @param name
	 * @return
	 * @throws ActuatorException thrown if Actuator does not exist
	 * @see ActionMethod
	 */
	public List<ActionMethod> getSchema(String name) throws ActuatorException;
	
}
