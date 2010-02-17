package de.uniol.inf.is.odysseus.action.services.actuator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * Actuator bases on reflections. Allows the use of any java-class as 
 * Actuator when extending this class. It does not have to implements any
 * special framework specific functionality! Schema and method execution is
 * retrieved dynamically through reflections technology
 * @author Simon Flandergan
 *
 */
public class ActuatorAdapter implements IActuator {
	private Object adapter;
	private ArrayList<ActionMethod> methods;
	
	public ActuatorAdapter(Object adapter) {
		this.adapter = adapter;
		this.methods = new ArrayList<ActionMethod>();
		for (Method method : this.adapter.getClass().getMethods()) {
			boolean show = false;
			if (method.isAnnotationPresent(ActuatorAdapterSchema.class)){
				if (!method.getAnnotation(ActuatorAdapterSchema.class).provide()){
					//do not provide this method
					continue;
				}
				show = method.getAnnotation(ActuatorAdapterSchema.class).show();
			}
			
			this.methods.add(
					new ActionMethod(method.getName(), method.getParameterTypes(), new String[]{}, show));
		}
	}
	
	@Override
	public List<ActionMethod> getFullSchema() {
		return this.methods;
	}
	
	
	@Override
	public void executeMethod(String method, Class<?>[] types, Object[] params) throws ActuatorException {
		try {
			Method methodToExecute = this.adapter.getClass().getMethod(method, types);
			methodToExecute.invoke(this.adapter, params);
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
		}catch (NullPointerException e){
			throw new ActuatorException(e.getMessage());
		}
	}

	@Override
	public List<ActionMethod> getReducedSchema() {
		ArrayList<ActionMethod> reducedSchema = new ArrayList<ActionMethod>();
		for (ActionMethod m : this.methods){
			if (m.isShowInSchema()){
				reducedSchema.add(m);
			}
			
		}
		return reducedSchema;
	}

}
