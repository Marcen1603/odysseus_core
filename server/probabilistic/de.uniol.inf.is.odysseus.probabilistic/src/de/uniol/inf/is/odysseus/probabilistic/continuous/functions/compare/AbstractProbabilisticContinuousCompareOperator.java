/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare;

import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
abstract public class AbstractProbabilisticContinuousCompareOperator extends AbstractProbabilisticBinaryOperator<IMultivariateDistribution> {

    /**
     * 
     */
    private static final long serialVersionUID = -7850744519118122850L;

   public AbstractProbabilisticContinuousCompareOperator(String symbol,SDFDatatype[][] accTypes) {
    	super(symbol,accTypes, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
    }

    protected final IMultivariateDistribution getValueInternal(final MultivariateMixtureDistribution a, final RealVector lowerBound, final RealVector upperBound) {
        final double value = a.probability(lowerBound.toArray(), upperBound.toArray());
        a.setScale(a.getScale() / value);
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
    public final boolean isLeftDistributiveWith(final IOperator<IMultivariateDistribution> operator) {
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean isRightDistributiveWith(final IOperator<IMultivariateDistribution> operator) {
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
