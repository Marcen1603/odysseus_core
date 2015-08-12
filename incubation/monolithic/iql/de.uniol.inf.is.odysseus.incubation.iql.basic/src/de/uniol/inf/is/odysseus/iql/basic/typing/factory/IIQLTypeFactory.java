package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;




public interface IIQLTypeFactory {
	
	String getImportName(JvmType type);	
	String getSimpleName(JvmType type,  String text, boolean wrapper, boolean array);
		
	Collection<Bundle> getDependencies();
	
	String getFileExtension();
	boolean isSystemFile(String fileName);
	IQLModel getSystemFile(String fileName);
	Collection<IQLModel> getSystemFiles();	
	ResourceSet getSystemResourceSet();
	
	Collection<JvmType> getVisibleTypes(Collection<String> usedNamespaces, Resource context);
	Collection<String> getImplicitImports();

	IQLSystemType addSystemType(JvmGenericType type, Class<?> javaType);
	void removeSystemType(String name);
	
	IQLSystemType getSystemType(String name);
	boolean isSystemType(String name);
	
	Collection<JvmTypeReference> getImportedTypes(EObject obj);
	
	Collection<String> getJavaPackages();
	
	boolean isImportNeeded(JvmType type, String text);
}
