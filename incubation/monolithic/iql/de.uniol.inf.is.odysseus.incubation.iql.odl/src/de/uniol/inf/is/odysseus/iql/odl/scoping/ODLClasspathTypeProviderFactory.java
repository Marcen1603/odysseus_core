package de.uniol.inf.is.odysseus.iql.odl.scoping;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;
import org.eclipse.xtext.common.types.access.impl.TypeResourceServices;

@SuppressWarnings("restriction")
public class ODLClasspathTypeProviderFactory extends ClasspathTypeProviderFactory {

	
	@Inject
	public ODLClasspathTypeProviderFactory(ClassLoader classLoader, TypeResourceServices services) {
		super(ODLClasspathTypeProviderFactory.class.getClassLoader(), services);
	}
}
