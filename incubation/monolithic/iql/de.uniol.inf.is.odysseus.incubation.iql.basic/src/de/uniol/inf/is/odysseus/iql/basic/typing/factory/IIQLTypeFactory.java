package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;




public interface IIQLTypeFactory {
		
	Collection<Bundle> getDependencies(IQLFile file);
	String getImportName(JvmType type);	
	String getSimpleName(JvmType type,  String text, boolean wrapper, boolean array);

	
	String getFileExtension();
	boolean isSystemFile(String fileName);
	IQLFile getSystemFile(String fileName);
	Collection<IQLFile> getSystemFiles();	
	ResourceSet getSystemResourceSet();
	
	Collection<JvmType> getVisibleTypes(Resource context);
	Collection<String> getImplicitImports();

	IQLSystemType addSystemType(JvmGenericType type, Class<?> javaType);
	void removeSystemType(String name);
	
	IQLSystemType getSystemType(String name);
	boolean isSystemType(String name);
	boolean isImportNeeded(JvmType type, String text);
	
	Collection<JvmTypeReference> getImportedTypes(EObject obj);
}
