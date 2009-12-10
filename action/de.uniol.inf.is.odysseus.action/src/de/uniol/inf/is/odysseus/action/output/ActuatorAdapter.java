package de.uniol.inf.is.odysseus.action.output;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorMethod;
import de.uniol.inf.is.odysseus.action.actuatorManagement.IActuator;
import de.uniol.inf.is.odysseus.action.exception.ActuatorException;

public abstract class ActuatorAdapter implements IActuator {
	private ArrayList<ActuatorMethod> methods;
	
	public ActuatorAdapter() {
		this.methods = new ArrayList<ActuatorMethod>();
		for (Method method : this.getClass().getMethods()) {
			this.methods.add(
					new ActuatorMethod(method.getName(),method.getParameterTypes()));
		}
	}
	
	@Override
	public List<ActuatorMethod> getSchema() {
		return this.methods;
	}
	
	
	@Override
	public void executeMethod(String method, Object[] params) throws ActuatorException {
		//check if method is defined in this class
		ActuatorMethod existingMethod = null;
		for (ActuatorMethod m : this.methods){
			if (m.isCompatibleTo(method, params)){
				existingMethod = m;
				break;
			}
		}
		if (existingMethod == null){
			throw new ActuatorException("Method undefined for actuator: "+this.getClass().getName());
		}
		//execute method
		try {
			Method methodToExecute = this.getClass().getMethod(method, 
					existingMethod.getParameterTypes());
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
