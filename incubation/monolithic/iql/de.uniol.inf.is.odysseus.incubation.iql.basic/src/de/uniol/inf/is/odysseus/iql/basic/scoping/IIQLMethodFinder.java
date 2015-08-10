package de.uniol.inf.is.odysseus.iql.basic.scoping;

import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;

public interface IIQLMethodFinder {
	
	boolean isMethod(JvmOperation op, String name, List<IQLExpression> arguments);

	JvmOperation findMethod(Collection<JvmOperation> methods, String name,List<IQLExpression> arguments);	
	JvmOperation findMethod(Collection<JvmOperation> methods, String name);	
	JvmOperation findMethod(Collection<JvmOperation> methods, String name, int args);	

	JvmOperation findDeclaredMethod(JvmDeclaredType type, String name,List<IQLExpression> arguments);

	JvmOperation findDeclaredMethod(JvmTypeReference typeRef, String name, List<IQLExpression> arguments);

	
	JvmExecutable findConstructor(Collection<JvmExecutable> constructors, List<IQLExpression> arguments);


	String createExecutableID(JvmExecutable exe);

	JvmOperation findDeclaredMethod(JvmTypeReference typeRef, String name,int args);
	JvmOperation findDeclaredMethod(JvmDeclaredType type, String name,int args);

}
