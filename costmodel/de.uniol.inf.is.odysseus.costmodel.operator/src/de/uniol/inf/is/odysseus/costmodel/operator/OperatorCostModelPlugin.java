/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel.operator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MedianProcessingTime;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicalplan.PlanMonitor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;

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

	/**
	 * Wird aufgerufen, wenn ein Operatorschätzer registriert wird (von OSGi-
	 * Framework)
	 * 
	 * @param estimator
	 *            Neuer Operatorschätzer
	 */
	public void bindOperatorEstimator(IOperatorEstimator<?> estimator) {
		OperatorEstimatorFactory.getInstance().register(estimator);

		getLogger().debug("Bound OperatorEstimator " + estimator.getClass().getSimpleName() + " for Operator " + estimator.getOperatorClass().getSimpleName());
	}

	/**
	 * Wird aufgerufen, wenn ein Operatorschätzer im OSGi-Framework
	 * deregistriert wurde
	 * 
	 * @param estimator Entfernter Operatorschätzer
	 */
	public void unbindOperatorEstimator(IOperatorEstimator<?> estimator) {
		OperatorEstimatorFactory.getInstance().unregister(estimator);

		getLogger().debug("Unbound OperatorEstimator " + estimator.getClass().getSimpleName() + " for Operator " + estimator.getOperatorClass().getSimpleName());
	}

	@Override
	public void run(IPhysicalQuery query, OptimizationConfiguration parameter) {
		// Bei jeder neuen Anfrage werden "datarate", "selectivity" und
		// "median_processing_time" an alle Operatoren gehängt.
		
		query.addPlanMonitor(DATARATE_TYPE, new PlanMonitor(query, false, false, DATARATE_TYPE, MONITORING_PERIOD_MILLIS));
		query.addPlanMonitor(MonitoringDataTypes.SELECTIVITY.name, new PlanMonitor(query, false, false, MonitoringDataTypes.SELECTIVITY.name, MONITORING_PERIOD_MILLIS));

		// own metadata
		for (IPhysicalOperator operator : query.getPhysicalChilds()) {
			if (operator.isSink()) {
				if (operator.getMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name) == null) {
					long rate = (long)(1000000000 * CPURateSaver.getInstance().get(operator.getClass().getSimpleName()));
					operator.addMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name, new MedianProcessingTime(operator, rate));
				}
			}
		}

	}

}
