package de.uniol.inf.is.odysseus.costmodel.physical.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("unchecked")
public class OperatorEstimatorRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(OperatorEstimatorRegistry.class);
	
	private static final StandardPhysicalOperatorEstimator<IPhysicalOperator> PHYSICAL_STANDARD_ESTIMATOR = new StandardPhysicalOperatorEstimator<IPhysicalOperator>() {
		
		@Override
		protected Class<? extends IPhysicalOperator> getOperatorClass() {
			return IPhysicalOperator.class;
		};
		
	};
	
	private static final Map<Class<? extends IPhysicalOperator>, Class<? extends IPhysicalOperatorEstimator<?>>> PHYSICAL_ESTIMATORS = Maps.newHashMap();

	// called by OSGi-DS
	public static void bindPhysicalOperatorEstimator(IPhysicalOperatorEstimator<?> serv) {
		Collection<? extends Class<?>> classes = serv.getOperatorClasses();
		
		if( classes == null || classes.isEmpty()) {
			throw new RuntimeException("Estimator " + serv.getClass().getName() + " returns empty or null list of operator classes!");
		}
		
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
	
	public static IPhysicalOperatorEstimator<IPhysicalOperator> getStandardPhysicalOperatorEstimator() {
		return PHYSICAL_STANDARD_ESTIMATOR;
	}
	
	public static Collection<Class<? extends IPhysicalOperator>> getRegisteredPhysicalOperators() {
		return PHYSICAL_ESTIMATORS.keySet();
	}
}
