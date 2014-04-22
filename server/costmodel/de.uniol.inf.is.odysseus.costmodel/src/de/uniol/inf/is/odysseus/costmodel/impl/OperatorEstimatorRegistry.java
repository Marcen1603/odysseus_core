package de.uniol.inf.is.odysseus.costmodel.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.costmodel.ILogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.IPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.StandardLogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.StandardPhysicalOperatorEstimator;

@SuppressWarnings("unchecked")
public class OperatorEstimatorRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(OperatorEstimatorRegistry.class);
	
	private static final StandardPhysicalOperatorEstimator PHYSICAL_STANDARD_ESTIMATOR = new StandardPhysicalOperatorEstimator();
	private static final StandardLogicalOperatorEstimator LOGICAL_STANDARD_ESTIMATOR = new StandardLogicalOperatorEstimator();
	
	private static final Map<Class<? extends IPhysicalOperator>, Class<? extends IPhysicalOperatorEstimator<?>>> PHYSICAL_ESTIMATORS = Maps.newHashMap();
	private static final Map<Class<? extends ILogicalOperator>, Class<? extends ILogicalOperatorEstimator<?>>> LOGICAL_ESTIMATORS = Maps.newHashMap();

	// called by OSGi-DS
	public static void bindPhysicalOperatorEstimator(IPhysicalOperatorEstimator<?> serv) {
		Collection<? extends Class<?>> classes = serv.getOperatorClasses();
		
		for( Class<?> clazz : classes ) {
			PHYSICAL_ESTIMATORS.put((Class<? extends IPhysicalOperator>) clazz, (Class<? extends IPhysicalOperatorEstimator<?>>) serv.getClass());
		}
	}

	// called by OSGi-DS
	public static void unbindPhysicalOperatorEstimator(IPhysicalOperatorEstimator<?> serv) {
		Collection<? extends Class<?>> classes = serv.getOperatorClasses();
		
		for( Class<?> clazz : classes ) {
			PHYSICAL_ESTIMATORS.remove(clazz);
		}
	}
	
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

	public static <T extends IPhysicalOperator> IPhysicalOperatorEstimator<T> getPhysicalOperatorEstimator( Class<T> operatorClass ) {
		Preconditions.checkNotNull(operatorClass, "operator class to get the estimator from must not be null!");
		Class<? extends IPhysicalOperatorEstimator<?>> estimator = PHYSICAL_ESTIMATORS.get(operatorClass);
		if( estimator == null ) {
			LOG.error("No physical operator estimator defined for operator of class {}", operatorClass.getName());
			return (IPhysicalOperatorEstimator<T>) PHYSICAL_STANDARD_ESTIMATOR;
		}
		
		try {
			return (IPhysicalOperatorEstimator<T>) estimator.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Could not create instance of {}", estimator.getClass().getName());
			return (IPhysicalOperatorEstimator<T>) PHYSICAL_STANDARD_ESTIMATOR;
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
	
	public static IPhysicalOperatorEstimator<IPhysicalOperator> getStandardPhysicalOperatorEstimator() {
		return PHYSICAL_STANDARD_ESTIMATOR;
	}
	
	public static ILogicalOperatorEstimator<ILogicalOperator> getStandardLogicalOperatorEstimator() {
		return LOGICAL_STANDARD_ESTIMATOR;
	}
}
