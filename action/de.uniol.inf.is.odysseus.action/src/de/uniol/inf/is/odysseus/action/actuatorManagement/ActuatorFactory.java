package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;

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
	
	public void createActuator (String name, String actuatorDescription, String managerName) 
		throws ActuatorException{
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			manager.createActuator(name, actuatorDescription);
		}
		throw new ActuatorException("Actuator manager not bound yet");
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
	
	public List<ActuatorMethod> getSchema(String actuatorName, String managerName){
		IActuatorManager manager = this.actuatorManager.get(managerName);
		if (manager != null){
			return manager.getSchema(actuatorName);
		}
		return null;
	}
	
	

}
