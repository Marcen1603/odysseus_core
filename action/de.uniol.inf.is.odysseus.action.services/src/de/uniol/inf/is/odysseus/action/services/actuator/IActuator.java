package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.List;

import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

public interface IActuator {
	
	public void executeMethod (String method, Class<?>[] types, Object[] params) throws ActuatorException;
	
	public List<ActionMethod> getSchema();

}
