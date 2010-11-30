package de.uniol.inf.is.odysseus.rcp.editor.model;

import org.eclipse.gef.requests.CreationFactory;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;

public class OperatorFactory implements CreationFactory {

	private final String operatorBuilderName;
	
	public OperatorFactory(String operatorBuilderName) {
		this.operatorBuilderName = operatorBuilderName;
	}
	
	@Override
	public Object getNewObject() {
		final User user = ActiveUser.getActiveUser();
		Operator op = new Operator( OperatorBuilderFactory.createOperatorBuilder(operatorBuilderName, user), operatorBuilderName);
		return op;
	}

	@Override
	public Object getObjectType() {
		return Operator.class;
	}

	
}
