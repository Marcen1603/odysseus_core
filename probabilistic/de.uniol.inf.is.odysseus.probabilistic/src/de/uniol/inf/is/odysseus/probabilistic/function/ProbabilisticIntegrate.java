package de.uniol.inf.is.odysseus.probabilistic.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class ProbabilisticIntegrate extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long           serialVersionUID = 144107943090837242L;
    public static final SDFDatatype[][] accTypes         = new SDFDatatype[][] {
            { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE },
            { SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX }, { SDFDatatype.VECTOR_DOUBLE },
            { SDFDatatype.VECTOR_DOUBLE }               };

    @Override
    public String getSymbol() {
        return "Integrate";
    }

    @Override
    public Double getValue() {
        ProbabilisticContinuousDouble continuousDouble = (ProbabilisticContinuousDouble) this.getInputValue(0);
        RealVector lowerBound = (RealVector) this.getInputValue(1);
        RealVector upperBound = (RealVector) this.getInputValue(2);
        return cumulativeProbability(continuousDouble, lowerBound, upperBound);
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DOUBLE;
    }

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument: A distribution, a covariance matrix and the lower and upper support.");
        }
        return accTypes[argPos];
    }

    private double cumulativeProbability(final ProbabilisticContinuousDouble distribution, final RealVector lowerBound,
            final RealVector upperBound) {

        double probability = 0.0;
        int dimension = distribution.getDistribution().getDimension();
        if (dimension == 1) {
            for (Entry<NormalDistribution, Double> mixture : distribution.getDistribution().getMixtures().entrySet()) {

                CovarianceMatrix covarianceMatrix = mixture.getKey().getCovarianceMatrix();
                double std = covarianceMatrix.getEntries()[distribution.getDimension()];

                org.apache.commons.math3.distribution.NormalDistribution normalDistribution = new org.apache.commons.math3.distribution.NormalDistribution(
                        mixture.getKey().getMean()[distribution.getDimension()], std);

                probability += mixture.getValue()
                        * normalDistribution.cumulativeProbability(lowerBound.getEntry(distribution.getDimension()),
                                upperBound.getEntry(distribution.getDimension()));
            }
        }
        else {
            // FIXME Re-check code!!!!
            for (Entry<NormalDistribution, Double> mixture : distribution.getDistribution().getMixtures().entrySet()) {
                RealMatrix covarianceMatrix = CovarianceMatrixUtils.toMatrix(mixture.getKey().getCovarianceMatrix());
                RealVector meanVector = MatrixUtils.createRealVector(mixture.getKey().getMean());
                // Perform Alan Genz algorithm for MV
            }

        }
        // Clone Test
        ProbabilisticContinuousDouble a = distribution;
        ProbabilisticContinuousDouble b = new ProbabilisticContinuousDouble(a.getDimension() - 1, a.getDistribution());

        // IDEA: after clone use a MEP function that propagates the distribution among the attributes (little hack!)
        // Use a Probabilistic tuple and overwrite clone method!!
        ProbabilisticContinuousDouble clone = a.clone();
        a.getDistribution().setScale(0.5);

        ProbabilisticContinuousDouble cloneB = new ProbabilisticContinuousDouble(clone.getDimension() - 1,
                clone.getDistribution());

        a.getDistribution().setSupport(a.getDimension(), new Interval(3.0, 5.0));
        clone.getDistribution().setSupport(clone.getDimension(), new Interval(7.0, 8.0));
        System.out.println(clone);
        System.out.println(cloneB);
        System.out.println(a);
        System.out.println(b);
        System.out.println(clone.getDistribution().getSupport());
        System.out.println(cloneB.getDistribution().getSupport());
        System.out.println(a.getDistribution().getSupport());
        System.out.println(b.getDistribution().getSupport());
        assert (clone.getDistribution().getScale() != a.getDistribution().getScale());
        assert (clone.getDistribution().getScale() != b.getDistribution().getScale());
        assert (a.getDistribution().getScale() == b.getDistribution().getScale());
        assert (clone.getDistribution().getScale() == cloneB.getDistribution().getScale());

        assert (!clone.getDistribution().equals(a.getDistribution()));
        assert (!clone.getDistribution().equals(b.getDistribution()));
        assert (a.getDistribution().equals(b.getDistribution()));

        assert (clone.getDistribution().equals(cloneB.getDistribution()));

        System.out.println(clone.getDistribution().getScale() + "!=" + a.getDistribution().getScale() + "=="
                + b.getDistribution().getScale());
        return probability;
    }

    /**
     * Test main
     * 
     * @param args
     */
    public static void main(String[] args) {
        ProbabilisticIntegrate function = new ProbabilisticIntegrate();

        int variate = 4;
        int multi = 4;
        final List<CovarianceMatrix> covarianceMatrixStore = new ArrayList<CovarianceMatrix>(variate);
        for (int i = 0; i < variate; i++) {
            final int size = multi + 6;
            final double[] entries = new double[size];
            for (int j = 0; j < entries.length; j++) {
                entries[j] = j + 1;
            }
            covarianceMatrixStore.add(new CovarianceMatrix(entries));
        }
        ProbabilisticContinuousDouble mixtureDistribution = null;
        final Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>();
        final double[] mean = new double[multi];
        for (int i = 0; i < multi; i++) {
            mean[i] = i;
        }
        for (int i = 0; i < multi; i++) {
            final Integer dimension = i;

            final NormalDistribution distribution = new NormalDistribution(mean, covarianceMatrixStore.get(0));
            mixtures.put(distribution, 1.0 / multi);
        }
        for (int i = 0; i < multi; i++) {
            mixtureDistribution = new ProbabilisticContinuousDouble(i, new NormalDistributionMixture(mixtures));
        }
        Map<String, Serializable> content = new HashMap<String, Serializable>();
        // content.put("covariance_matrix_store", covarianceMatrixStore);
        function.setAdditionalContent(content);
        function.setArguments(
                new Constant<ProbabilisticContinuousDouble>(mixtureDistribution,
                        SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
                new Constant<RealVector>(MatrixUtils.createRealVector(new double[] { 1.0 }), SDFDatatype.VECTOR_DOUBLE),
                new Constant<RealVector>(MatrixUtils.createRealVector(new double[] { 2.0 }), SDFDatatype.VECTOR_DOUBLE));

        Double value = function.getValue();
        System.out.println("Mixture: " + mixtureDistribution);
        System.out.println("CoVariance: " + covarianceMatrixStore);
        for (CovarianceMatrix covarianceMatrix : covarianceMatrixStore) {
            System.out.println("CoVariance Matrix : " + CovarianceMatrixUtils.toMatrix(covarianceMatrix) + " Size: "
                    + covarianceMatrix.size());
        }
        System.out.println(value);
        assert (value == 3.0);
    }
}
