package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;
import java.util.Map;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;

public interface IIQLServiceObserver {
	public Collection<Bundle> getDependencies();
	public Collection<Class<?>> getVisibleTypes();
	public Collection<String> getImplicitImports();
	public Collection<Class<?>> getImplicitStaticImports();
	public Collection<Bundle> getVisibleTypesFromBundle();
	public Map<Class<? extends IParameter<?>>, Class<?>> getParameters();

}
