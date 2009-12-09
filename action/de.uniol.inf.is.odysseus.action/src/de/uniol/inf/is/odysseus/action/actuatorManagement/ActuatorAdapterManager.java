package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.action.exception.ActuatorCreationException;
import de.uniol.inf.is.odysseus.action.output.ActuatorAdapter;
import de.uniol.inf.is.odysseus.action.output.WorkflowClient;


public class ActuatorAdapterManager implements IActuatorManager{
	private HashMap<String, ActuatorAdapter> adapters;
	
	private ActuatorAdapterManager () {
		this.adapters = new HashMap<String, ActuatorAdapter>();
	}
	

	public IActuator getActuator(String name) {
		return this.adapters.get(name);
	}

	@Override
	public void createActuator(String description) throws ActuatorCreationException{
		//TODO classenName und parameter parsen
		String className = "";
		Object[] constructorParams = new Object[6];
		try {
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
						this.adapters.put(className, (ActuatorAdapter)constructor.newInstance(constructorParams));
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

	@Override
	public List<ActuatorMethod> getSchema(String name) {
		ActuatorAdapter adapter = this.adapters.get(name);
		if (adapter != null){
			return adapter.getSchema();
		}
		return null;
	}


	@Override
	public String getName() {
		return "ActuatorAdapterManager";
	}
}
