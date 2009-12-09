package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.List;
import java.util.Map;

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
	
	public Map<String, List<Class<?>>> getSchema(String name);
	
}
