package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorExecutionException;

public interface IActuator {
	
	public void executeMethod (String method, Object[] params) throws ActuatorExecutionException;
	
	public List<ActuatorMethod> getSchema();

}
