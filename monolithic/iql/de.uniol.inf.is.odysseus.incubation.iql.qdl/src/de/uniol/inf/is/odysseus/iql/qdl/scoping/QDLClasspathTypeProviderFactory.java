package de.uniol.inf.is.odysseus.iql.qdl.scoping;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;
import org.eclipse.xtext.common.types.access.impl.TypeResourceServices;

@SuppressWarnings("restriction")
public class QDLClasspathTypeProviderFactory extends ClasspathTypeProviderFactory {

	
	@Inject
	public QDLClasspathTypeProviderFactory(ClassLoader classLoader, TypeResourceServices services) {
		super(QDLClasspathTypeProviderFactory.class.getClassLoader(), services);
	}
}
