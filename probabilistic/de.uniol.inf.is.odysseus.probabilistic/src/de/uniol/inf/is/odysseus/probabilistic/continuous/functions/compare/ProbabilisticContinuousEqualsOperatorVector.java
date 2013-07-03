package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.compare;

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticResult;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Equals operator for continuous probabilistic values
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticContinuousEqualsOperatorVector extends AbstractProbabilisticBinaryOperator<ProbabilisticResult> {

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
    public ProbabilisticResult getValue() {
        ProbabilisticContinuousDouble a = getInputValue(0);
        NormalDistributionMixture mixtures = getDistributions(a.getDistribution());

        double[][] b = (double[][]) this.getInputValue(1);
        double[] lowerBoundData = new double[mixtures.getDimension()];
        Arrays.fill(lowerBoundData, Double.NEGATIVE_INFINITY);
        System.arraycopy(b[1], 0, lowerBoundData, 0, b[1].length);
        double[] upperBoundData = new double[mixtures.getDimension()];
        Arrays.fill(upperBoundData, Double.POSITIVE_INFINITY);
        System.arraycopy(b[1], 0, lowerBoundData, 0, b[1].length);

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
        return new ProbabilisticResult(value, mixtures);
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
    public boolean isLeftDistributiveWith(IOperator<ProbabilisticResult> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<ProbabilisticResult> operator) {
        return false;
    }

    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
            { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT,
                    SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG }, { SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT, SDFDatatype.VECTOR_DOUBLE } };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity() - 1) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return ACC_TYPES[argPos];
    }
}
