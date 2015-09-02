package de.uniol.inf.is.odysseus.iql.basic.lookup;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
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
	Collection<JvmOperation> getDeclaredPublicMethods(JvmTypeReference typeRef,	boolean onlyStatic);

	Collection<JvmExecutable> getPublicConstructors(JvmTypeReference typeRef);
	JvmExecutable findConstructor(JvmTypeReference typeRef, List<IQLExpression> arguments);
	JvmExecutable findPublicConstructor(JvmTypeReference typeRef, List<IQLExpression> arguments);

	Collection<String> getAllNamespaces();

	JvmTypeReference getThisType(EObject obj);	
	JvmTypeReference getSuperType(EObject obj);
	
	boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef);	
	boolean isCastable(JvmTypeReference targetRef, JvmTypeReference typeRef);	

	
	boolean isInstantiateable(JvmDeclaredType declaredType);
	Collection<JvmOperation> getPublicSetters(JvmTypeReference typeRef);

}
