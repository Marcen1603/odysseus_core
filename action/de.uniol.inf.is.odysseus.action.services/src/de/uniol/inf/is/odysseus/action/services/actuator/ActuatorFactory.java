package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.HashMap;
import java.util.List;

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
public class ActuatorFactory implements IActuatorFactory{
	private volatile HashMap<String, IActuatorManager> actuatorManager;
			
	/**
	 * Public Constructor called by OSGI, shouldnt be called by clients,
	 * use getInstance instead!
	 */
	public ActuatorFactory () {
		this.actuatorManager = new HashMap<String, IActuatorManager>();
	}
	
	/**
	 * OSGI method for binding {@link IActuatorManager}s
	 * @param manager
	 */
	public void bindActuatorManager(IActuatorManager manager){
		this.actuatorManager.put(manager.getName(), manager);
	}
	
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
	public void unbindActuatorManager(IActuatorManager manager){
		this.actuatorManager.remove(manager.getName());
	}
	
	public IActuator getActuator (String actuatorName, String managerName) throws ActuatorException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.getActuator(actuatorName);
		}
		throw new ActuatorException("Referenced manager <"+managerName+"> does not exist");
	}

	
	public List<ActionMethod> getSchema(String actuatorName, String managerName) throws ActuatorException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.getSchema(actuatorName);
		}
		throw new ActuatorException("Referenced manager <"+managerName+"> does not exist");
	}
	
	public HashMap<String, IActuatorManager> getActuatorManagers() {
		return actuatorManager;
	}

	@Override
	public void removeActuator(String actuatorName, String managerName) throws ActuatorException {
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			manager.removeActuator(actuatorName);
			System.out.println("<"+managerName+"> successfully removed: <"+actuatorName+">");
		}else {
			throw new ActuatorException("Referenced manager <"+managerName+"> does not exist");
		}
	}
	
	

}
