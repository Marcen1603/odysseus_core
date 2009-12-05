package de.uniol.inf.is.odysseus.action.actuatorManagement;

import de.uniol.inf.is.odysseus.action.output.AbstractActuator;

public class ActuatorFactory {
	private ActuatorAdapterManager adapterManager;
	private WorkflowClientManager clientManager;
	
	private ActuatorFactory () {
		
	}
	
	public AbstractActuator createActuator (String actuatorDescription){
		return null;
	}

}
