package de.uniol.inf.is.odysseus.iql.basic.lookup;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;


public interface IIQLLookUp {
	
	Collection<JvmField> getPublicAttributes(JvmTypeReference typeRef, Collection<JvmTypeReference> importedTypes, boolean extensionAttributes);
	Collection<JvmField> getProtectedAttributes(JvmTypeReference typeRef,Collection<JvmTypeReference> importedTypes, boolean extensionAttributes);
	Collection<JvmField> getPublicAttributes(JvmTypeReference typeRef, boolean extensionAttributes);
	Collection<JvmField> getProtectedAttributes(JvmTypeReference typeRef, boolean extensionAttributes);

	Collection<JvmOperation> getPublicMethods(JvmTypeReference typeRef,Collection<JvmTypeReference> importedTypes,boolean extensionMethods);	
	Collection<JvmOperation> getProtectedMethods(JvmTypeReference typeRef,Collection<JvmTypeReference> importedTypes, boolean extensionMethods);		
	Collection<JvmOperation> getPublicMethods(JvmTypeReference typeRef, boolean extensionMethods);	
	Collection<JvmOperation> getProtectedMethods(JvmTypeReference typeRef, boolean extensionMethods);		

	Collection<JvmExecutable> getConstructors(JvmTypeReference typeRef);
	JvmExecutable findConstructor(JvmTypeReference typeRef, List<IQLExpression> arguments);

	Collection<JvmType> getAllTypes(Collection<String> usedNamespaces, Resource context);	
	Collection<JvmType> getAllInstantiateableTypes(Collection<String> usedNamespaces, Resource context);	
	Collection<JvmType> getAllAssignableTypes(JvmTypeReference target, Collection<String> usedNamespaces, Resource context);

	Collection<String> getAllNamespaces();

	
	boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef);	
	
	Map<String, JvmTypeReference> getProperties(JvmTypeReference typeRef);
	boolean isInstantiateable(JvmDeclaredType declaredType);


}
