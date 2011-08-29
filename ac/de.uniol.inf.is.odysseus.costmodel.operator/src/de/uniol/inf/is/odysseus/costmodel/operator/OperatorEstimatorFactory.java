package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public class OperatorEstimatorFactory {

	private static OperatorEstimatorFactory instance = null;
	
	private Map<Class<? extends IPhysicalOperator>, Class<? extends IOperatorEstimator<? extends IPhysicalOperator>>> estimators = new HashMap
			   <Class<? extends IPhysicalOperator>, Class<? extends IOperatorEstimator<? extends IPhysicalOperator>>>();
	
	private OperatorEstimatorFactory() {
		
	}
	
	public static OperatorEstimatorFactory getInstance() {
		if( instance == null ) 
			instance = new OperatorEstimatorFactory();
		
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void register( IOperatorEstimator<?> estimator ) {
		estimators.put( estimator.getOperatorClass(), (Class<IOperatorEstimator<? extends IPhysicalOperator>>)estimator.getClass());
	}
	
	public void unregister( IOperatorEstimator<?> estimator ) {
		estimators.remove(estimator.getOperatorClass());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IPhysicalOperator> IOperatorEstimator<T> get( T operator ) {
		if( operator == null )
			return new StandardOperatorEstimator<T>();
		
		Class<? extends IPhysicalOperator> clazz = (Class<? extends IPhysicalOperator>) operator.getClass();
		Class<? extends IOperatorEstimator<? extends IPhysicalOperator>> estimatorClass = (Class<IOperatorEstimator<? extends IPhysicalOperator>>) estimators.get(clazz);
		
		if( estimatorClass == null ) {
			// use standard-estimator
			return new StandardOperatorEstimator<T>();
		}
		
		try {
			IOperatorEstimator<? extends IPhysicalOperator> newInstance = estimatorClass.newInstance();
			return (IOperatorEstimator<T>) newInstance;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return new StandardOperatorEstimator<T>();
	}
}
