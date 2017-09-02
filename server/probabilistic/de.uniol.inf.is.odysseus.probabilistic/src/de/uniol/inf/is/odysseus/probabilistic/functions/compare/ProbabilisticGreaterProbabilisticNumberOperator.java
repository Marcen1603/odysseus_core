/**
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.functions.compare;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ProbabilisticGreaterProbabilisticNumberOperator extends AbstractProbabilisticCompareOperator {

    /**
     *
     */

    private static final long serialVersionUID = 3981025281707408907L;
    private final boolean leftInclusive;
    private final boolean rightInclusive;

    public ProbabilisticGreaterProbabilisticNumberOperator() {
        this(">", false, true);
    }

    protected ProbabilisticGreaterProbabilisticNumberOperator(final String symbol, final boolean leftInclusive, final boolean rightInclusive) {
        super(symbol, ProbabilisticGreaterProbabilisticNumberOperator.ACC_TYPES);
        this.leftInclusive = leftInclusive;
        this.rightInclusive = rightInclusive;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final int getPrecedence() {
        return 8;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final ProbabilisticBooleanResult getValue() {
        final MultivariateMixtureDistribution a = ((MultivariateMixtureDistribution) this.getInputValue(0)).clone();
        final MultivariateMixtureDistribution b = ((MultivariateMixtureDistribution) this.getInputValue(1)).clone();

        final double[] lowerBound = new double[a.getDimension()];
        Arrays.fill(lowerBound, Double.NEGATIVE_INFINITY);
        System.arraycopy(b.getMean(), 0, lowerBound, 0, b.getMean().length);
        final double[] upperBound = new double[a.getDimension()];
        Arrays.fill(upperBound, Double.POSITIVE_INFINITY);

        final ProbabilisticBooleanResult result = this.getValueInternal(a, lowerBound, upperBound, this.leftInclusive, this.rightInclusive);
        final double scale = ((MultivariateMixtureDistribution) result.getDistribution()).getScale();
        // Assume symmetry
        final ProbabilisticBooleanResult scaledResult = new ProbabilisticBooleanResult(result.getDistribution(), result.getProbability() );
        ((MultivariateMixtureDistribution) scaledResult.getDistribution()).setScale(scale * 0.5);
        return scaledResult;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

}
