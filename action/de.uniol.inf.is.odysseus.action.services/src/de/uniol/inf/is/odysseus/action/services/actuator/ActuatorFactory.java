package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Logger logger;
			
	/**
	 * Public Constructor called by OSGI, shouldnt be called by clients,
	 * use getInstance instead!
	 */
	public ActuatorFactory () {
		this.actuatorManager = new HashMap<String, IActuatorManager>();
		this.logger = LoggerFactory.getLogger(ActuatorFactory.class);
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
			IActuator actuator = manager.createActuator(name, actuatorDescription);
			this.logger.debug(manager.getClass()+ " created instance of "+actuator.getClass());
			return actuator;
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
			IActuator actuator = manager.removeActuator(actuatorName);
			this.logger.debug(manager.getClass()+" removed "+ actuator.getClass() +"with id: <"+actuatorName);
		}else {
			throw new ActuatorException("Referenced manager <"+managerName+"> does not exist");
		}
	}
	
	

}
