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
    private final boolean inclusive;

    public ProbabilisticGreaterProbabilisticNumberOperator() {
        this(">", false);
    }

    protected ProbabilisticGreaterProbabilisticNumberOperator(final String symbol, final boolean inclusive) {
        super(symbol, ProbabilisticGreaterProbabilisticNumberOperator.ACC_TYPES);
        this.inclusive = inclusive;
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
        if (!inclusive) {
            Arrays.fill(lowerBound, Double.MIN_VALUE);
        } else {
            Arrays.fill(lowerBound, 0.0);
        }
        final double[] upperBound = new double[a.getDimension()];
        Arrays.fill(upperBound, Double.POSITIVE_INFINITY);
        return this.getValueInternal(a, b, lowerBound, upperBound);
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

}
