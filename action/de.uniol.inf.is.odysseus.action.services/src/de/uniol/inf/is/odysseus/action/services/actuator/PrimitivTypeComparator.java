package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.HashMap;

/**
 * Helper which checks if a class is the primitiv wrapper of the other one
 * @author Simon Flandergan
 *
 */
public class PrimitivTypeComparator {
	
private static HashMap<Class<?>,Class<?>> primitivClassMapping;

	public static boolean sameType (Class<?>c1, Class<?> c2){
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
		
		if (c1 == c2){
			return true;
		}else if (c1 == null || c2 == null){
			return false;	
		}else if (primitivClassMapping.get(c1) == c2 || primitivClassMapping.get(c2) == c1){
			return true;
		}
		return false;
	}
	
}
