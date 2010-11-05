package de.uniol.inf.is.odysseus.broker.evaluation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.broker.benchmarker.BenchmarkService;
import de.uniol.inf.is.odysseus.broker.evaluation.pql.CycleBuilder;
import de.uniol.inf.is.odysseus.broker.evaluation.pql.FreqCycleBuilder;
import de.uniol.inf.is.odysseus.broker.evaluation.rules.TBenchmarkAORule;
import de.uniol.inf.is.odysseus.broker.evaluation.rules.TBrokerAORule;
import de.uniol.inf.is.odysseus.broker.evaluation.rules.TBrokerCycleDetectionRule;
import de.uniol.inf.is.odysseus.broker.evaluation.rules.TBufferAORule;
import de.uniol.inf.is.odysseus.broker.evaluation.rules.TUpdateEvaluationAORule;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static BenchmarkService bs;
	
	public static BenchmarkService getBenchmarkService(){
		return bs;
	}

	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		//loading transform
		
		
		//loading PQL
//		OperatorBuilderFactory.putOperatorBuilderType("BROKER", BrokerAOBuilder.class);
//		OperatorBuilderFactory.putOperatorBuilderType("UPEVAL", UpdateEvaluationAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType("CYCLE", CycleBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType("FREQCYCLE", FreqCycleBuilder.class);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;		
	}
	


	public void bindBS(BenchmarkService service) {
		bs = service;
		System.out.println("benchmark bound!!");
	}

	public void unbindBS(BenchmarkService bs) {
		bs = null;
	}

}
