package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;
import org.eclipse.xtext.common.types.access.impl.IndexedJvmTypeAccess;
import org.eclipse.xtext.common.types.access.jdt.IJdtTypeProvider;

@SuppressWarnings("restriction")
public class IQLClasspathTypeProvider extends ClasspathTypeProvider{

	private IJdtTypeProvider jdtTypeProvider;
	
	public IQLClasspathTypeProvider(IJdtTypeProvider jdtTypeProvider, ClassLoader classLoader,ResourceSet resourceSet, IndexedJvmTypeAccess indexedJvmTypeAccess) {
		super(classLoader, resourceSet, indexedJvmTypeAccess);
		this.jdtTypeProvider = jdtTypeProvider;
	}

	public IJdtTypeProvider getJdtTypeProvider() {
		return this.jdtTypeProvider;
	}

}
