package de.uniol.inf.is.odysseus.iql.qdl.typing;

import java.util.Collection;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class QDLDefaultTypes {
	
	
	public static Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(Option.class);
		return types;
	}
	
	public static Collection<String> getImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		implicitImports.add(Option.class.getCanonicalName());

		return implicitImports;
	}

	public static Collection<Class<?>> getImplicitStaticImports() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(System.class);
		return types;
	}

	public static Collection<IIQLTypeExtensions> getTypeOperators() {
		Collection<IIQLTypeExtensions> result = new HashSet<>();
		return result;		
	}
	
	public static Collection<String> getDependencies() {
		Collection<String> bundles = new HashSet<>();
		bundles.add("de.uniol.inf.is.odysseus.iql.qdl");
		return bundles;
	}
	
	public static Collection<String> getVisibleTypesFromBundle() {
		Collection<String> bundles = new HashSet<>();
		return bundles;
	}

	public static Collection<String>  getImportedPackages() {
		Collection<String> packages = new HashSet<>();
		return packages;
	}

}
