package de.uniol.inf.is.odysseus.costmodel.operator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.monitoring.physicalplan.PlanMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class OperatorCostModelPlugin implements BundleActivator, IPostOptimizationAction {

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(OperatorCostModelPlugin.class);
		}
		return _logger;
	}

	public static final int MONITORING_PERIOD_SECONDS = 20;
	public static final int MONITORING_PERIOD_MILLIS = MONITORING_PERIOD_SECONDS * 1000;
	public static final String DATARATE_TYPE = "datarate";

	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	public void bindOperatorEstimator(IOperatorEstimator<?> estimator) {
		OperatorEstimatorFactory.getInstance().register(estimator);

		getLogger().debug("Bound OperatorEstimator " + estimator.getClass().getSimpleName() + " for Operator " + estimator.getOperatorClass().getSimpleName());
	}

	public void unbindOperatorEstimator(IOperatorEstimator<?> estimator) {
		OperatorEstimatorFactory.getInstance().unregister(estimator);

		getLogger().debug("Unbound OperatorEstimator " + estimator.getClass().getSimpleName() + " for Operator " + estimator.getOperatorClass().getSimpleName());
	}

	@Override
	public void run(IQuery query, OptimizationConfiguration parameter) {
		query.addPlanMonitor(DATARATE_TYPE, 
				new PlanMonitor(query, false, false, DATARATE_TYPE, MONITORING_PERIOD_MILLIS));
		query.addPlanMonitor(MonitoringDataTypes.SELECTIVITY.name, 
				new PlanMonitor(query, false, false, MonitoringDataTypes.SELECTIVITY.name, MONITORING_PERIOD_MILLIS));
		
		// own metadata
		for ( IPhysicalOperator operator : query.getPhysicalChilds() ) {
			if( operator instanceof ISink) {
				if( !operator.getProvidedMonitoringData().contains(AvgCPUTime.METADATA_TYPE_NAME))
					operator.addMonitoringData(AvgCPUTime.METADATA_TYPE_NAME, new AvgCPUTime(operator) );
			}
		}
//		
//		// place latency
//		IPhysicalOperator root = query.getRoots().get(0);
//		if (root instanceof ISource) {
//			LatencyCalculationPipe<?> latencyCalc = new LatencyCalculationPipe();
//			((ISource) root).connectSink(latencyCalc, 0, 0, root.getOutputSchema());
//		} else {
//			throw new RuntimeException(
//					"Cannot connect SLA conformance operator to query root: " + root);
//		}

	}

}
