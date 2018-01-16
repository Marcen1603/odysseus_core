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
public class ProbabilisticSmallerProbabilisticNumberOperator extends AbstractProbabilisticCompareOperator {

    /**
     *
     */
    private static final long serialVersionUID = 9197974946216206256L;
    private final boolean inclusive;

    public ProbabilisticSmallerProbabilisticNumberOperator() {
        this("<", false);
    }

    public ProbabilisticSmallerProbabilisticNumberOperator(final String symbol, final boolean inclusive) {
        super(symbol, ProbabilisticSmallerProbabilisticNumberOperator.ACC_TYPES);
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
        Arrays.fill(lowerBound, Double.NEGATIVE_INFINITY);
        final double[] upperBound = new double[a.getDimension()];
        if (!inclusive) {
            Arrays.fill(upperBound, -Double.MIN_VALUE);
        } else {
            Arrays.fill(upperBound, 0.0);
        }
        return this.getValueInternal(a, b, lowerBound, upperBound);

    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

}
