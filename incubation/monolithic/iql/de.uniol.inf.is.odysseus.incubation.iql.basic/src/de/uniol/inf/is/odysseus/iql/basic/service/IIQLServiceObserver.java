package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;

import org.osgi.framework.Bundle;

public interface IIQLServiceObserver {
	public Collection<Bundle> getDependencies();
	public Collection<Class<?>> getVisibleTypes();
	public Collection<String> getImplicitImports();

}
