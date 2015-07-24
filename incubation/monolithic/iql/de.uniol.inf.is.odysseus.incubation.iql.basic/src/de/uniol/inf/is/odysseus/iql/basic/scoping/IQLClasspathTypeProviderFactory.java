package de.uniol.inf.is.odysseus.iql.basic.scoping;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;

@SuppressWarnings("restriction")
public class IQLClasspathTypeProviderFactory extends ClasspathTypeProviderFactory {

	
	@Inject
	public IQLClasspathTypeProviderFactory(ClassLoader classLoader) {
		super(IQLClasspathTypeProviderFactory.class.getClassLoader());
	}
}
