/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare;

import java.util.Arrays;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMixtureMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousEqualsProbabilisticNumberOperator extends AbstractProbabilisticContinuousCompareOperator {

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
    public final IMultivariateRealDistribution getValue() {
        final ExtendedMixtureMultivariateRealDistribution a = ((ExtendedMixtureMultivariateRealDistribution) this.getInputValue(0)).clone();

        final AbstractProbabilisticValue<?> probabilisticVaue = (AbstractProbabilisticValue<?>) this.getInputValue(1);
        double b = 0.0;
        for (final Entry<?, Double> bEntry : probabilisticVaue.getValues().entrySet()) {
            b += ((Number) bEntry.getKey()).doubleValue() * bEntry.getValue();
        }

        final double[] lowerBoundData = new double[a.getDimension()];
        Arrays.fill(lowerBoundData, b);
        final double[] upperBoundData = new double[a.getDimension()];
        Arrays.fill(upperBoundData, b);

        final RealVector lowerBound = MatrixUtils.createRealVector(lowerBoundData);
        final RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);

        a.setScale(Double.POSITIVE_INFINITY);
        final Interval[] support = new Interval[a.getDimension()];
        for (int i = 0; i < a.getDimension(); i++) {
            final Interval interval = new Interval(lowerBound.getEntry(i), upperBound.getEntry(i));
            support[i] = a.getSupport(i).intersection(interval);
        }
        a.setSupport(support);
        return a;
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
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

   

}
