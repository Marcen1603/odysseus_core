package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;

import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;


public interface IIQLService {
	Collection<String> getDependencies();
	Collection<Class<?>> getVisibleTypes();
	Collection<String> getImplicitImports();
	Collection<IIQLTypeExtensions> getTypeExtensions();
	Collection<Class<?>> getImplicitStaticImports();
}
