package de.uniol.inf.is.odysseus.iql.basic.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmVoid;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;
import org.eclipse.xtext.common.types.access.impl.IndexedJvmTypeAccess;

@SuppressWarnings("restriction")
public class IQLClasspathTypeProvider extends ClasspathTypeProvider{
	
	private IIQLJdtTypeProvider jdtTypeProvider;
	private IQLQualifiedNameConverter converter;
	
	public IQLClasspathTypeProvider(IIQLJdtTypeProvider jdtTypeProvider, ClassLoader classLoader,ResourceSet resourceSet, IQLQualifiedNameConverter converter, IndexedJvmTypeAccess indexedJvmTypeAccess) {
		super(classLoader, resourceSet, indexedJvmTypeAccess);
		this.jdtTypeProvider = jdtTypeProvider;
		this.converter = converter;
	}

	
	public IIQLJdtTypeProvider getJdtTypeProvider() {
		return jdtTypeProvider;
	}
	
	@Override
	public JvmType findTypeByName(String name) {
		JvmType type = null;
		if (jdtTypeProvider!= null) {
			try {
				type = jdtTypeProvider.findTypeByName(converter.toJavaString(name));
			}catch (Exception e) {
				
			}
		}
		if (type != null) {
			return type;
		} else {
			type = super.findTypeByName(name);
		}
		if (type instanceof JvmVoid) {
			System.out.println();
		}
		return type;
	}
	
	@Override
	protected JvmType tryFindTypeInIndex(String name, boolean binaryNestedTypeDelimiter) {
		return null;
	}

}
