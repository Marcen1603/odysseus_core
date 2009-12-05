package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.uniol.inf.is.odysseus.action.exception.ActuatorCreationException;
import de.uniol.inf.is.odysseus.action.output.ActuatorAdapter;


public class ActuatorAdapterManager {
	private static ActuatorAdapterManager instance = new ActuatorAdapterManager();
	
	
	private ActuatorAdapterManager () {
		
	}
	
	
	public ActuatorAdapter createActuatorAdapter (String className, Object[] constructorParams) throws ActuatorCreationException{
		try {
			Object adapter = null;
			Class<?> adapterClass = Class.forName(className);
			for (Constructor<?> constructor : adapterClass.getConstructors()){
				Class<?>[] parameters = constructor.getParameterTypes();
				if (parameters.length != constructorParams.length){
					continue;
				}else {
					//check types
					boolean compatible = true;
					for (int i=0; i<parameters.length;i++){
						if (parameters[i] != constructorParams[i].getClass()){
							compatible = false;
							break;
						}
					}
					//create object
					if (compatible){
						adapter = constructor.newInstance(constructorParams);
						return (ActuatorAdapter)adapter;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			throw new ActuatorCreationException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new ActuatorCreationException(e.getMessage());
		} catch (InstantiationException e) {
			throw new ActuatorCreationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ActuatorCreationException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new ActuatorCreationException(e.getMessage());
		} catch (ClassCastException e){
			throw new ActuatorCreationException("Adapter is not derived from ActuatorAdapter.class");
		}
		
		throw new ActuatorCreationException("Constructor undefined");
	}
	
	
	public static ActuatorAdapterManager getInstance() {
		return instance;
	}
}
