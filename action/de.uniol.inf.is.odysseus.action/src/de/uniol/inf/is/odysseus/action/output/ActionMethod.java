package de.uniol.inf.is.odysseus.action.output;

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
	
	@SuppressWarnings("unchecked")
	public boolean isCompatibleTo(String methodName, Class<?>[] parameters){
		ArrayList<Class<?>> types = (ArrayList<Class<?>>) this.parameterTypes.clone();
		if (!this.name.equals(methodName)){
			//method name differs
			return false;
		}
		for (Class<?> param : parameters){
			if (!types.remove(param)){
				//parameter not found -> check wrapper types
				Class<?> typeToRemove = null;
				for (Class<?> type : types){
					if (PrimitivTypeComparator.sameType(param, type)){
						typeToRemove = type;
						break;
					}
				}
				types.remove(typeToRemove);
			}
		}
		if (types.isEmpty()){
			//all parameters used
			return true;
		}
		return false;
	}

}
