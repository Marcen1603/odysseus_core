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
	
	public void createActuator(String name, String description) throws ActuatorException;
	
	public IActuator getActuator(String name);
	
	public String getName();
	
	public List<ActionMethod> getSchema(String name);
	
}
