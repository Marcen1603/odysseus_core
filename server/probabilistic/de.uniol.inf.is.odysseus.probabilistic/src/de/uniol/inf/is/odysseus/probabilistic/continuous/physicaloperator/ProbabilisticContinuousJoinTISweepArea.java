/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousJoinTISweepArea<K extends ITimeIntervalProbabilistic, T extends ProbabilisticTuple<K>> extends JoinTISweepArea<T> implements Cloneable {
    /** Attribute positions list required for variable bindings. */
    private VarHelper[][] variables;
    /** The expressions. */
    private SDFProbabilisticExpression[] expressions;
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
    public ProbabilisticContinuousJoinTISweepArea(final int[] leftProbabilisticAttributePos, final int[] rightProbabilisticAttributePos, final IDataMergeFunction<? super T, K> dataMerge,
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
    public ProbabilisticContinuousJoinTISweepArea(@SuppressWarnings("rawtypes") final ProbabilisticContinuousJoinTISweepArea area) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setQueryPredicate(IPredicate<? super T> updatePredicate) {
        super.setQueryPredicate(updatePredicate);
        init(null, getQueryPredicate());
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
                        world = this.evaluate(this.getQueryPredicate(), element, next, this.leftProbabilisticAttributePos, this.rightProbabilisticAttributePos, order);
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
                        world = this.evaluate(this.getQueryPredicate(), next, element, this.leftProbabilisticAttributePos, this.rightProbabilisticAttributePos, order);
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

    private void init(final SDFSchema schema, final IPredicate<?> selectPredicate) {
        final List<SDFExpression> expressionsList = PredicateUtils.getExpressions(selectPredicate);
        this.expressions = new SDFProbabilisticExpression[expressionsList.size()];
        for (int i = 0; i < expressionsList.size(); i++) {
            this.expressions[i] = new SDFProbabilisticExpression(expressionsList.get(i));
        }
        this.variables = new VarHelper[this.expressions.length][];
        final Set<SDFAttribute> neededAttributesSet = new HashSet<>();

        for (final SDFExpression expression : expressionsList) {
            final List<SDFAttribute> neededAttributes = expression.getAllAttributes();
            neededAttributesSet.addAll(neededAttributes);
        }
        int i = 0;
        for (final SDFExpression expression : expressionsList) {
            final List<SDFAttribute> neededAttributes = expression.getAllAttributes();

            final VarHelper[] newArray = new VarHelper[neededAttributes.size()];

            this.variables[i++] = newArray;
            int j = 0;
            for (final SDFAttribute curAttribute : neededAttributes) {
                newArray[j++] = new VarHelper(schema.indexOf(curAttribute), 0, curAttribute.getDatatype() instanceof SDFProbabilisticDatatype);
            }
            // if
            // (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE))
            // {
            // distributions++;
            // }
        }
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
            this.iter = ProbabilisticContinuousJoinTISweepArea.this.getElements().iterator();
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
                        final T world = ProbabilisticContinuousJoinTISweepArea.this.evaluate(ProbabilisticContinuousJoinTISweepArea.this.getQueryPredicate(), this.element, next,
                                ProbabilisticContinuousJoinTISweepArea.this.leftProbabilisticAttributePos, ProbabilisticContinuousJoinTISweepArea.this.rightProbabilisticAttributePos, this.order);
                        if (world.getMetadata().getExistence() > 0.0) {
                            this.currentElement = world;
                            return true;
                        }
                    }
                    break;
                case RightLeft:
                    while (this.iter.hasNext()) {
                        final T next = this.iter.next();
                        final T world = ProbabilisticContinuousJoinTISweepArea.this.evaluate(ProbabilisticContinuousJoinTISweepArea.this.getQueryPredicate(), next, this.element,
                                ProbabilisticContinuousJoinTISweepArea.this.rightProbabilisticAttributePos, ProbabilisticContinuousJoinTISweepArea.this.leftProbabilisticAttributePos, this.order);
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
    @SuppressWarnings("unchecked")
    private T evaluate(final IPredicate<? super T> predicate, final T left, final T right, final int[] leftProbabilisticAttributePositions, final int[] rightProbabilisticAttributePositions,
            final Order order) {
        final T outputVal = (T) this.dataMerge.merge((T) left.clone(), (T) right.clone(), this.metadataMerge, order);
        double jointProbability = outputVal.getMetadata().getExistence();
        synchronized (this.expressions) {
            double scale = 1.0;
            for (int i = 0; i < this.expressions.length; ++i) {
                int d = 0;

                final Object[] values = new Object[this.variables[i].length];
                for (int j = 0; j < this.variables[i].length; ++j) {
                    Object attribute = outputVal.getAttribute(this.variables[i][j].getPos());
                    if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
                        int index = ((ProbabilisticContinuousDouble) attribute).getDistribution();
                        attribute = outputVal.getDistribution(index);
                        scale = ((NormalDistributionMixture) attribute).getScale();
                        // FIXME What happens if we have more than one
                        // distribution inside an expression or even other
                        // functions? (CKu 17.12.2013)
                        if (d >= 0) {
                            throw new IllegalArgumentException("More than one distribution not supported inside predicate");
                        }
                        d = index;
                    }
                    values[j] = attribute;
                }

                this.expressions[i].bindMetaAttribute(outputVal.getMetadata());
                this.expressions[i].bindDistributions(outputVal.getDistributions());
                this.expressions[i].bindAdditionalContent(outputVal.getAdditionalContent());
                this.expressions[i].bindVariables(values);

                final Object expr = this.expressions[i].getValue();
                if (this.expressions[i].getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE)) {
                    final NormalDistributionMixture distribution = (NormalDistributionMixture) expr;
                    jointProbability *= scale / distribution.getScale();
                    // distribution.getAttributes()[0] = i;
                    outputVal.setDistribution(d, distribution);
                    // outputVal.setAttribute(i, new
                    // ProbabilisticContinuousDouble(d));
                    ((IProbabilistic) outputVal.getMetadata()).setExistence(jointProbability);
                    d++;
                }
                else {
                    if (!(Boolean) expr) {
                        jointProbability = 0.0;
                        ((IProbabilistic) outputVal.getMetadata()).setExistence(jointProbability);
                    }
                }
            }
        }
        return outputVal;

    }

    @SuppressWarnings({ "unused", "unchecked" })
    private T evaulateAndOperator(T input) {
        T output = (T) input.clone();
        // call first expression with output and store the result in output
        // again
        // Call second expression with output and store the result in output
        // again

        return output;
    }

    @SuppressWarnings({ "unused", "unchecked" })
    private T evaulateOrOperator(T input) {
        T a = (T) input.clone();
        T b = (T) input.clone();
        // call first expression with a and store the result in a again
        // Call second expression with b and store the result in b again
        if (a.getMetadata().getExistence() > b.getMetadata().getExistence()) {
            return a;
        }
        else {
            return b;
        }
    }
}
