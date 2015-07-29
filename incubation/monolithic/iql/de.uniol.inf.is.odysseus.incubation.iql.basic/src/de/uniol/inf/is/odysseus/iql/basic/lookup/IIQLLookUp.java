package de.uniol.inf.is.odysseus.iql.basic.lookup;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;


public interface IIQLLookUp {
	
	Collection<JvmField> getPublicAttributes(JvmTypeReference typeRef, boolean extensionAttributes);
	Collection<JvmField> getProtectedAttributes(JvmTypeReference typeRef, boolean extensionAttributes);

	Collection<JvmOperation> getPublicMethods(JvmTypeReference typeRef, boolean extensionMethods);	
	Collection<JvmOperation> getProtectedMethods(JvmTypeReference typeRef, boolean extensionMethods);		
	
	Collection<JvmExecutable> getPublicConstructors(JvmTypeReference typeRef);
	Collection<JvmExecutable> getDeclaredConstructors(JvmTypeReference typeRef);
	JvmExecutable findConstructor(JvmTypeReference typeRef,	int arguments);
	
	Collection<JvmType> getAllTypes(Resource context);	
	Collection<JvmType> getAllInstantiateableTypes(Resource context);	
	Collection<JvmType> getAllAssignableTypes(JvmTypeReference target,Resource context);

	
	boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef);	
	
	Map<String, JvmTypeReference> getProperties(JvmTypeReference typeRef);


}
