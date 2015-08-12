package de.uniol.inf.is.odysseus.iql.basic.scoping;


import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;

@SuppressWarnings("restriction")
public class IQLClasspathTypeProviderFactory extends ClasspathTypeProviderFactory {
		
	@Inject
	private IIQLJdtTypeProviderFactory jdtTypeProviderFactory;
	
	@Inject
	public IQLClasspathTypeProviderFactory(ClassLoader classLoader) {
		super(IQLClasspathTypeProviderFactory.class.getClassLoader());
	}
	
	@Override
	protected ClasspathTypeProvider createClasspathTypeProvider(ResourceSet resourceSet) {
		return new IQLClasspathTypeProvider(jdtTypeProviderFactory.createJdtTypeProvider(resourceSet), IQLClasspathTypeProviderFactory.class.getClassLoader(), resourceSet, getIndexedJvmTypeAccess());
	}
	
}
