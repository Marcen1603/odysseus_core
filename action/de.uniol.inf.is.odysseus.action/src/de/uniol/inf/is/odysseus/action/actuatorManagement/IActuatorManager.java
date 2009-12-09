package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorCreationException;

/**
 * OSGI Service Interface for ActuatorManagers
 * @author Simon
 *
 */
public interface IActuatorManager {
	
	public void createActuator(String description) throws ActuatorCreationException;
	
	public IActuator getActuator(String name);
	
	public String getName();
	
	public List<ActuatorMethod> getSchema(String name);
	
}
