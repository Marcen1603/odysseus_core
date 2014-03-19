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
public class ProbabilisticGreaterEqualsProbabilisticNumberOperator extends AbstractProbabilisticCompareOperator {

    /**
     * 
     */
    private static final long serialVersionUID = -3834057191681240434L;

    public ProbabilisticContinuousGreaterEqualsProbabilisticNumberOperator() {
    	this(">=");
    }

    protected ProbabilisticContinuousGreaterEqualsProbabilisticNumberOperator(String symbol) {
    	super(symbol,ACC_TYPES);
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

        ProbabilisticBooleanResult result = this.getValueInternal(a, lowerBound, upperBound);
        double scale = ((MultivariateMixtureDistribution) result.getDistribution()).getScale();
        // Assume symmetry
        ProbabilisticBooleanResult scaledResult = new ProbabilisticBooleanResult(result.getDistribution(), result.getProbability() * 0.5);
        ((MultivariateMixtureDistribution) scaledResult.getDistribution()).setScale(scale * 0.5);
        return scaledResult;
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };


}
