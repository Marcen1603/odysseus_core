package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;

@SuppressWarnings("restriction")
public class QDLClasspathTypeProviderFactory extends ClasspathTypeProviderFactory {

	
	@Inject
	public QDLClasspathTypeProviderFactory(ClassLoader classLoader) {
		super(QDLClasspathTypeProviderFactory.class.getClassLoader());
	}
}
