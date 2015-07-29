package de.uniol.inf.is.odysseus.iql.qdl.services;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class QDLServiceBinding {
	
	private static IOperatorBuilderFactory operatorBuilderFactory;
	
	private static IPQLGenerator pqlGenerator;
	
	private static IExecutor executor;

	private static Set<IPreParserKeywordProvider> preParserKeywordProviders = new HashSet<>();

	public static void bindPreParserKeywordProvider(IPreParserKeywordProvider provider) {
		preParserKeywordProviders.add(provider);
	}
	
	public static void unbindPreParserKeywordProvider(IPreParserKeywordProvider provider) {
		preParserKeywordProviders.remove(provider);
	}
	
	public static Set<IPreParserKeywordProvider> getPreParserKeywordProviders() {
		return preParserKeywordProviders;
	}
	
	
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
