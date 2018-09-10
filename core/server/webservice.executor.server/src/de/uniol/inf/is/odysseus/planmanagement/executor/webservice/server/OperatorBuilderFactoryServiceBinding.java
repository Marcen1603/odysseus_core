package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;

public class OperatorBuilderFactoryServiceBinding {

	private static IOperatorBuilderFactory instance;
	
	public void bindOperatorBuilderFactory(IOperatorBuilderFactory f) {
		instance = f;
	}
	
	public void unbindOperatorBuilderFactory(IOperatorBuilderFactory f) {
		instance = null;
	}
	
	public static IOperatorBuilderFactory getOperatorBuilderFactory() {
		return instance;
	}
}
