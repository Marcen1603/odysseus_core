package de.uniol.inf.is.odysseus.iql.qdl.service;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.iql.basic.service.IQLServiceBinding;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class QDLServiceBinding extends IQLServiceBinding {	
	
	private static IOperatorBuilderFactory operatorBuilderFactory;
	
	private static IPQLGenerator pqlGenerator;
	
	private static IExecutor executor;

	private static Set<IPreParserKeywordProvider> preParserKeywordProviders = new HashSet<>();

	private static QDLServiceBinding instance = new QDLServiceBinding();
	
	public static QDLServiceBinding getInstance() {
		return instance;
	}

	public static void bindQDLService(IQDLService service) {
		getInstance().onIQLServiceAdded(service);
	}
	
	public static void unbindQDLService(IQDLService service) {
		getInstance().onIQLServiceRemoved(service);
	}
	
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
