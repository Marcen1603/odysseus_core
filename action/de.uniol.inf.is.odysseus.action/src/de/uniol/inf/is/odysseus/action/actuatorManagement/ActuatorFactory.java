package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.action.exception.ActuatorCreationException;

/**
 * Factory for Creation, Acess to all kind of {@link AbstractActuator}s
 */
public class ActuatorFactory {
	private volatile HashMap<String, IActuatorManager> actuatorManager;
	
	private static ActuatorFactory instance = new ActuatorFactory();
		
	private ActuatorFactory () {
		this.actuatorManager = new HashMap<String, IActuatorManager>();
	}
	
	public void bindActuatorManager(IActuatorManager manager){
		this.actuatorManager.put(manager.getName(), manager);
	}
	
	public void createActuator (String actuatorDescription, String managerName) throws ActuatorCreationException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			manager.createActuator(actuatorDescription);
		}
		throw new ActuatorCreationException("Actuator manager not bound yet");
	}
	
	public void unbindActuatorManager(IActuatorManager manager){
		this.actuatorManager.remove(manager.getName());
	}
	
	
	public IActuator getActuator (String actuatorName, String managerName){
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.getActuator(actuatorName);
		}
		return null;
	}
	
	public static ActuatorFactory getInstance() {
		return instance;
	}
	
	public Map<String, List<Class<?>>> getSchema(String actuatorName, String managerName){
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.getSchema(actuatorName);
		}
		return null;
	}
	
	

}
