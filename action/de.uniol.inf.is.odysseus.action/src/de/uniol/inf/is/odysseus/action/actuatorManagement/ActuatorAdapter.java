package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.output.IActuator;

public abstract class ActuatorAdapter implements IActuator {
	private ArrayList<ActuatorAdapterMethod> methods;
	
	public ActuatorAdapter() {
		this.methods = new ArrayList<ActuatorAdapterMethod>();
		for (Method method : this.getClass().getMethods()) {
			this.methods.add(
					new ActuatorAdapterMethod(method.getName(),method.getParameterTypes()));
		}
	}
	
	@Override
	public List<ActuatorAdapterMethod> getSchema() {
		return this.methods;
	}
	
	
	@Override
	public void executeMethod(String method, Class<?>[] types, Object[] params) throws ActuatorException {
		try {
			Method methodToExecute = this.getClass().getMethod(method, types);
			methodToExecute.invoke(this, params);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
