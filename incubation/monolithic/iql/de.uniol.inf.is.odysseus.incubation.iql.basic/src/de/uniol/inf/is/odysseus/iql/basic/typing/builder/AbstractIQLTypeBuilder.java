package de.uniol.inf.is.odysseus.iql.basic.typing.builder;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;
import org.eclipse.xtext.common.types.util.TypeReferences;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;



@SuppressWarnings("restriction")
public abstract class AbstractIQLTypeBuilder<T extends IIQLTypeFactory, U extends IIQLTypeUtils> implements IIQLTypeBuilder {

	@Inject
	protected IJvmTypeProvider.Factory typeProviderFactory;
	
	@Inject
	protected TypeReferences typeReferences;
	
	protected T typeFactory;
	protected U typeUtils;

	public AbstractIQLTypeBuilder(T typeFactory, U typeUtils) {
		this.typeFactory = typeFactory;
		this.typeUtils = typeUtils;
	}	
	
	
	public IQLSystemType createOrGetSystemType(Class<?> javaType) {
		return createSystemType(javaType.getPackage().getName(), javaType.getSimpleName(), javaType);
	}

	public IQLSystemType createOrGetSystemType(String[] namespaceSegments, String simpleName, Class<?> javaType ) {
		return createSystemType(createQualifiedName(namespaceSegments), simpleName, javaType);
	}
	
	private IQLSystemType createSystemType(String packageName, String simpleName, Class<?> javaType) {
		JvmGenericType type = null;
		if (javaType.isInterface()) {
			type = createInterfaceType(packageName, simpleName, javaType);
		} else {
			type = createClassType(packageName, simpleName, javaType);
		}
		return new IQLSystemType(type, javaType);
	}
	
	private IQLInterface createInterfaceType(String packageName, String simpleName, Class<?> javaType) {
		IQLInterface iqlInterface = BasicIQLFactory.eINSTANCE.createIQLInterface();
		iqlInterface.setPackageName(packageName);
		iqlInterface.setSimpleName(simpleName);
		return iqlInterface;		
	}
	
	private IQLClass createClassType(String packageName, String simpleName, Class<?> javaType) {
		IQLClass iqlClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		iqlClass.setPackageName(packageName);
		iqlClass.setSimpleName(simpleName);
		return iqlClass;		
	}
	
	public void removeSystemType(String[] namespaceSegments, String simpleName) {
		typeFactory.removeSystemType(createQualifiedName(namespaceSegments, simpleName));
	}
	
	protected String createQualifiedName(String[] namespaceSegments) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i< namespaceSegments.length; i++) {
			if (i > 0) {
				b.append(".");
			}
			b.append(namespaceSegments[i]);
		}
		return b.toString();
	}
	
	protected String createQualifiedName(String[] namespaceSegments, String simpleName) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i< namespaceSegments.length; i++) {
			if (i > 0) {
				b.append(".");
			}
			b.append(namespaceSegments[i]);
		}
		if (namespaceSegments.length > 0) {
			b.append(".");
		}
		b.append(simpleName);
		return b.toString();
	}
	
	
	protected String createClassName(String packageName, String simpeName) {
		return packageName + "." + simpeName;
	}
	
	protected String firstCharUpperCase(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	protected String firstCharLowerCase(String s) {
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	
	protected ClasspathTypeProvider getTypeProvider() {
		IJvmTypeProvider p = typeProviderFactory.findOrCreateTypeProvider(typeFactory.getSystemResourceSet());
		return (ClasspathTypeProvider) p;
	}
	
}
