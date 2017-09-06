package de.uniol.inf.is.odysseus.core.util;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

public class CollectOperatorLogicalGraphVisitor<T extends ILogicalOperator>
		implements IGraphNodeVisitor<T, Set<T>> {

	final private Set<T> operators;
	final private Set<Class<? extends T>> operatorClasses;
	final boolean checkAssigneable;

	public CollectOperatorLogicalGraphVisitor(Class<? extends T> operatorClass) {
		this(operatorClass, false);
	}

	public CollectOperatorLogicalGraphVisitor(Class<? extends T> operatorClass,
			boolean checkAssignable) {
		this.operatorClasses = new HashSet<Class<? extends T>>();
		this.operatorClasses.add(operatorClass);
		this.operators = new HashSet<T>();
		this.checkAssigneable = checkAssignable;
	}

	public CollectOperatorLogicalGraphVisitor(
			Set<Class<? extends T>> operatorClasses, boolean checkAssigneable) {
		this.operatorClasses = operatorClasses;
		this.operators = new HashSet<T>();
		this.checkAssigneable = checkAssigneable;
	}

	@Override
	public void nodeAction(T node) {
		if (checkAssigneable) {
			for (Class<? extends T> operator : operatorClasses) {
				if (operator.isAssignableFrom(node.getClass())) {
					operators.add(node);
				}
			}
		} else {
			if (operatorClasses.contains(node.getClass())){
				operators.add(node);
			}
		}
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<T> getResult() {
		return this.operators;
	}

}
