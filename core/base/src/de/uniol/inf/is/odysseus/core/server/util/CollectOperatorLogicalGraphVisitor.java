package de.uniol.inf.is.odysseus.core.server.util;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

public class CollectOperatorLogicalGraphVisitor<T extends ILogicalOperator> implements
		IGraphNodeVisitor<T, Set<T>> {

	private Set<T> operators;
	private Set<Class<? extends T>> operatorClasses;
	
	public CollectOperatorLogicalGraphVisitor(Class<? extends T> operatorClass) {
		this.operatorClasses = new HashSet<Class<? extends T>>();
		this.operatorClasses.add(operatorClass);
		this.operators = new HashSet<T>();
	}
	
	public CollectOperatorLogicalGraphVisitor(Set<Class<? extends T>> operatorClasses) {
		this.operatorClasses = operatorClasses;
		this.operators = new HashSet<T>();
	}
	
	@Override
	public void nodeAction(T node) {
		if(this.operatorClasses.contains(node.getClass())) {
			operators.add(node);
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
