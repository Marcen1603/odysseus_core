package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;


public interface IIQLService {
	Collection<Bundle> getDependencies();
	Collection<String> getImplicitImports();
	Collection<Class<?>> getImplicitStaticImports();
	Collection<IIQLTypeExtensions> getTypeExtensions();

	Collection<Class<?>> getVisibleTypes();	
	Collection<Bundle> getVisibleTypesFromBundle();	
	
	Collection<ParameterPair> getParameters();	

}
