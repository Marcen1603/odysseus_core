package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.output.ActionMethod;
import de.uniol.inf.is.odysseus.action.output.IActuator;

public abstract class ActuatorAdapter implements IActuator {
	private ArrayList<ActionMethod> methods;
	
	public ActuatorAdapter() {
		this.methods = new ArrayList<ActionMethod>();
		for (Method method : this.getClass().getMethods()) {
			this.methods.add(
					new ActionMethod(method.getName(),method.getParameterTypes()));
		}
	}
	
	@Override
	public List<ActionMethod> getSchema() {
		return this.methods;
	}
	
	
	@Override
	public void executeMethod(String method, Class<?>[] types, Object[] params) throws ActuatorException {
		try {
			Method methodToExecute = this.getClass().getMethod(method, types);
			methodToExecute.invoke(this, params);
		} catch (SecurityException e) {
			throw new ActuatorException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new ActuatorException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new ActuatorException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ActuatorException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new ActuatorException(e.getMessage());
		}
	}

}
