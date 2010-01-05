package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.output.IActuator;

/**
 * Factory for Creation, Access to all kind of {@link IActuator}s.
 * The Management of each Actuator is done by a specific {@link IActuatorManager} referenced
 * by a unique name
 * @see IActuatorManager
 * @see IActuator
 * @author Simon Flandergan
 *
 */
public class ActuatorFactory {
	private volatile HashMap<String, IActuatorManager> actuatorManager;
	
	private static ActuatorFactory instance = new ActuatorFactory();
		
	private ActuatorFactory () {
		this.actuatorManager = new HashMap<String, IActuatorManager>();
	}
	
	/**
	 * OSGI method for binding {@link IActuatorManager}s
	 * @param manager
	 */
	public void bindActuatorManager(IActuatorManager manager){
		this.actuatorManager.put(manager.getName(), manager);
	}
	
	/**
	 * Create a new Actuator
	 * @param name name of the Actuator, must be unique for the chosen {@link IActuatorManager}!
	 * @param actuatorDescription description for actuator, will be parsed by {@link IActuatorManager}
	 * @param managerName name of the manager responsible for the new Actuator
	 * @throws ActuatorException
	 */
	public void createActuator (String name, String actuatorDescription, String managerName) 
		throws ActuatorException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			manager.createActuator(name, actuatorDescription);
		}
		throw new ActuatorException("Actuator manager not bound yet");
	}
	
	/**
	 * OSGI method for unbinding {@link IActuatorManager}s
	 * @param manager
	 */
	public void unbindActuatorManager(IActuatorManager manager){
		this.actuatorManager.remove(manager.getName());
	}
	
	/**
	 * Looks up for Actuator and returns it
	 * @param actuatorName
	 * @param managerName
	 * @return
	 * @throws ActuatorException
	 */
	public IActuator getActuator (String actuatorName, String managerName) throws ActuatorException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.getActuator(actuatorName);
		}
		throw new ActuatorException("Referenced manager <"+managerName+"> does not exist");
	}
	
	/**
	 * Returns factory instance (singleton pattern)
	 * @return
	 */
	public static ActuatorFactory getInstance() {
		return instance;
	}
	
	public List<ActuatorAdapterMethod> getSchema(String actuatorName, String managerName) throws ActuatorException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.getSchema(actuatorName);
		}
		throw new ActuatorException("Referenced manager <"+managerName+"> does not exist");
	}
	
	

}
