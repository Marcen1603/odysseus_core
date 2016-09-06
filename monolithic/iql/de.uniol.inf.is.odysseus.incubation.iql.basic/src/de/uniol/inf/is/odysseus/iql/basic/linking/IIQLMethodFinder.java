package de.uniol.inf.is.odysseus.iql.basic.linking;

import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmOperation;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;

public interface IIQLMethodFinder {
	String createExecutableID(JvmExecutable exe);

	JvmOperation findMethod(Collection<JvmOperation> methods, String name,List<IQLExpression> arguments);
	JvmOperation findMethod(Collection<JvmOperation> methods, String name);	
	JvmExecutable findConstructor(Collection<JvmExecutable> constructors, List<IQLExpression> arguments);

	JvmOperation findMethod(Collection<JvmOperation> methods, String name,int args, boolean hasReturnType);

}
