package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorManager;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

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
	private HashMap<String, IActuatorManager> actuatorManager;
	
	private static ActuatorFactory instance = new ActuatorFactory();
		
	/**
	 * Constructor called by OSGI, shouldnt be called by clients,
	 * use getInstance instead!
	 */
	public ActuatorFactory () {
		this.actuatorManager = new HashMap<String, IActuatorManager>();
		instance = this;
	}
	
	/**
	 * OSGI method for binding {@link IActuatorManager}s
	 * @param manager
	 */
	public synchronized void bindActuatorManager(IActuatorManager manager){
		this.actuatorManager.put(manager.getName(), manager);
	}
	
	/**
	 * Create a new Actuator
	 * @param name name of the Actuator, must be unique for the chosen {@link IActuatorManager}!
	 * @param actuatorDescription description for actuator, will be parsed by {@link IActuatorManager}
	 * @param managerName name of the manager responsible for the new Actuator
	 * @throws ActuatorException
	 */
	public IActuator createActuator (String name, String actuatorDescription, String managerName) 
		throws ActuatorException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.createActuator(name, actuatorDescription);
		}
		throw new ActuatorException("Actuator manager not bound yet");
	}
	
	/**
	 * OSGI method for unbinding {@link IActuatorManager}s
	 * @param manager
	 */
	public synchronized void unbindActuatorManager(IActuatorManager manager){
		this.actuatorManager.remove(manager.getName());
		System.out.println("ActuatorFactory unbound <"+manager+">");
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
	public static synchronized ActuatorFactory getInstance() {
		if (instance == null) {
			instance = new ActuatorFactory();
		}
		return instance;
	}
	
	public List<ActionMethod> getSchema(String actuatorName, String managerName) throws ActuatorException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.getSchema(actuatorName);
		}
		throw new ActuatorException("Referenced manager <"+managerName+"> does not exist");
	}
	
	public HashMap<String, IActuatorManager> getActuatorManager() {
		return actuatorManager;
	}
	
	

}
