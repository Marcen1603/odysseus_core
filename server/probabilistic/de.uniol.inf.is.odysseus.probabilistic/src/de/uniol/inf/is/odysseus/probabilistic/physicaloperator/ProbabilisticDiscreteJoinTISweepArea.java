/*
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <K>
 * @param <T>
 */
public class ProbabilisticDiscreteJoinTISweepArea<K extends IProbabilisticTimeInterval, T extends Tuple<K>> extends JoinTISweepArea<T> implements Cloneable {
   
	private static final long serialVersionUID = 7233937981370968188L;
	
	/** The positions of the probabilistic attributes in the left stream. */
    private final int[] leftProbabilisticAttributePos;
    /** The positions of the probabilistic attributes in the right stream. */
    private final int[] rightProbabilisticAttributePos;
    /** The data merge function. */
    private IDataMergeFunction<? super T, K> dataMerge;
    /** The meta data merge function. */
    private IMetadataMergeFunction<K> metadataMerge;

    /**
     * Creates a new sweep area for probabilistic values.
     * 
     * @param leftProbabilisticAttributePos
     *            The positions of the probabilistic attributes in the left
     *            stream
     * @param rightProbabilisticAttributePos
     *            The positions of the probabilistic attributes in the right
     *            stream
     * @param dataMerge
     *            The data merge function
     * @param metadataMerge
     *            The meta data merge function
     */
    public ProbabilisticDiscreteJoinTISweepArea(final int[] leftProbabilisticAttributePos, final int[] rightProbabilisticAttributePos, final IDataMergeFunction<? super T, K> dataMerge,
            final IMetadataMergeFunction<K> metadataMerge) {
        super();
        this.leftProbabilisticAttributePos = leftProbabilisticAttributePos;
        this.rightProbabilisticAttributePos = rightProbabilisticAttributePos;
        this.dataMerge = dataMerge;
        this.metadataMerge = metadataMerge;
    }

    /**
     * Copy constructor.
     * 
     * @param area
     *            The object to copy from
     */
    public ProbabilisticDiscreteJoinTISweepArea(@SuppressWarnings("rawtypes") final ProbabilisticDiscreteJoinTISweepArea area) {
        super();
        this.leftProbabilisticAttributePos = area.leftProbabilisticAttributePos.clone();
        this.rightProbabilisticAttributePos = area.rightProbabilisticAttributePos.clone();
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.sweeparea.AbstractSweepArea#query(de.uniol.inf
     * .is.odysseus.core.metadata.IStreamObject,
     * de.uniol.inf.is.odysseus.core.Order)
     */
    @Override
    public final Iterator<T> query(final T element, final Order order) {
        return new ProbabilisticDiscreteQueryIterator(element, order);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea#queryCopy
     * (de.uniol.inf.is.odysseus.core.metadata.IStreamObject,
     * de.uniol.inf.is.odysseus.core.Order)
     */
    @Override
    public final Iterator<T> queryCopy(final T element, final Order order, final boolean extract) {
        final LinkedList<T> result = new LinkedList<T>();
        Iterator<T> iter;
        synchronized (this.getElements()) {
            T world;
            switch (order) {
                case LeftRight:
                    iter = this.getElements().iterator();
                    while (iter.hasNext()) {
                        final T next = iter.next();
                        if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
                            continue;
                        }
                        if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
                            break;
                        }
                        world = this.evaluateWorld(this.getQueryPredicate(), element, next, this.leftProbabilisticAttributePos, this.rightProbabilisticAttributePos, order);
                        if (world.getMetadata().getExistence() > 0.0) {
                            result.add(world);
                            if (extract) {
                                iter.remove();
                            }
                        }
                    }
                    break;
                case RightLeft:
                    iter = this.getElements().iterator();
                    while (iter.hasNext()) {
                        final T next = iter.next();
                        if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
                            continue;
                        }
                        if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
                            break;
                        }
                        world = this.evaluateWorld(this.getQueryPredicate(), next, element, this.leftProbabilisticAttributePos, this.rightProbabilisticAttributePos, order);
                        if (world.getMetadata().getExistence() > 0.0) {
                            result.add(world);
                            if (extract) {
                                iter.remove();
                            }
                        }
                    }
                    break;
                default:
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
        /** The element iterator. */
        private final Iterator<T> iter;
        /** The current element. */
        private T currentElement;
        /** The order. */
        private final Order order;
        /** The element. */
        private final T element;

        /**
         * Creates a new query iterator.
         * 
         * @param element
         *            The element
         * @param order
         *            The order
         */
        public ProbabilisticDiscreteQueryIterator(final T element, final Order order) {
            this.iter = ProbabilisticDiscreteJoinTISweepArea.this.getElements().iterator();
            this.order = order;
            this.element = element;
        }

        /*
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
                        final T world = ProbabilisticDiscreteJoinTISweepArea.this.evaluateWorld(ProbabilisticDiscreteJoinTISweepArea.this.getQueryPredicate(), this.element, next,
                                ProbabilisticDiscreteJoinTISweepArea.this.leftProbabilisticAttributePos, ProbabilisticDiscreteJoinTISweepArea.this.rightProbabilisticAttributePos, this.order);
                        if (world.getMetadata().getExistence() > 0.0) {
                            this.currentElement = world;
                            return true;
                        }
                    }
                    break;
                case RightLeft:
                    while (this.iter.hasNext()) {
                        final T next = this.iter.next();
                        final T world = ProbabilisticDiscreteJoinTISweepArea.this.evaluateWorld(ProbabilisticDiscreteJoinTISweepArea.this.getQueryPredicate(), next, this.element,
                                ProbabilisticDiscreteJoinTISweepArea.this.rightProbabilisticAttributePos, ProbabilisticDiscreteJoinTISweepArea.this.leftProbabilisticAttributePos, this.order);
                        if (world.getMetadata().getExistence() > 0.0) {
                            this.currentElement = world;
                            return true;
                        }
                    }
                    break;
                default:
                    break;
            }
            this.currentElement = null;
            return false;
        }

        /*
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
     * Evaluates each world with the given predicate.
     * 
     * @param predicate
     *            The predicate to evaluate
     * @param right
     *            The right element
     * @param left
     *            The left element
     * @param rightProbabilisticAttributePositions
     *            The positions of probabilistic values in the right
     * @param leftProbabilisticAttributePositions
     *            The positions of probabilistic values in the left
     * @param order
     *            The order to evaluate
     * @return The result element
     */
    @SuppressWarnings({ "unchecked", "unused" })
    private T evaluateWorld(final IPredicate<? super T> predicate, final T left, final T right, final int[] leftProbabilisticAttributePositions, final int[] rightProbabilisticAttributePositions,
            final Order order) {
        final T outputVal = (T) this.dataMerge.merge((T) left.clone(), (T) right.clone(), this.metadataMerge, order);
        final double[] outSum = new double[leftProbabilisticAttributePositions.length + rightProbabilisticAttributePositions.length];
        // for (final int rightProbabilisticAttributePo :
        // rightProbabilisticAttributePositions) {
        // ((AbstractProbabilisticValue<?>) outputVal.getAttribute(left.size() +
        // rightProbabilisticAttributePo)).getValues().clear();
        // }
        // for (final int leftProbabilisticAttributePo :
        // leftProbabilisticAttributePositions) {
        // ((AbstractProbabilisticValue<?>)
        // outputVal.getAttribute(leftProbabilisticAttributePo)).getValues().clear();
        // }

        // Dummy tuple to hold the different worlds during evaluation
        final T leftSelectObject = (T) left.clone();
        // final Object[][] leftWorlds =
        // ProbabilisticDiscreteUtils.getWorlds(left,
        // leftProbabilisticAttributePositions);

        final T rightSelectObject = (T) right.clone();
        // final Object[][] rightWorlds =
        // ProbabilisticDiscreteUtils.getWorlds(right,
        // rightProbabilisticAttributePositions);

        // Evaluate each world and store the possible ones in the output tuple
        // for (int lw = 0; lw < leftWorlds.length; lw++) {
        // for (int rw = 0; rw < rightWorlds.length; rw++) {
        // for (int i = 0; i < leftProbabilisticAttributePositions.length; i++)
        // {
        // leftSelectObject.setAttribute(leftProbabilisticAttributePositions[i],
        // leftWorlds[lw][i]);
        // }
        // for (int i = 0; i < rightProbabilisticAttributePositions.length; i++)
        // {
        // rightSelectObject.setAttribute(rightProbabilisticAttributePositions[i],
        // rightWorlds[rw][i]);
        // }
        //
        // if (predicate.evaluate(leftSelectObject, rightSelectObject)) {
        // for (int i = 0; i < rightProbabilisticAttributePositions.length; i++)
        // {
        // final AbstractProbabilisticValue<?> inAttribute =
        // (AbstractProbabilisticValue<?>)
        // right.getAttribute(rightProbabilisticAttributePositions[i]);
        // final AbstractProbabilisticValue<Double> outAttribute =
        // (AbstractProbabilisticValue<Double>)
        // outputVal.getAttribute(left.size() +
        // rightProbabilisticAttributePositions[i]);
        // final double probability =
        // inAttribute.getValues().get(rightWorlds[rw][i]);
        // if (!outAttribute.getValues().containsKey(rightWorlds[rw][i])) {
        // outAttribute.getValues().put((Double) rightWorlds[rw][i],
        // probability);
        // outSum[leftProbabilisticAttributePositions.length + i] +=
        // probability;
        // }
        // }
        //
        // for (int i = 0; i < leftProbabilisticAttributePositions.length; i++)
        // {
        // final AbstractProbabilisticValue<?> inAttribute =
        // (AbstractProbabilisticValue<?>)
        // left.getAttribute(leftProbabilisticAttributePositions[i]);
        // final AbstractProbabilisticValue<Double> outAttribute =
        // (AbstractProbabilisticValue<Double>)
        // outputVal.getAttribute(leftProbabilisticAttributePositions[i]);
        // final double probability =
        // inAttribute.getValues().get(leftWorlds[lw][i]);
        // if (!outAttribute.getValues().containsKey(leftWorlds[lw][i])) {
        // outAttribute.getValues().put((Double) leftWorlds[lw][i],
        // probability);
        // outSum[i] += probability;
        // }
        // }
        // }
        // }
        // }
        double jointProbability = 1.0;
        for (final double element : outSum) {
            jointProbability *= element;
        }
        outputVal.getMetadata().setExistence(jointProbability);
        return outputVal;
    }
}
