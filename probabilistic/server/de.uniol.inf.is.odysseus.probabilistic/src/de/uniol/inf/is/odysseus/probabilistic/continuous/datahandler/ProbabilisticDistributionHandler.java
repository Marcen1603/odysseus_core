/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.continuous.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateEnumeratedDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.Sample;

/**
 * Distribution handler to read and write continuous probabilistic distributions
 * as Gaussian mixtures.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ProbabilisticDistributionHandler extends AbstractDataHandler<MultivariateMixtureDistribution> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticDistributionHandler.class);
    /** The supported data types. */
    private static final List<String> TYPES = new ArrayList<>();

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
     * nio.ByteBuffer)
     */
    @Override
    public final MultivariateMixtureDistribution readData(final ByteBuffer buffer) {
        Objects.requireNonNull(buffer);
        Preconditions.checkArgument(buffer.remaining() >= 4);
        MultivariateMixtureDistribution distributionMixture = null;
        final int size = buffer.getInt();
        if (size > 0) {
            final List<Pair<Double, IMultivariateDistribution>> mixtures = new ArrayList<>();
            final int dimension = buffer.getInt();
            for (int m = 0; m < size; m++) {
                final double weight = buffer.getDouble();
                final double[] mean = new double[dimension];
                for (int i = 0; i < mean.length; i++) {
                    mean[i] = buffer.getDouble();
                }
                final double[] entries = new double[CovarianceMatrixUtils.getCovarianceTriangleSizeFromDimension(dimension)];
                for (int i = 0; i < entries.length; i++) {
                    entries[i] = buffer.getDouble();
                }
                try {
                    final IMultivariateDistribution distribution = new MultivariateNormalDistribution(mean, CovarianceMatrixUtils.toMatrix(entries).getData());
                    mixtures.add(new Pair<>(weight, distribution));
                } catch (final Exception e) {
                    ProbabilisticDistributionHandler.LOG.warn(e.getMessage(), e);
                }

            }
            final double scale = buffer.getDouble();
            final Interval[] support = new Interval[dimension];
            for (int i = 0; i < support.length; i++) {
                support[i] = new Interval(buffer.getDouble(), buffer.getDouble());
            }

            distributionMixture = new MultivariateMixtureDistribution(mixtures);
            distributionMixture.setScale(scale);
            distributionMixture.setSupport(support);
        }
        return distributionMixture;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#readData(java.
     * lang.String)
     */
    @Override
    public final MultivariateMixtureDistribution readData(final String string) {
        Objects.requireNonNull(string);
        Preconditions.checkArgument(!string.isEmpty());
        final List<Pair<Double, IMultivariateDistribution>> distributions = new ArrayList<>();

        double scale = 1.0;
        Interval[] support = null;

        boolean readScale = true;
        boolean readWeight = false;
        boolean readSupport = false;
        boolean continuous = false;
        boolean inFunction = false;
        double weight = 0.0;
        IMultivariateDistribution distribution = null;

        int startIndex = 0;
        int index = 0;
        for (int i = 0; i < string.length(); i++) {
            final String token = string.substring(i, i + 1);
            if ((token.equals("D")) || (token.equals("N"))) {
                if (!inFunction) {
                    inFunction = true;
                    if (readScale) {
                        readScale = false;
                        scale = Double.parseDouble(string.substring(0, i));
                    }
                    if ((token.equalsIgnoreCase("D"))) {
                        continuous = false;
                    } else {
                        continuous = true;
                    }
                }
            }
            if (token.equals("(")) {
                startIndex = index + 1;
            }
            if (token.equals(")")) {
                if (continuous) {
                    distribution = readContinuousDistribution(string.substring(startIndex, i));
                } else {
                    distribution = readDiscreteDistribution(string.substring(startIndex, i));
                }
                if (inFunction) {
                    inFunction = false;
                }
                readWeight = true;
            }
            if (token.equals(":")) {
                readWeight = true;
                startIndex = index + 1;
            }
            if (token.equals(",")) {
                if (readWeight) {
                    readWeight = false;
                    weight = Double.parseDouble(string.substring(startIndex, i));
                    distributions.add(new Pair<>(weight, distribution));
                }
            }
            if (token.equals("[")) {
                if (readWeight) {
                    readWeight = false;
                    weight = Double.parseDouble(string.substring(startIndex, i));
                    distributions.add(new Pair<>(weight, distribution));

                    readSupport = true;
                    startIndex = i + 1;
                }
            }
            if (token.equals(">")) {
                if (readSupport) {
                    readSupport = false;
                    support = readSupport(string.substring(startIndex, i - 2));
                }
            }
            if (token.equals(">")) {
                startIndex = i + 1;
            }
            index++;
        }
        final int[] reference = readReference(string.substring(startIndex));

        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(distributions);
        mixture.setScale(scale);
        mixture.setAttributes(reference);
        if (support != null) {
            mixture.setSupport(support);
        }
        return mixture;
    }

    private IMultivariateDistribution readContinuousDistribution(final String string) {
        final List<double[]> variance = new LinkedList<>();
        double[] mean = null;
        boolean readMean = true;
        boolean row = false;
        int rows = 0;
        int beginIndex = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.substring(i, i + 1).equals("[")) {
                if (readMean) {
                    beginIndex = i + 1;
                } else {
                    if (row) {
                        row = false;
                    } else {
                        beginIndex = i + 1;
                    }
                }
            }
            if (string.substring(i, i + 1).equals("]")) {
                if (readMean) {
                    final String[] parameter = string.substring(beginIndex, i).split(",");
                    mean = new double[parameter.length];
                    for (int j = 0; j < parameter.length; j++) {
                        mean[j] = Double.parseDouble(parameter[j]);
                    }
                    readMean = false;
                    rows = mean.length;
                    row = true;
                } else {
                    if (!row) {
                        final String[] parameter = string.substring(beginIndex, i).split(",");
                        final double[] varianceEntry = new double[parameter.length];
                        for (int j = 0; j < parameter.length; j++) {
                            varianceEntry[j] = Double.parseDouble(parameter[j]);
                        }
                        variance.add(varianceEntry);
                        rows--;
                        if (rows == 0) {
                            row = true;
                        }
                    }
                }
            }

        }

        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(mean, variance.toArray(new double[variance.size()][variance.get(0).length]));
        return distribution;
    }

    private IMultivariateDistribution readDiscreteDistribution(final String string) {
        final List<Sample> samples = new ArrayList<>();
        double[] sample = null;
        double probability = 0.0;

        boolean readSample = true;
        boolean readProbability = false;
        int beginIndex = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.substring(i, i + 1).equals("[")) {
                beginIndex = i + 1;
                readSample = true;
            }
            if (string.substring(i, i + 1).equals("]")) {
                final String[] parameter = string.substring(beginIndex, i).split(",");
                sample = new double[parameter.length];
                for (int j = 0; j < parameter.length; j++) {
                    sample[j] = Double.parseDouble(parameter[j]);
                }
                readSample = false;
            }
            if (string.substring(i, i + 1).equals(",")) {
                if (!readSample) {
                    if (readProbability) {
                        readProbability = false;
                        probability = Double.parseDouble(string.substring(beginIndex, i));
                        samples.add(new Sample(sample, probability));
                    } else {
                        readProbability = true;
                        beginIndex = i + 1;
                    }
                }
            }
        }
        if (readProbability) {
            probability = Double.parseDouble(string.substring(beginIndex));
            samples.add(new Sample(sample, probability));
        }
        final MultivariateEnumeratedDistribution distribution = new MultivariateEnumeratedDistribution(samples);
        return distribution;
    }

    private Interval[] readSupport(final String string) {
        final List<Interval> support = new LinkedList<>();
        int beginIndex = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.substring(i, i + 1).equals("[")) {
                beginIndex = i + 1;
            }
            if (string.substring(i, i + 1).equals("]")) {
                final String[] parameter = string.substring(beginIndex, i).split(",");
                final String infString = parameter[0];
                final String supString = parameter[1];
                double inf = 0.0;
                double sup = 0.0;
                if (infString.equalsIgnoreCase("-oo")) {
                    inf = Double.NEGATIVE_INFINITY;
                } else {
                    inf = Double.parseDouble(infString);
                }
                if (supString.equalsIgnoreCase("oo")) {
                    sup = Double.POSITIVE_INFINITY;
                } else {
                    sup = Double.parseDouble(supString);
                }
                support.add(new Interval(inf, sup));
            }
        }
        return support.toArray(new Interval[support.size()]);
    }

    private int[] readReference(final String string) {
        final String[] parameter = string.substring(1, string.length() - 1).split(",");
        final int[] ref = new int[parameter.length];
        for (int i = 0; i < parameter.length; i++) {
            ref[i] = Integer.parseInt(parameter[i].trim());
        }
        return ref;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#writeData(java
     * .nio.ByteBuffer, java.lang.Object)
     */
    @Override
    public final void writeData(final ByteBuffer buffer, final Object data) {
        Objects.requireNonNull(buffer);
        Objects.requireNonNull(data);
        final MultivariateMixtureDistribution value = (MultivariateMixtureDistribution) data;
        buffer.putInt(value.getComponents().size());
        buffer.putInt(value.getDimension());

        for (final Pair<Double, IMultivariateDistribution> entry : value.getComponents()) {
            buffer.putDouble(entry.getKey());
            final double[] mean = entry.getValue().getMean();
            for (final double element : mean) {
                buffer.putDouble(element);
            }
            final double[] entries = CovarianceMatrixUtils.fromMatrix(new Array2DRowRealMatrix(entry.getValue().getVariance()));
            for (final double entrie : entries) {
                buffer.putDouble(entrie);
            }
        }
        buffer.putDouble(value.getScale());

        final Interval[] support = value.getSupport();
        for (final Interval element : support) {
            buffer.putDouble(element.inf());
            buffer.putDouble(element.sup());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.IDataHandler#memSize(java.lang
     * .Object)
     */
    @Override
    public final int memSize(final Object attribute) {
        Objects.requireNonNull(attribute);
        final MultivariateMixtureDistribution value = (MultivariateMixtureDistribution) attribute;
        final int numberOfMixtures = value.getComponents().size();
        final int dimension = value.getDimension();
        final int covarianceMatrixSize = (int) (-0.5 + Math.sqrt(0.25 + (dimension * 2)));
        // Number of mixtures: 1
        // Dimension: 1
        // Probability for each mixtue: 1 * numberOfMixtures
        // Mean of distribution: dimension * numberOfMixtures
        // Covariance matrix: numberOfMixtures * covarianceMatrixSize
        // Scale: 1
        // Support for each dimension: dimension * 2
        return ((2 * Integer.SIZE) + (Double.SIZE * (numberOfMixtures + (numberOfMixtures * dimension) + (numberOfMixtures * covarianceMatrixSize) + 1 + (2 * dimension)))) / 8;

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#getInstance
     * (de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
     */
    @Override
    protected final IDataHandler<MultivariateMixtureDistribution> getInstance(final SDFSchema schema) {
        return new ProbabilisticDistributionHandler();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler#
     * getSupportedDataTypes()
     */
    @Override
    public final List<String> getSupportedDataTypes() {
        return Collections.unmodifiableList(ProbabilisticDistributionHandler.TYPES);
    }

    @Override
    public final Class<?> createsType() {
        return MultivariateMixtureDistribution.class;
    }

    public static void main(final String[] args) {
        final String discreteDistributionString1 = "1.0D([-198.0],0.81):1.0[[-oo,oo]]->[4]";
        final String discreteDistributionString2 = "1.0D([-28.0, -28.0],0.9):1.0[[-oo,oo], [-oo,oo]]->[0, 1]";
        final String discreteDistributionString3 = "1.0D([NaN],0.81):1.0[[0.0,0.0]]->[3]";
        final String discreteDistributionString4 = "1.0N([-99.0, -99.0],[[1.0, 0.5],[0.5, 1.0]]):1.0[[-oo,oo], [-oo,oo]]->[1, 2]";
        final String continuousDistributionString1 = "1.0N([-67.0, -67.0],[[1.0, 0.5],[0.5, 1.0]]):1.0[[-oo,oo], [-oo,oo]]->[0, 1]";
        final String continuousDistributionString2 = "1.0N(-99.0,1.0):1.0[[-oo,oo]]->[0]";
        final String continuousDistributionString3 = "1.0N([-99.0, -99.0],[[1.0, 0.5],[0.5, 1.0]]):1.0[[-oo,0.0], [-oo,oo]]->[1, 2]";
        final String continuousDistributionString4 = "1.0N([-99.0, -99.0],[[1.0, 0.5],[0.5, 1.0]]):1.0[[-oo,oo], [-oo,oo]]->[1, 2]";
        final String discreteDistributionString5 = "1.0D([3.0, 3.0],0.9):1.0[[-oo,oo], [-oo,oo]]->[1, 2]|1.1111111111111112D([3.0, 3.0],0.9):1.0[[-oo,3.0], [-oo,oo]]->[4, 5]|1.1111111111111112D([2.0, 2.0],0.9):1.0[[-oo,2.0], [-oo,oo]]->[7, 8]";
        final ProbabilisticDistributionHandler dataHandler = new ProbabilisticDistributionHandler();
        System.out.println(dataHandler.readData(discreteDistributionString1));
        System.out.println(dataHandler.readData(discreteDistributionString2));
        System.out.println(dataHandler.readData(discreteDistributionString3));
        System.out.println(dataHandler.readData(discreteDistributionString4));
        System.out.println(dataHandler.readData(discreteDistributionString5));
        System.out.println(dataHandler.readData(continuousDistributionString1));
        System.out.println(dataHandler.readData(continuousDistributionString2));
        System.out.println(dataHandler.readData(continuousDistributionString3));
        System.out.println(dataHandler.readData(continuousDistributionString4));
    }
}
