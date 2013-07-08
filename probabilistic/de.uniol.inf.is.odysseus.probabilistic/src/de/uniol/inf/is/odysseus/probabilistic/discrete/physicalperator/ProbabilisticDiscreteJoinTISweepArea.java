package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.common.ProbabilisticDiscreteUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticDiscreteJoinTISweepArea<K extends ITimeIntervalProbabilistic, T extends Tuple<K>> extends JoinTISweepArea<T> implements Cloneable {
	private int[] leftProbabilisticAttributePos;
	private int[] rightProbabilisticAttributePos;
	protected IDataMergeFunction<? super T, K> dataMerge;
	protected IMetadataMergeFunction<K> metadataMerge;

	/**
	 * 
	 * @param leftProbabilisticAttributePos
	 * @param rightProbabilisticAttributePos
	 */
	public ProbabilisticDiscreteJoinTISweepArea(int[] leftProbabilisticAttributePos, int[] rightProbabilisticAttributePos, IDataMergeFunction<? super T, K> dataMerge, IMetadataMergeFunction<K> metadataMerge) {
		super();
		this.leftProbabilisticAttributePos = leftProbabilisticAttributePos;
		this.rightProbabilisticAttributePos = rightProbabilisticAttributePos;
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
	}

	/**
	 * 
	 * @param area
	 */
	public ProbabilisticDiscreteJoinTISweepArea(ProbabilisticDiscreteJoinTISweepArea area) {
		super();
		this.leftProbabilisticAttributePos = area.leftProbabilisticAttributePos.clone();
		this.rightProbabilisticAttributePos = area.rightProbabilisticAttributePos.clone();
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.AbstractSweepArea#query(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public Iterator<T> query(T element, Order order) {
		return new ProbabilisticDiscreteQueryIterator(element, order);
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea#queryCopy(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public Iterator<T> queryCopy(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		synchronized (this.getElements()) {
			T world;
			switch (order) {
			case LeftRight:
				for (T next : this.getElements()) {

					world = evaluateWorld(getQueryPredicate(), element, next, leftProbabilisticAttributePos, rightProbabilisticAttributePos, order);
					if (world.getMetadata().getExistence() > 0.0) {
						result.add(world);
					}
				}
				break;
			case RightLeft:
				for (T next : this.getElements()) {

					world = evaluateWorld(getQueryPredicate(), next, element, rightProbabilisticAttributePos, leftProbabilisticAttributePos, order);
					if (world.getMetadata().getExistence() > 0.0) {
						result.add(world);
					}
				}
				break;
			}
		}
		return result.iterator();
	}

	/**
	 * 
	 * @author Christian Kuka <christian@kuka.cc>
	 * 
	 */
	private class ProbabilisticDiscreteQueryIterator implements Iterator<T> {
		private Iterator<T> iter;

		T currentElement;

		private Order order;

		private T element;

		/**
		 * 
		 * @param element
		 * @param order
		 */
		public ProbabilisticDiscreteQueryIterator(T element, Order order) {
			this.iter = getElements().iterator();
			this.order = order;
			this.element = element;
		}

		/**
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			if (this.currentElement != null) {
				return true;
			}

			switch (order) {
			case LeftRight:
				while (iter.hasNext()) {
					T next = iter.next();
					T world = evaluateWorld(getQueryPredicate(), element, next, leftProbabilisticAttributePos, rightProbabilisticAttributePos, order);
					if (world.getMetadata().getExistence() > 0.0) {
						this.currentElement = world;
						return true;
					}
				}
				break;
			case RightLeft:
				while (iter.hasNext()) {
					T next = iter.next();
					T world = evaluateWorld(getQueryPredicate(), next, element, rightProbabilisticAttributePos, leftProbabilisticAttributePos, order);
					if (world.getMetadata().getExistence() > 0.0) {
						this.currentElement = world;
						return true;
					}
				}
				break;
			}
			this.currentElement = null;
			return false;
		}

		/**
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next() {
			if (this.currentElement != null) {
				T tmpElement = this.currentElement;
				this.currentElement = null;
				return tmpElement;
			}
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			return next();
		}

		/**
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			this.iter.remove();
		}

	}

	/**
	 * 
	 * @param predicate
	 * @param right
	 * @param left
	 * @param rightProbabilisticAttributePos
	 * @param leftProbabilisticAttributePos
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T evaluateWorld(IPredicate<? super T> predicate, T left, T right, int[] leftProbabilisticAttributePos, int[] rightProbabilisticAttributePos, Order order) {
		final T outputVal = (T) this.dataMerge.merge((T) left.clone(), (T) right.clone(), this.metadataMerge, Order.LeftRight);
		final double[] outSum = new double[leftProbabilisticAttributePos.length + rightProbabilisticAttributePos.length];
		for (int i = 0; i < rightProbabilisticAttributePos.length; i++) {
			((AbstractProbabilisticValue<?>) outputVal.getAttribute(left.size() + rightProbabilisticAttributePos[i])).getValues().clear();
		}
		for (int i = 0; i < leftProbabilisticAttributePos.length; i++) {
			((AbstractProbabilisticValue<?>) outputVal.getAttribute(leftProbabilisticAttributePos[i])).getValues().clear();
		}

		// Dummy tuple to hold the different worlds during evaluation
		T leftSelectObject = (T) left.clone();
		Object[][] leftWorlds = ProbabilisticDiscreteUtils.getWorlds(left, leftProbabilisticAttributePos);

		T rightSelectObject = (T) right.clone();
		Object[][] rightWorlds = ProbabilisticDiscreteUtils.getWorlds(right, rightProbabilisticAttributePos);

		// Evaluate each world and store the possible ones in the output tuple
		for (int lw = 0; lw < leftWorlds.length; lw++) {
			for (int rw = 0; rw < rightWorlds.length; rw++) {
				for (int i = 0; i < leftProbabilisticAttributePos.length; i++) {
					leftSelectObject.setAttribute(leftProbabilisticAttributePos[i], leftWorlds[lw][i]);
				}
				for (int i = 0; i < rightProbabilisticAttributePos.length; i++) {
					rightSelectObject.setAttribute(rightProbabilisticAttributePos[i], rightWorlds[rw][i]);
				}

				if (predicate.evaluate(leftSelectObject, rightSelectObject)) {
					for (int i = 0; i < rightProbabilisticAttributePos.length; i++) {
						AbstractProbabilisticValue<?> inAttribute = (AbstractProbabilisticValue<?>) right.getAttribute(rightProbabilisticAttributePos[i]);
						AbstractProbabilisticValue<Double> outAttribute = (AbstractProbabilisticValue<Double>) outputVal.getAttribute(left.size() + rightProbabilisticAttributePos[i]);
						double probability = inAttribute.getValues().get(rightWorlds[rw][i]);
						if (!outAttribute.getValues().containsKey((Double) rightWorlds[rw][i])) {
							outAttribute.getValues().put((Double) rightWorlds[rw][i], probability);
							outSum[leftProbabilisticAttributePos.length + i] += probability;
						}
					}

					for (int i = 0; i < leftProbabilisticAttributePos.length; i++) {
						AbstractProbabilisticValue<?> inAttribute = (AbstractProbabilisticValue<?>) left.getAttribute(leftProbabilisticAttributePos[i]);
						AbstractProbabilisticValue<Double> outAttribute = (AbstractProbabilisticValue<Double>) outputVal.getAttribute(leftProbabilisticAttributePos[i]);
						double probability = inAttribute.getValues().get(leftWorlds[lw][i]);
						if (!outAttribute.getValues().containsKey((Double) leftWorlds[lw][i])) {
							outAttribute.getValues().put((Double) leftWorlds[lw][i], probability);
							outSum[i] += probability;
						}
					}
				}
			}
		}
		double jointProbability = 1.0;
		for (int i = 0; i < outSum.length; i++) {
			jointProbability *= outSum[i];
		}
		outputVal.getMetadata().setExistence(jointProbability);
		return outputVal;
	}
}
