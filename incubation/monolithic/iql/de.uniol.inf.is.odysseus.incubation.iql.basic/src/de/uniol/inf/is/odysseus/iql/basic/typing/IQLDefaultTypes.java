package de.uniol.inf.is.odysseus.iql.basic.typing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.CollectionExtensions;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ListExtensions;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.impl.ObjectExtensions;

public class IQLDefaultTypes {
	
	
	public static Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(Object.class);
		types.add(String.class);
		types.add(List.class);
		types.add(ArrayList.class);
		types.add(Map.class);
		types.add(HashMap.class);
		types.add(Iterator.class);
		types.add(Range.class);

		return types;
	}
	
	public static Collection<String> getImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		implicitImports.add("java.util.*");
		implicitImports.add("java.lang.*");
		implicitImports.add("java.lang.System");
		implicitImports.add(Range.class.getCanonicalName());

		return implicitImports;
	}
	
	public static Collection<IIQLTypeExtensions> getTypeExtensions() {
		Collection<IIQLTypeExtensions> result = new HashSet<>();
		result.add(new CollectionExtensions());
		result.add(new ListExtensions());
		result.add(new ObjectExtensions());
		return result;
	}

	public static Collection<Class<?>> getImplicitStaticImports() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(System.class);
		return types;
	}

}
