package de.uniol.inf.is.odysseus.rcp.editor.model;

import org.eclipse.gef.requests.CreationFactory;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class OperatorFactory implements CreationFactory {

	private final String operatorBuilderName;
	
	public OperatorFactory(String operatorBuilderName) {
		this.operatorBuilderName = operatorBuilderName;
	}
	
	@Override
	public Object getNewObject() {
		Operator op = new Operator( OperatorBuilderFactory.createOperatorBuilder(operatorBuilderName), operatorBuilderName);
		return op;
	}

	@Override
	public Object getObjectType() {
		return Operator.class;
	}

	
}
