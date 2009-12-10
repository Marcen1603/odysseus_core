package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;

/**
 * OSGI Service Interface for ActuatorManagers
 * @author Simon
 *
 */
public interface IActuatorManager {
	
	public void createActuator(String name, String description) throws ActuatorException;
	
	public IActuator getActuator(String name);
	
	public String getName();
	
	public List<ActuatorMethod> getSchema(String name);
	
}
