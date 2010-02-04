package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.exception.ActionException;
import de.uniol.inf.is.odysseus.action.operator.EventTriggerPO;
import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * An Action is the combination of an {@link IActuator} and
 * a Method that should be executed. In Combination with {@link ActionParameter}s
 * it can be executed by a {@link EventTriggerPO}.
 * @see EventTriggerPO
 * @see IActuator
 * @author Simon Flandergan
 *
 */
public class Action {
	private IActuator actuator;
	private String methodName;
	private ActionMethod method;
	
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
		for (ActionMethod method : actuator.getSchema()){
			if(method.isCompatibleTo(methodName, parameterTypes)){
				compatible = true;
				this.method = method;
				break;
			}
		}
		if (!compatible){
			throw new ActionException("Error while creating Action: Undefined Method");
		}
		
		this.actuator = actuator;
		this.methodName = methodName;
	}
	
	/**
	 * Invokes associated {@link IActuator} to execute the associated method with
	 * given parameters
	 * @param params
	 */
	public void executeMethod(Object[] params) throws ActuatorException{
		this.actuator.executeMethod(this.methodName, this.method.getParameterTypes(), params);
	}
}
