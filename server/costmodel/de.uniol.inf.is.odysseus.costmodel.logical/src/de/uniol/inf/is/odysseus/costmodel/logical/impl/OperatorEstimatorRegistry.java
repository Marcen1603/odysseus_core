package de.uniol.inf.is.odysseus.costmodel.logical.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.costmodel.logical.ILogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

@SuppressWarnings("unchecked")
public class OperatorEstimatorRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(OperatorEstimatorRegistry.class);
	
	private static final StandardLogicalOperatorEstimator<ILogicalOperator> LOGICAL_STANDARD_ESTIMATOR = new StandardLogicalOperatorEstimator<ILogicalOperator>() {

		@Override
		protected java.lang.Class<? extends ILogicalOperator> getOperatorClass() {
			return ILogicalOperator.class;
		};
	};
	
	private static final Map<Class<? extends ILogicalOperator>, Class<? extends ILogicalOperatorEstimator<?>>> LOGICAL_ESTIMATORS = Maps.newHashMap();

	// called by OSGi-DS
	public static void bindLogicalOperatorEstimator(ILogicalOperatorEstimator<?> serv) {
		Collection<? extends Class<?>> classes = serv.getOperatorClasses();
		
		for( Class<?> clazz : classes ) {
			LOGICAL_ESTIMATORS.put((Class<? extends ILogicalOperator>) clazz, (Class<? extends ILogicalOperatorEstimator<?>>) serv.getClass());
		}
	}

	// called by OSGi-DS
	public static void unbindLogicalOperatorEstimator(ILogicalOperatorEstimator<?> serv) {
		Collection<? extends Class<?>> classes = serv.getOperatorClasses();
		
		for( Class<?> clazz : classes ) {
			LOGICAL_ESTIMATORS.remove(clazz);
		}
	}
	
	public static <T extends ILogicalOperator> ILogicalOperatorEstimator<T> getLogicalOperatorEstimator( Class<T> operatorClass ) {
		Preconditions.checkNotNull(operatorClass, "operator class to get the estimator from must not be null!");
		Class<? extends ILogicalOperatorEstimator<?>> estimator = LOGICAL_ESTIMATORS.get(operatorClass);
		if( estimator == null ) {
			LOG.error("No logical operator estimator defined for operator of class {}", operatorClass.getName());
			return (ILogicalOperatorEstimator<T>) LOGICAL_STANDARD_ESTIMATOR;
		}
		
		try {
			return (ILogicalOperatorEstimator<T>) estimator.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Could not create instance of {}", estimator.getClass().getName());
			return (ILogicalOperatorEstimator<T>) LOGICAL_STANDARD_ESTIMATOR;
		}
	}
	
	public static ILogicalOperatorEstimator<ILogicalOperator> getStandardLogicalOperatorEstimator() {
		return LOGICAL_STANDARD_ESTIMATOR;
	}
	
	public static Collection<Class<? extends ILogicalOperator>> getRegisteredLogicalOperators() {
		return LOGICAL_ESTIMATORS.keySet();
	}
}
