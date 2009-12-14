package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.exception.ActionException;
import de.uniol.inf.is.odysseus.action.exception.ActuatorException;


public class Action {
	private IActuator actuator;
	private String methodName;
	private Class<?>[] parameterTypes;
	
	public Action (IActuator actuator, String methodName, Class<?>[] parameterTypes) throws ActionException{
		boolean compatible = false;
		//check for compatibility
		for (ActionMethod method : actuator.getSchema()){
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
	
	public void executeMethod(Object[] params){
		try {
			this.actuator.executeMethod(this.methodName, this.parameterTypes, params);
		} catch (ActuatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
