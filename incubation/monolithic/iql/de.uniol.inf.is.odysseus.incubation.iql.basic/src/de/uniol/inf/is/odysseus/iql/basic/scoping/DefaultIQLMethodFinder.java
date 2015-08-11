package de.uniol.inf.is.odysseus.iql.basic.scoping;

import java.util.Collection;
import java.util.List;


import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmOperation;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;

public class DefaultIQLMethodFinder implements IIQLMethodFinder {
	
	
	@Override
	public String createExecutableID(JvmExecutable exe){
		return exe.getSimpleName()+"_"+exe.getParameters().size();
	}	

	@Override
	public boolean isMethod(JvmOperation op, String name, List<IQLExpression> arguments) {
		return op.getSimpleName().equalsIgnoreCase(name) && op.getParameters().size() == arguments.size();
	}
	

	@Override
	public JvmExecutable findConstructor(Collection<JvmExecutable> constructors, List<IQLExpression> arguments) {
		for (JvmExecutable exe : constructors) {
			if (exe.getParameters().size() == arguments.size()){
				return exe;
			}
		}		
		return null;
	}

	@Override
	public JvmOperation findMethod(Collection<JvmOperation> methods,String name, List<IQLExpression> arguments) {
		for (JvmOperation op : methods) {
			if (op.getSimpleName().equalsIgnoreCase(name) && op.getParameters().size() == arguments.size()) {
				return op;
			}
		}
		return null;
	}

	@Override
	public JvmOperation findMethod(Collection<JvmOperation> methods, String name) {
		for (JvmOperation op : methods) {
			if (op.getSimpleName().equalsIgnoreCase(name) && op.getParameters().size() == 0) {
				return op;
			}
		}
		return null;
	}
	

	@Override
	public JvmOperation findMethod(Collection<JvmOperation> methods, String name, int args) {
		for (JvmOperation op : methods) {
			if (op.getSimpleName().equalsIgnoreCase(name) && op.getParameters().size() == args) {
				return op;
			}
		}
		return null;
	}

}
