/**
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.functions.compare;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
abstract public class AbstractProbabilisticCompareOperator extends AbstractProbabilisticBinaryOperator<ProbabilisticBooleanResult> {

    /**
     *
     */
    private static final long serialVersionUID = -7850744519118122850L;

    public AbstractProbabilisticCompareOperator(final String symbol, final SDFDatatype[][] accTypes) {
        super(symbol, accTypes, SDFProbabilisticDatatype.PROBABILISTIC_RESULT);
    }

    protected final ProbabilisticBooleanResult getValueInternal(final MultivariateMixtureDistribution a, final double[] lowerBound, final double[] upperBound) {
        return getValueInternal(a, lowerBound, upperBound, false, true);
    }

    protected final ProbabilisticBooleanResult getValueInternal(final MultivariateMixtureDistribution a, final MultivariateMixtureDistribution b, final double[] lowerBound, final double[] upperBound,
            final boolean leftInclusive, final boolean rightInclusive) {
        final double probability;
        final MultivariateMixtureDistribution c = a.subtract(b);

        if (Arrays.equals(lowerBound, upperBound)) {
            probability = c.probability(lowerBound, upperBound);
        } else {
            if (!leftInclusive && rightInclusive) {
                // Estimate the probability in the given bounds: P(lowerBound <
                // X <=
                // upperBound)
                probability = c.probability(lowerBound, upperBound) - c.probability(lowerBound, lowerBound);
            } else if (leftInclusive && rightInclusive) {
                // Estimate the probability in the given bounds:
                // P(lowerBound <= X <= upperBound)
                probability = c.probability(lowerBound, upperBound);
            } else if (leftInclusive) {
                // Estimate the probability in the given bounds: P(lowerBound <=
                // X <
                // upperBound)
                probability = c.probability(lowerBound, upperBound) - c.probability(upperBound, upperBound);
            } else {
                // Estimate the probability in the given bounds: P(lowerBound <
                // X < upperBound)
                probability = c.probability(lowerBound, upperBound) - c.probability(lowerBound, lowerBound) - c.probability(upperBound, upperBound);
            }
        }
        final MultivariateMixtureDistribution aResult = a.clone();
        final MultivariateMixtureDistribution bResult = b.clone();

        if (probability == 0.0) {
            aResult.setScale(Double.POSITIVE_INFINITY);
            bResult.setScale(Double.POSITIVE_INFINITY);
        } else {
            aResult.setScale(a.getScale());
            bResult.setScale(b.getScale());
        }
        // final Interval[] support = new Interval[c.getDimension()];
        // for (int i = 0; i < c.getDimension(); i++) {
        // final Interval interval = new Interval(lowerBound[i], upperBound[i]);
        // support[i] = c.getSupport(i).intersection(interval);
        // }
        // aResult.setSupport(support);
        return new ProbabilisticBooleanResult(new IMultivariateDistribution[] { aResult, bResult }, probability);
    }

    protected final ProbabilisticBooleanResult getValueInternal(final MultivariateMixtureDistribution a, final double[] lowerBound, final double[] upperBound, final boolean leftInclusive,
            final boolean rightInclusive) {
        final double probability;
        if (Arrays.equals(lowerBound, upperBound)) {
            probability = a.probability(lowerBound, upperBound);
        } else {
            if (!leftInclusive && rightInclusive) {
                // Estimate the probability in the given bounds: P(lowerBound <
                // X <=
                // upperBound)
                probability = a.probability(lowerBound, upperBound) - a.probability(lowerBound, lowerBound);
            } else if (leftInclusive && rightInclusive) {
                // Estimate the probability in the given bounds:
                // P(lowerBound <= X <= upperBound)
                probability = a.probability(lowerBound, upperBound);
            } else if (leftInclusive) {
                // Estimate the probability in the given bounds: P(lowerBound <=
                // X <
                // upperBound)
                probability = a.probability(lowerBound, upperBound) - a.probability(upperBound, upperBound);
            } else {
                // Estimate the probability in the given bounds: P(lowerBound <
                // X < upperBound)
                probability = a.probability(lowerBound, upperBound) - a.probability(lowerBound, lowerBound) - a.probability(upperBound, upperBound);
            }
        }
        final MultivariateMixtureDistribution result = a.clone();
        if (probability == 0.0) {
            result.setScale(Double.POSITIVE_INFINITY);
        } else {
            result.setScale(a.getScale() / probability);
        }
        final Interval[] support = new Interval[a.getDimension()];
        for (int i = 0; i < a.getDimension(); i++) {
            final Interval interval = new Interval(lowerBound[i], upperBound[i]);
            support[i] = a.getSupport(i).intersection(interval);
        }
        result.setSupport(support);

        return new ProbabilisticBooleanResult(result, probability);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final boolean isCommutative() {
        return false;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final boolean isAssociative() {
        return false;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final boolean isLeftDistributiveWith(final IOperator<ProbabilisticBooleanResult> operator) {
        return false;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final boolean isRightDistributiveWith(final IOperator<ProbabilisticBooleanResult> operator) {
        return false;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

}
