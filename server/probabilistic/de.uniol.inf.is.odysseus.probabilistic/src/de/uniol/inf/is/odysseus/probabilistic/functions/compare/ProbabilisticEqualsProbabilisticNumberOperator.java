/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.functions.compare;

import java.util.Arrays;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
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

    public ProbabilisticContinuousEqualsProbabilisticNumberOperator() {
    	super("==", ACC_TYPES);
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
        Arrays.fill(lowerBound, Double.NEGATIVE_INFINITY);
        System.arraycopy(b.getMean(), 0, lowerBound, 0, b.getMean().length);
        final double[] upperBound = new double[a.getDimension()];
        Arrays.fill(upperBound, Double.POSITIVE_INFINITY);
        System.arraycopy(b.getMean(), 0, lowerBound, 0, b.getMean().length);

        ProbabilisticBooleanResult result = this.getValueInternal(a, lowerBound, upperBound);
        double scale = ((MultivariateMixtureDistribution) result.getDistribution()).getScale();
        // Assume symmetry
        ProbabilisticBooleanResult scaledResult = new ProbabilisticBooleanResult(result.getDistribution(), result.getProbability() * 0.5);
        ((MultivariateMixtureDistribution) scaledResult.getDistribution()).setScale(scale * 0.5);
        return scaledResult;
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
