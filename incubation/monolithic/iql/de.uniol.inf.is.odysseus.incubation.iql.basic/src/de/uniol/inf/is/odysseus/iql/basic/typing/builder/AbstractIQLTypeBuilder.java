package de.uniol.inf.is.odysseus.iql.basic.typing.builder;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;
import org.eclipse.xtext.common.types.util.TypeReferences;
















import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLFactory;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.types.compiler.IQLMEPSystemTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.types.compiler.MEP;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import de.uniol.inf.is.odysseus.mep.FunctionSignature;



@SuppressWarnings("restriction")
public abstract class AbstractIQLTypeBuilder<T extends IIQLTypeDictionary, U extends IIQLTypeUtils> implements IIQLTypeBuilder {

	@Inject
	protected IJvmTypeProvider.Factory typeProviderFactory;
	
	@Inject
	protected TypeReferences typeReferences;
	
	protected T typeDictionary;
	protected U typeUtils;

	public AbstractIQLTypeBuilder(T typeDictionary, U typeUtils) {
		this.typeDictionary = typeDictionary;
		this.typeUtils = typeUtils;
		createMepFuntions();
	}	
	
	private void createMepFuntions() {
		IQLClass mepIQLClass = BasicIQLFactory.eINSTANCE.createIQLClass();
		mepIQLClass.setPackageName("iql");
		mepIQLClass.setSimpleName("mep");
		
		for (FunctionSignature signature : de.uniol.inf.is.odysseus.mep.MEP.getFunctions()) {
			IQLMethod method = BasicIQLFactory.eINSTANCE.createIQLMethod();
			method.setVisibility(JvmVisibility.PUBLIC);
			method.setStatic(true);
			method.setSimpleName(signature.getSymbol().toLowerCase());
			
			for (int i = 0; i<signature.getParameters().size(); i++) {
				JvmFormalParameter parameter = TypesFactory.eINSTANCE.createJvmFormalParameter();
				parameter.setName("arg"+i);
				parameter.setParameterType(typeUtils.createTypeRef(String.class, typeDictionary.getSystemResourceSet()));
				method.getParameters().add(parameter);
			}			
			method.setReturnType(typeUtils.createTypeRef(Object.class, typeDictionary.getSystemResourceSet()));	
			mepIQLClass.getMembers().add(method);
		}
		
		IQLSystemType systemType = new IQLSystemType(mepIQLClass, MEP.class);
		typeDictionary.addSystemType(systemType, new IQLMEPSystemTypeCompiler());
	}
		
	public void removeSystemType(String[] namespaceSegments, String simpleName) {
		typeDictionary.removeSystemType(createQualifiedName(namespaceSegments, simpleName));
	}
	
	protected String createQualifiedName(String[] namespaceSegments) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i< namespaceSegments.length; i++) {
			if (i > 0) {
				b.append(IQLQualifiedNameConverter.DELIMITER);
			}
			b.append(namespaceSegments[i]);
		}
		return b.toString();
	}
	
	protected String createQualifiedName(String[] namespaceSegments, String simpleName) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i< namespaceSegments.length; i++) {
			if (i > 0) {
				b.append(IQLQualifiedNameConverter.DELIMITER);
			}
			b.append(namespaceSegments[i]);
		}
		if (namespaceSegments.length > 0) {
			b.append(IQLQualifiedNameConverter.DELIMITER);
		}
		b.append(simpleName);
		return b.toString();
	}
	
	
	protected String createClassName(String packageName, String simpeName) {
		return packageName + IQLQualifiedNameConverter.DELIMITER + simpeName;
	}
	
	protected String firstCharUpperCase(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	protected String firstCharLowerCase(String s) {
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	
	protected ClasspathTypeProvider getTypeProvider() {
		IJvmTypeProvider p = typeProviderFactory.findOrCreateTypeProvider(typeDictionary.getSystemResourceSet());
		return (ClasspathTypeProvider) p;
	}
	
}
