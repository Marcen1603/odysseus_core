package de.uniol.inf.is.odysseus.costmodel.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.costmodel.IPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.StandardPhysicalOperatorEstimator;

public class OperatorEstimatorRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(OperatorEstimatorRegistry.class);
	private static final StandardPhysicalOperatorEstimator STANDARD_ESTIMATOR = new StandardPhysicalOperatorEstimator();
	private static final Map<Class<? extends IPhysicalOperator>, Class<? extends IPhysicalOperatorEstimator<?>>> ESTIMATORS = Maps.newHashMap();

	// called by OSGi-DS
	@SuppressWarnings("unchecked")
	public static void bindPhysicalOperatorEstimator(IPhysicalOperatorEstimator<?> serv) {
		Collection<? extends Class<?>> classes = serv.getOperatorClasses();
		
		for( Class<?> clazz : classes ) {
			ESTIMATORS.put((Class<? extends IPhysicalOperator>) clazz, (Class<? extends IPhysicalOperatorEstimator<?>>) serv.getClass());
		}
	}

	// called by OSGi-DS
	public static void unbindPhysicalOperatorEstimator(IPhysicalOperatorEstimator<?> serv) {
		Collection<? extends Class<?>> classes = serv.getOperatorClasses();
		
		for( Class<?> clazz : classes ) {
			ESTIMATORS.remove(clazz);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IPhysicalOperator> IPhysicalOperatorEstimator<T> getPhysicalOperatorEstimator( Class<T> operatorClass ) {
		Preconditions.checkNotNull(operatorClass, "operator class to get the estimator from must not be null!");
		Class<? extends IPhysicalOperatorEstimator<?>> estimator = ESTIMATORS.get(operatorClass);
		if( estimator == null ) {
			LOG.error("No physical operator estimator defined for operator of class {}", operatorClass.getName());
			return (IPhysicalOperatorEstimator<T>) STANDARD_ESTIMATOR;
		}
		
		try {
			return (IPhysicalOperatorEstimator<T>) estimator.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Could not create instance of {}", estimator.getClass().getName());
			return (IPhysicalOperatorEstimator<T>) STANDARD_ESTIMATOR;
		}
	}
	
	public static IPhysicalOperatorEstimator<IPhysicalOperator> getStandardPhysicalOperatorEstimator() {
		return STANDARD_ESTIMATOR;
	}
}
