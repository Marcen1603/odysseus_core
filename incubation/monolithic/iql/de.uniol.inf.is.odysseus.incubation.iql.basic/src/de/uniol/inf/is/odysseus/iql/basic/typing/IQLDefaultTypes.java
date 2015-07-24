package de.uniol.inf.is.odysseus.iql.basic.typing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class IQLDefaultTypes {
	
	
	public static Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(Object.class);
		types.add(String.class);
		types.add(List.class);
		types.add(ArrayList.class);
		types.add(Map.class);
		types.add(HashMap.class);
		return types;
	}
	
	public static Collection<String> getImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		implicitImports.add("java.util.*");
		implicitImports.add("java.lang.*");
		return implicitImports;
	}

}
