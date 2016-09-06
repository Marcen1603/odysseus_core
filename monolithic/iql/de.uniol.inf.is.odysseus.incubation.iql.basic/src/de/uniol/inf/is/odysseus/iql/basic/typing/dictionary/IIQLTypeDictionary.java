package de.uniol.inf.is.odysseus.iql.basic.typing.dictionary;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;
import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLSystemTypeCompiler;




public interface IIQLTypeDictionary {
	

	Collection<JvmType> getVisibleTypes(Collection<String> usedNamespaces, Resource context);
	Collection<String> getImplicitImports();
	Collection<String> getImplicitStaticImports();
	
	String getImportName(JvmType type);	
	String getSimpleName(JvmType type,  String text, boolean wrapper, boolean array);
		
	Collection<Bundle> getRequiredBundles();
	
	String getFileExtension();
	boolean isSystemFile(String fileName);
	IQLModel getSystemFile(String fileName);
	Collection<IQLModel> getSystemFiles();	
	ResourceSet getSystemResourceSet();
			
	Collection<JvmTypeReference> getStaticImports(EObject obj);	
	Collection<String> getJavaPackages();	
	boolean isImportNeeded(JvmType type, String text);
	
	Class<?> getParameterValue(Class<? extends IParameter<?>> parameterType);
	Collection<Bundle> getVisibleTypesFromBundle();
	Collection<String> getImportedPackages();
	
	void addSystemType(IQLSystemType systemType);
	void addSystemType(IQLSystemType systemType, IIQLSystemTypeCompiler compiler);
	boolean hasSystemTypeCompiler(String name);
	IIQLSystemTypeCompiler getSystemTypeCompiler(String name);
	void removeSystemType(String name);	
	IQLSystemType getSystemType(String name);
	boolean isSystemType(String name);
	String[] getBundleClasspath(Bundle bundle);
}
