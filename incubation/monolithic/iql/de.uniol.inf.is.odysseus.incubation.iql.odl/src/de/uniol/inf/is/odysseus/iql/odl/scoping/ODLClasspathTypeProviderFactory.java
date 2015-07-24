package de.uniol.inf.is.odysseus.iql.odl.scoping;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;

@SuppressWarnings("restriction")
public class ODLClasspathTypeProviderFactory extends ClasspathTypeProviderFactory {

	
	@Inject
	public ODLClasspathTypeProviderFactory(ClassLoader classLoader) {
		super(ODLClasspathTypeProviderFactory.class.getClassLoader());
	}
}
