package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ActuatorAdapterMethod {
	private String name;
	private ArrayList<Class<?>> parameterTypes;
	
	private static HashMap<Class<?>,Class<?>> primitivClassMapping;
	
	public ActuatorAdapterMethod (String name, Class<?>[] classes){
		this.name = name;
		this.parameterTypes = new ArrayList<Class<?>>(Arrays.asList(classes));
		
		if (primitivClassMapping == null){
			primitivClassMapping = new HashMap<Class<?>, Class<?>>();
			primitivClassMapping.put(Double.class, double.class);
			primitivClassMapping.put(Float.class, float.class);
			primitivClassMapping.put(Long.class, long.class);
			primitivClassMapping.put(Integer.class, int.class);
			primitivClassMapping.put(Short.class, short.class);
			primitivClassMapping.put(Byte.class, byte.class);
			primitivClassMapping.put(Character.class, char.class);
			primitivClassMapping.put(Boolean.class, boolean.class);
		
		}
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
		for (Object param : parameters){
			if (!types.remove(param)){
				//parameter not found -> check primitiv type
				Class<?> prim = primitivClassMapping.get(param);
				if (prim != null){
					if (!types.remove(prim)){
						return false;
					}
				}
			}
		}
		if (types.isEmpty()){
			//all parameters used
			return true;
		}
		return false;
	}

}
