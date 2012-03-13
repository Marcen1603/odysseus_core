package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * Singleton-Klasse, welcher alle Operatorschätzer verwaltet und anderen Klassen
 * zur Verfügung stellt.
 * 
 * @author Timo Michelsen
 * 
 */
public class OperatorEstimatorFactory {

	private static OperatorEstimatorFactory instance = null;

	private Map<Class<? extends IPhysicalOperator>, Class<? extends IOperatorEstimator<? extends IPhysicalOperator>>> estimators = new HashMap<Class<? extends IPhysicalOperator>, Class<? extends IOperatorEstimator<? extends IPhysicalOperator>>>();

	private OperatorEstimatorFactory() {

	}

	/**
	 * Liefert die einzige Instanz dieser Klasse
	 * 
	 * @return Einzige Instanz
	 */
	public static OperatorEstimatorFactory getInstance() {
		if (instance == null)
			instance = new OperatorEstimatorFactory();

		return instance;
	}

	/**
	 * Registriert einen Operatorschätzer, sodass dieser dem Kostenmodell in
	 * Zukunft zur Verfügung steht.
	 * 
	 * @param estimator
	 *            Neuer Operatorschätzer
	 */
	@SuppressWarnings("unchecked")
	public void register(IOperatorEstimator<?> estimator) {
		estimators.put(estimator.getOperatorClass(), (Class<IOperatorEstimator<? extends IPhysicalOperator>>) estimator.getClass());
	}

	/**
	 * Deregistriert einen Operatorschätzer.
	 * 
	 * @param estimator
	 *            Zu entfernenden Operatorschätzer
	 */
	public void unregister(IOperatorEstimator<?> estimator) {
		estimators.remove(estimator.getOperatorClass());
	}

	/**
	 * Liefert zu einem konkreten Operator eine neue Instanz des
	 * korrespondierenden Schätzers. Ist kein Schätzer zum Operator bekannt,
	 * wird ein Standardschätzer zurückgegeben.
	 * 
	 * @param operator
	 *            Physischer Operator, dessen Schätzer gebraucht wird
	 * @return Schätzer zum physischen Operator
	 */
	@SuppressWarnings("unchecked")
	public <T extends IPhysicalOperator> IOperatorEstimator<T> get(T operator) {
		if (operator == null)
			return new StandardOperatorEstimator<T>();

		Class<? extends IPhysicalOperator> clazz = operator.getClass();
		Class<? extends IOperatorEstimator<? extends IPhysicalOperator>> estimatorClass = estimators.get(clazz);

		if (estimatorClass == null) {
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
