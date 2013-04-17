package de.uniol.inf.is.odysseus.probabilistic.function;

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Equals operator for continuous probabilistic values
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticContinuousEqualsOperator extends AbstractProbabilisticBinaryOperator<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 3016679134461973157L;

    @Override
    public int getPrecedence() {
        return 9;
    }

    @Override
    public String getSymbol() {
        return "==";
    }

    @Override
    public Double getValue() {
        ProbabilisticContinuousDouble a = getInputValue(0);
        NormalDistributionMixture mixtures = getDistributions(a.getDistribution());

        Double b = getNumericalInputValue(1);
        double[] lowerBoundData = new double[mixtures.getDimension()];
        Arrays.fill(lowerBoundData, b);
        double[] upperBoundData = new double[mixtures.getDimension()];
        Arrays.fill(upperBoundData, b);

        RealVector lowerBound = MatrixUtils.createRealVector(lowerBoundData);
        RealVector upperBound = MatrixUtils.createRealVector(upperBoundData);

        double value = 0.0;
        mixtures.setScale(mixtures.getScale() * value);
        Interval[] support = new Interval[mixtures.getDimension()];
        for (int i = 0; i < mixtures.getDimension(); i++) {
            double lower = FastMath.max(mixtures.getSupport(i).inf(), lowerBound.getEntry(i));
            double upper = FastMath.min(mixtures.getSupport(i).sup(), upperBound.getEntry(i));
            support[i] = new Interval(lower, upper);
        }
        mixtures.setSupport(support);
        return value;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DOUBLE;
    }

    @Override
    public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(IOperator<Double> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<Double> operator) {
        return false;
    }

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT,
                    SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG }, { SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER, SDFDatatype.FLOAT, SDFDatatype.DOUBLE, SDFDatatype.LONG } };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity() - 1) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return accTypes[argPos];
    }
}
