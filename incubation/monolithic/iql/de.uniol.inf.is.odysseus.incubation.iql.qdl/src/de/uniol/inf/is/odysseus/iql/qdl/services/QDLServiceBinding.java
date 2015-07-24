package de.uniol.inf.is.odysseus.iql.qdl.services;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class QDLServiceBinding {
	
	private static IOperatorBuilderFactory operatorBuilderFactory;
	
	private static IPQLGenerator pqlGenerator;
	
	private static IExecutor executor;

	
	public static void bindExecutor(IExecutor exe) {
		executor = exe;
	}
	
	public static void unbindExecutor(IExecutor exe) {
		executor= exe;
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
	
	public static void bindPQLGenerator(IPQLGenerator generator) {
		pqlGenerator = generator;
	}
	
	public static void unbindPQLGenerator(IPQLGenerator generator) {
		pqlGenerator= generator;
	}
	
	public static IPQLGenerator getPQLGenerator() {
		return pqlGenerator;
	}

	public static void bindOperatorBuilderFactory(IOperatorBuilderFactory factory) {
		operatorBuilderFactory= factory;
	}
	
	public static void unbindOperatorBuilderFactory(IOperatorBuilderFactory factory) {
		operatorBuilderFactory= factory;
	}
	
	public static IOperatorBuilderFactory getOperatorBuilderFactory() {
		return operatorBuilderFactory;
	}
}
