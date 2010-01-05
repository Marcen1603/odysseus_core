package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorAdapterMethod;
import de.uniol.inf.is.odysseus.action.exception.ActionException;
import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.operator.EventDetectionPO;

/**
 * An Action is the combination of an {@link IActuator} and
 * a Method that should be executed. In Combination with {@link IActionParameter}s
 * it can be executed by a {@link EventDetectionPO}.
 * @see EventDetectionPO
 * @see IActuator
 * @author Simon Flandergan
 *
 */
public class Action {
	private IActuator actuator;
	private String methodName;
	private Class<?>[] parameterTypes;
	
	/**
	 * Creates a new Action
	 * @param actuator
	 * @param methodName method associated to actuator
	 * @param parameterTypes parameter classtypes of method
	 * @throws ActionException
	 */
	public Action (IActuator actuator, String methodName, Class<?>[] parameterTypes) throws ActionException{
		boolean compatible = false;
		//check for compatibility
		for (ActuatorAdapterMethod method : actuator.getSchema()){
			if(method.isCompatibleTo(methodName, parameterTypes)){
				compatible = true;
				break;
			}
		}
		if (!compatible){
			throw new ActionException("Error while creating Action: Undefined Method");
		}
		
		this.actuator = actuator;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
	}
	
	/**
	 * Invokes associated {@link IActuator} to execute the associated method with
	 * given parameters
	 * @param params
	 */
	public void executeMethod(Object[] params){
		//FIXME reihenfolge der parameter evtl. korrigieren
		try {
			this.actuator.executeMethod(this.methodName, this.parameterTypes, params);
		} catch (ActuatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
