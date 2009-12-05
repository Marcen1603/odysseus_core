package de.uniol.inf.is.odysseus.action.output;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Action {
	private Object actuator;
	private Method method;
	
	public Action (Object actuator, String methodName, Class<?>[] paramTypes){
		try {
			this.method = actuator.getClass().getMethod(methodName, paramTypes);
			this.actuator = actuator;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void executeMethod(Object[] params){
		try {
			method.invoke(this.actuator, params);
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
