package de.uniol.inf.is.odysseus.iql.basic.typing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.iql.basic.types.Range;
import de.uniol.inf.is.odysseus.iql.basic.types.extension.CollectionExtensions;
import de.uniol.inf.is.odysseus.iql.basic.types.extension.ListExtensions;
import de.uniol.inf.is.odysseus.iql.basic.types.extension.ObjectExtensions;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

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
		types.add(Map.Entry.class);
		return types;
	}

	public static Collection<String> getImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		implicitImports.add("java.util.*");
		implicitImports.add("java.lang.*");
		implicitImports.add("java.lang.System");
		implicitImports.add(Range.class.getCanonicalName());
		implicitImports.add(Map.Entry.class.getCanonicalName());
		return implicitImports;
	}

	public static Collection<IIQLTypeExtensions> getTypeExtensions() {
		Collection<IIQLTypeExtensions> result = new HashSet<>();
		result.add(new CollectionExtensions());
		result.add(new ListExtensions());
		result.add(new ObjectExtensions());
		return result;
	}

	public static Collection<String> getImplicitStaticImports() {
		Collection<String> types = new HashSet<>();
		types.add(System.class.getCanonicalName());
		types.add(Collections.class.getCanonicalName());
		types.add("iql.mep");
		return types;
	}
	
	public static Collection<String> getDependencies() {
		Collection<String> bundles = new HashSet<>();
		bundles.add("de.uniol.inf.is.odysseus.iql.basic");
		bundles.add("de.uniol.inf.is.odysseus.core");
		bundles.add("de.uniol.inf.is.odysseus.core.server");
		bundles.add("de.uniol.inf.is.odysseus.slf4j");	
		
		bundles.add("org.eclipse.osgi");
		bundles.add("io.netty");
		
		bundles.add("com.google.guava");
		bundles.add("com.rits.cloning");
		
		bundles.add("de.uniol.inf.is.odysseus.mep");	
		return bundles;
	}
	
	public static Collection<String> getVisibleTypesFromBundle() {
		Collection<String> bundles = new HashSet<>();
		bundles.add("de.uniol.inf.is.odysseus.core");
		bundles.add("de.uniol.inf.is.odysseus.core.server");
		return bundles;
	}

	public static Collection<String> getImportedPackages() {
		Collection<String> packages = new HashSet<>();
		packages.add("org.apache.commons.io");
		packages.add("org.apache.log4j");
		packages.add("org.simpleframework.xml");
		packages.add("org.simpleframework.xml.core");
		return packages;	
	}

}
