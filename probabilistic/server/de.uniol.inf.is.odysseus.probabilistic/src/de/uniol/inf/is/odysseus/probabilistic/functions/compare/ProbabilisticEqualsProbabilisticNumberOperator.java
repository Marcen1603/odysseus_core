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
public class ProbabilisticEqualsProbabilisticNumberOperator extends AbstractProbabilisticCompareOperator {

    /**
     *
     */
    private static final long serialVersionUID = 7656616835285616813L;
    @SuppressWarnings("unused")
	private final boolean leftInclusive;
    @SuppressWarnings("unused")
	private final boolean rightInclusive;

    public ProbabilisticEqualsProbabilisticNumberOperator() {
        super("==", ProbabilisticEqualsProbabilisticNumberOperator.ACC_TYPES);
        this.leftInclusive = true;
        this.rightInclusive = true;
    }

    /*
     *
     * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
     */
    @Override
    public final ProbabilisticBooleanResult getValue() {
        final MultivariateMixtureDistribution a = ((MultivariateMixtureDistribution) this.getInputValue(0)).clone();
        final MultivariateMixtureDistribution b = ((MultivariateMixtureDistribution) this.getInputValue(1)).clone();

        final double[] lowerBound = new double[a.getDimension()];
        Arrays.fill(lowerBound, 0.0);
        final double[] upperBound = new double[a.getDimension()];
        Arrays.fill(upperBound, 0.0);
        return this.getValueInternal(a, b, lowerBound, upperBound);

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int getPrecedence() {
        return 9;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

}
