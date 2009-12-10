package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;

public interface IActuator {
	
	public void executeMethod (String method, Object[] params) throws ActuatorException;
	
	public List<ActuatorMethod> getSchema();

}
