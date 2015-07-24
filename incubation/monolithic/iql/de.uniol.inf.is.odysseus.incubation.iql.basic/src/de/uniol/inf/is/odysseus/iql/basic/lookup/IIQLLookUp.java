package de.uniol.inf.is.odysseus.iql.basic.lookup;

import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsList;


public interface IIQLLookUp {

	Collection<JvmType> getAllTypes(Resource context);

	Collection<JvmOperation> getPublicMethods(JvmTypeReference typeRef);
	
	Collection<JvmOperation> getDeclaredPublicMethods(JvmTypeReference typeRef);

	Collection<JvmField> getPublicAttributes(JvmTypeReference typeRef);
	
	Collection<JvmField> getDeclaredPublicAttributes(JvmTypeReference typeRef);


	boolean isAssignable(JvmTypeReference targetRef, JvmTypeReference typeRef);
	
	JvmOperation findMethod(JvmTypeReference type, String methodName, IQLArgumentsList arguments);

	JvmExecutable findConstructor(JvmTypeReference typeRef,	IQLArgumentsList arguments);

	Collection<JvmExecutable> getPublicConstructors(JvmTypeReference typeRef);

	Collection<JvmExecutable> getDeclaredConstructors(JvmTypeReference typeRef);

	Collection<JvmType> getAllAssignableTypes(JvmTypeReference target,
			Resource context);

	String createExecutableID(JvmExecutable exe);

	Collection<JvmType> getAllInstantiateableTypes(Resource context);

}
