package de.uniol.inf.is.odysseus.transformation.greedy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.costmodel.base.IPOTransformator;

public class TransformationDatabase {
	private static TransformationDatabase instance = null;

	private final Map<Class<? extends ILogicalOperator>, SortedLinkedList> database = new HashMap<Class<? extends ILogicalOperator>, SortedLinkedList>();
	private final Lock lock = new ReentrantLock();

	private TransformationDatabase() {
	}

	public static TransformationDatabase getInstance() {
		if (instance == null) {
			instance = new TransformationDatabase();
		}
		return instance;
	}

	/**
	 * Adds the physical implementation for a ILogicalOperator in form of a
	 * {@link IPOTransformator}.
	 * 
	 * @param <T>
	 *            - the Type of the {@link ILogicalOperator}
	 * @param logicalOperatorClass
	 *            - the {@link Class} of the {@link ILogicalOperator}
	 * @param transformator
	 *            - the {@link IPOTransformator} to perform the transformation
	 */
	public <T extends ILogicalOperator> void registerTransformator(Class<T> logicalOperatorClass,
			IPOTransformator<T> transformator) {

		lock.lock();
		SortedLinkedList transformatorList = database.get(logicalOperatorClass);

		if (transformatorList == null) {
			transformatorList = new SortedLinkedList();
			database.put(logicalOperatorClass, transformatorList);
		}
		transformatorList.addPrioritised((IPOTransformator<ILogicalOperator>) transformator);
		lock.unlock();
	}

	/**
	 * Unregisters the transformation previously registered with
	 * {@link #registerTransformator(Class, IPOTransformator)}.
	 * 
	 * @param <T>
	 *            - the Type of the {@link ILogicalOperator}
	 * @param logicalOperatorClass
	 *            - the {@link Class} of the {@link ILogicalOperator}
	 * @param transformator
	 *            - the {@link IPOTransformator} to perform the transformation
	 */
	public <T extends ILogicalOperator> void unregisterTransformator(Class<T> logicalOperatorClass,
			IPOTransformator<T> transformator) {

		lock.lock();
		SortedLinkedList transformatorList = database.get(logicalOperatorClass);

		if (transformatorList == null) {
			return;
		}
		transformatorList.remove(transformator);
		if (transformatorList.isEmpty()) {
			database.remove(logicalOperatorClass);
		}
		lock.unlock();
	}

	public SortedLinkedList getOperatorTransformations(ILogicalOperator logicalOperator,
			TransformationConfiguration config) {
		SortedLinkedList transformatorList = database.get(logicalOperator.getClass());
		return transformatorList;
	}
}
