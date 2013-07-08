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
	private final int[] leftProbabilisticAttributePos;
	private final int[] rightProbabilisticAttributePos;
	protected IDataMergeFunction<? super T, K> dataMerge;
	protected IMetadataMergeFunction<K> metadataMerge;

	/**
	 * 
	 * @param leftProbabilisticAttributePos
	 * @param rightProbabilisticAttributePos
	 */
	public ProbabilisticDiscreteJoinTISweepArea(final int[] leftProbabilisticAttributePos, final int[] rightProbabilisticAttributePos, final IDataMergeFunction<? super T, K> dataMerge, final IMetadataMergeFunction<K> metadataMerge) {
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
	public ProbabilisticDiscreteJoinTISweepArea(final ProbabilisticDiscreteJoinTISweepArea area) {
		super();
		this.leftProbabilisticAttributePos = area.leftProbabilisticAttributePos.clone();
		this.rightProbabilisticAttributePos = area.rightProbabilisticAttributePos.clone();
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.AbstractSweepArea#query(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public Iterator<T> query(final T element, final Order order) {
		return new ProbabilisticDiscreteQueryIterator(element, order);
	}

	/**
	 * 
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea#queryCopy(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public Iterator<T> queryCopy(final T element, final Order order) {
		final LinkedList<T> result = new LinkedList<T>();
		synchronized (this.getElements()) {
			T world;
			switch (order) {
			case LeftRight:
				for (final T next : this.getElements()) {

					world = this.evaluateWorld(this.getQueryPredicate(), element, next, this.leftProbabilisticAttributePos, this.rightProbabilisticAttributePos, order);
					if (world.getMetadata().getExistence() > 0.0) {
						result.add(world);
					}
				}
				break;
			case RightLeft:
				for (final T next : this.getElements()) {

					world = this.evaluateWorld(this.getQueryPredicate(), next, element, this.rightProbabilisticAttributePos, this.leftProbabilisticAttributePos, order);
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
		private final Iterator<T> iter;

		T currentElement;

		private final Order order;

		private final T element;

		/**
		 * 
		 * @param element
		 * @param order
		 */
		public ProbabilisticDiscreteQueryIterator(final T element, final Order order) {
			this.iter = ProbabilisticDiscreteJoinTISweepArea.this.getElements().iterator();
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

			switch (this.order) {
			case LeftRight:
				while (this.iter.hasNext()) {
					final T next = this.iter.next();
					final T world = ProbabilisticDiscreteJoinTISweepArea.this.evaluateWorld(ProbabilisticDiscreteJoinTISweepArea.this.getQueryPredicate(), this.element, next, ProbabilisticDiscreteJoinTISweepArea.this.leftProbabilisticAttributePos,
							ProbabilisticDiscreteJoinTISweepArea.this.rightProbabilisticAttributePos, this.order);
					if (world.getMetadata().getExistence() > 0.0) {
						this.currentElement = world;
						return true;
					}
				}
				break;
			case RightLeft:
				while (this.iter.hasNext()) {
					final T next = this.iter.next();
					final T world = ProbabilisticDiscreteJoinTISweepArea.this.evaluateWorld(ProbabilisticDiscreteJoinTISweepArea.this.getQueryPredicate(), next, this.element, ProbabilisticDiscreteJoinTISweepArea.this.rightProbabilisticAttributePos,
							ProbabilisticDiscreteJoinTISweepArea.this.leftProbabilisticAttributePos, this.order);
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
				final T tmpElement = this.currentElement;
				this.currentElement = null;
				return tmpElement;
			}
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}

			return this.next();
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
	private T evaluateWorld(final IPredicate<? super T> predicate, final T left, final T right, final int[] leftProbabilisticAttributePos, final int[] rightProbabilisticAttributePos, final Order order) {
		final T outputVal = (T) this.dataMerge.merge((T) left.clone(), (T) right.clone(), this.metadataMerge, Order.LeftRight);
		final double[] outSum = new double[leftProbabilisticAttributePos.length + rightProbabilisticAttributePos.length];
		for (final int rightProbabilisticAttributePo : rightProbabilisticAttributePos) {
			((AbstractProbabilisticValue<?>) outputVal.getAttribute(left.size() + rightProbabilisticAttributePo)).getValues().clear();
		}
		for (final int leftProbabilisticAttributePo : leftProbabilisticAttributePos) {
			((AbstractProbabilisticValue<?>) outputVal.getAttribute(leftProbabilisticAttributePo)).getValues().clear();
		}

		// Dummy tuple to hold the different worlds during evaluation
		final T leftSelectObject = (T) left.clone();
		final Object[][] leftWorlds = ProbabilisticDiscreteUtils.getWorlds(left, leftProbabilisticAttributePos);

		final T rightSelectObject = (T) right.clone();
		final Object[][] rightWorlds = ProbabilisticDiscreteUtils.getWorlds(right, rightProbabilisticAttributePos);

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
						final AbstractProbabilisticValue<?> inAttribute = (AbstractProbabilisticValue<?>) right.getAttribute(rightProbabilisticAttributePos[i]);
						final AbstractProbabilisticValue<Double> outAttribute = (AbstractProbabilisticValue<Double>) outputVal.getAttribute(left.size() + rightProbabilisticAttributePos[i]);
						final double probability = inAttribute.getValues().get(rightWorlds[rw][i]);
						if (!outAttribute.getValues().containsKey(rightWorlds[rw][i])) {
							outAttribute.getValues().put((Double) rightWorlds[rw][i], probability);
							outSum[leftProbabilisticAttributePos.length + i] += probability;
						}
					}

					for (int i = 0; i < leftProbabilisticAttributePos.length; i++) {
						final AbstractProbabilisticValue<?> inAttribute = (AbstractProbabilisticValue<?>) left.getAttribute(leftProbabilisticAttributePos[i]);
						final AbstractProbabilisticValue<Double> outAttribute = (AbstractProbabilisticValue<Double>) outputVal.getAttribute(leftProbabilisticAttributePos[i]);
						final double probability = inAttribute.getValues().get(leftWorlds[lw][i]);
						if (!outAttribute.getValues().containsKey(leftWorlds[lw][i])) {
							outAttribute.getValues().put((Double) leftWorlds[lw][i], probability);
							outSum[i] += probability;
						}
					}
				}
			}
		}
		double jointProbability = 1.0;
		for (final double element : outSum) {
			jointProbability *= element;
		}
		outputVal.getMetadata().setExistence(jointProbability);
		return outputVal;
	}
}
