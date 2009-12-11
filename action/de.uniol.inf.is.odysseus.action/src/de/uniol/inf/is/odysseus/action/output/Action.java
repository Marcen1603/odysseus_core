package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;


public class Action {
	private IActuator actuator;
	private String methodName;
	
	public Action (IActuator actuator, String methodName){
		this.actuator = actuator;
		this.methodName = methodName;
	}
	
	public void executeMethod(Object[] params){
		try {
			this.actuator.executeMethod(this.methodName, params);
		} catch (ActuatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
