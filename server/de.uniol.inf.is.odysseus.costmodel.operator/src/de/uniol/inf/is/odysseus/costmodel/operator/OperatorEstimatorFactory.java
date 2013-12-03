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

import java.util.Map;

import com.google.common.collect.Maps;

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

	private Map<Class<?>, Class<? extends IOperatorEstimator<?>>> estimators = Maps.newHashMap();

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
	public <T> IOperatorEstimator<T> get(T operator) {
		if (operator == null)
			return new StandardOperatorEstimator<T>();

		Class<?> clazz = operator.getClass();
		Class<? extends IOperatorEstimator<?>> estimatorClass = estimators.get(clazz);

		if (estimatorClass == null) {
			// use standard-estimator
			return new StandardOperatorEstimator<T>();
		}

		try {
			IOperatorEstimator<?> newInstance = estimatorClass.newInstance();
			return (IOperatorEstimator<T>) newInstance;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return new StandardOperatorEstimator<T>();
	}
}
