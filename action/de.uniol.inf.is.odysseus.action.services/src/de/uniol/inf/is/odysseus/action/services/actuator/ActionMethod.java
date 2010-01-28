package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionMethod {
	private String name;
	private ArrayList<Class<?>> parameterTypes;
		
	public ActionMethod (String name, Class<?>[] classes){
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
	
	public boolean isCompatibleTo(String methodName, Class<?>[] parameters){
		if (!this.name.equals(methodName) || parameters.length != this.parameterTypes.size()){
			//method name differs
			return false;
		}
		for (int i=0; i < parameters.length; i++){
			if (!PrimitivTypeComparator.sameType(parameters[i], this.parameterTypes.get(i))){
				return false;
			}
		}
		return true;
	}

}
