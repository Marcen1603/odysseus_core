package de.uniol.inf.is.odysseus.action.output;

import java.util.List;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorAdapterMethod;
import de.uniol.inf.is.odysseus.action.exception.ActuatorException;

public interface IActuator {
	
	public void executeMethod (String method, Class<?>[] types, Object[] params) throws ActuatorException;
	
	public List<ActuatorAdapterMethod> getSchema();

}
