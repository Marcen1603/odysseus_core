package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;
import org.eclipse.xtext.common.types.access.impl.IndexedJvmTypeAccess;

@SuppressWarnings("restriction")
public class IQLClasspathTypeProvider extends ClasspathTypeProvider{
	
	private IIQLJdtTypeProvider jdtTypeProvider;
	
	public IQLClasspathTypeProvider(IIQLJdtTypeProvider jdtTypeProvider, ClassLoader classLoader,ResourceSet resourceSet, IndexedJvmTypeAccess indexedJvmTypeAccess) {
		super(classLoader, resourceSet, indexedJvmTypeAccess);
		this.jdtTypeProvider = jdtTypeProvider;
	}

	
	public IIQLJdtTypeProvider getJdtTypeProvider() {
		return jdtTypeProvider;
	}
	
	@Override
	public JvmType findTypeByName(String name) {
		JvmType type = jdtTypeProvider.findTypeByName(name);
		if (type != null) {
			return type;
		} else {
			return super.findTypeByName(name);
		}
	}

}
