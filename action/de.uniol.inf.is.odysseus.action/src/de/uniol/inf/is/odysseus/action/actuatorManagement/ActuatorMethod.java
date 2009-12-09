package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.ArrayList;
import java.util.Arrays;

public class ActuatorMethod {
	private String name;
	private ArrayList<Class<?>> parameterTypes;
	
	public ActuatorMethod (String name, Class<?>[] classes){
		this.name = name;
		this.parameterTypes = new ArrayList<Class<?>>(Arrays.asList(classes));
	}
	
	public String getName() {
		return name;
	}
	
	public Class<?>[] getParameterTypes() {
		Class<?>[] parameters = new Class<?>[this.parameterTypes.size()];
		return this.parameterTypes.toArray(parameters);
	}
	
	@SuppressWarnings("unchecked")
	public boolean isCompatibleTo(String methodName, Object[] parameters){
		ArrayList<Class<?>> types = (ArrayList<Class<?>>) this.parameterTypes.clone();
		if (!this.name.equals(methodName)){
			//method name differs
			return false;
		}
		for (Object param : parameters){
			if (!types.remove(param.getClass())){
				//parameter not found
				return false;
			}
		}
		if (types.isEmpty()){
			//all parameters used
			return true;
		}
		return false;
	}

}
