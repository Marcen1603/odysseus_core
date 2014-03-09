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
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3_patch.filter.KalmanFilterPatched;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class KalmanFilterPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(KalmanFilterPO.class);

    /** The attribute positions. */
    private final int[] attributes;
    /** The process model. */
    private final ProcessModel processModel;
    /** The measurement model. */
    private final MeasurementModel measurementModel;
    /** The Kalman filter. */
    private final KalmanFilterPatched filter;
    private RealMatrix errorCovariance;

    /**
     * 
     * Creates a new Kalman filter operator.
     * 
     * 
     * @param attributes
     *            The list of attributes
     * @param stateTransition
     *            The state transition matrix
     * @param control
     *            The control matrix
     * @param processNoise
     *            The process noise matrix
     * @param measurement
     *            The measurement matrix
     * @param measurementNoise
     *            The measurement noise matrix
     */
    public KalmanFilterPO(final int[] attributes, final double[][] stateTransition, final double[][] control, final double[][] processNoise, final double[][] measurement,
            final double[][] measurementNoise, final double[] initialState, final double[][] initialError) {
        this.attributes = attributes;
        Array2DRowRealMatrix controlMatrix = null;
        RealVector initialStateMatrix = null;
        Array2DRowRealMatrix initialErrorMatrix = null;
        if (control != null) {
            controlMatrix = new Array2DRowRealMatrix(control);
        }
        if (initialState != null) {
            initialStateMatrix = new ArrayRealVector(initialState);
        }
        if (initialError != null) {
            initialErrorMatrix = new Array2DRowRealMatrix(initialError);
        }
        this.processModel = new DefaultProcessModel(new Array2DRowRealMatrix(stateTransition), controlMatrix, new Array2DRowRealMatrix(processNoise), initialStateMatrix, initialErrorMatrix);

        this.measurementModel = new DefaultMeasurementModel(measurement, measurementNoise);
        this.filter = new KalmanFilterPatched(this.processModel, this.measurementModel);
    }

    /**
     * Clone constructor.
     * 
     * @param kalmanPO
     *            The copy
     */
    public KalmanFilterPO(final KalmanFilterPO<T> kalmanPO) {
        super(kalmanPO);
        this.attributes = kalmanPO.attributes.clone();
        RealMatrix measurementMatrix = null;
        RealMatrix measurementNoise = null;
        if (kalmanPO.measurementModel.getMeasurementMatrix() != null) {
            measurementMatrix = kalmanPO.measurementModel.getMeasurementMatrix().copy();
        }
        if (kalmanPO.measurementModel.getMeasurementNoise() != null) {
            measurementNoise = kalmanPO.measurementModel.getMeasurementNoise().copy();
        }
        this.measurementModel = new DefaultMeasurementModel(measurementMatrix, measurementNoise);
        RealMatrix controlMatrix = null;
        RealVector initialStateEstimate = null;
        RealMatrix initialErrorCovariance = null;
        if (kalmanPO.processModel.getControlMatrix() != null) {
            controlMatrix = kalmanPO.processModel.getControlMatrix().copy();
        }
        if (kalmanPO.processModel.getInitialStateEstimate() != null) {
            initialStateEstimate = kalmanPO.processModel.getInitialStateEstimate().copy();
        }
        if (kalmanPO.processModel.getInitialErrorCovariance() != null) {
            initialErrorCovariance = kalmanPO.processModel.getInitialErrorCovariance().copy();
        }
        this.processModel = new DefaultProcessModel(kalmanPO.processModel.getStateTransitionMatrix().copy(), controlMatrix, kalmanPO.processModel.getProcessNoise().copy(), initialStateEstimate,
                initialErrorCovariance);
        this.filter = new KalmanFilterPatched(this.processModel, this.measurementModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final AbstractPipe.OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
        final NormalDistributionMixture[] inputDistributions = object.getDistributions();
        final Object[] inputAttributes = object.getAttributes();

        this.filter.predict();

        final double[] value = new double[this.attributes.length];
        for (int i = 0; i < this.attributes.length; i++) {
            value[i] = ((Number) object.getAttribute(this.attributes[i])).doubleValue();
        }
        this.filter.correct(value);

        final double[] state = this.filter.getStateEstimationVector().toArray();
        final double[][] covariance = this.filter.getErrorCovarianceMatrix().getData();
        final NormalDistributionMixture[] outputDistributions = new NormalDistributionMixture[inputDistributions.length + 1];
        final Object[] outputAttributes = new Object[inputAttributes.length + state.length];
        // Copy the old distributions to the new tuple
        System.arraycopy(inputDistributions, 0, outputDistributions, 0, inputDistributions.length);
        // Copy the old attributes to the new tuple
        System.arraycopy(inputAttributes, 0, outputAttributes, 0, inputAttributes.length);
        // And append the new references
        final int[] outputAttributePositions = new int[state.length];
        for (int i = 0; i < state.length; i++) {
            outputAttributes[inputAttributes.length + i] = new ProbabilisticContinuousDouble(inputDistributions.length);
            outputAttributePositions[i] = inputAttributes.length + i;
        }
        final List<Pair<Double, MultivariateNormalDistribution>> mvns = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
        MultivariateNormalDistribution component;
        try {
            component = new MultivariateNormalDistribution(state, covariance);
            this.errorCovariance = this.filter.getErrorCovarianceMatrix().copy();
        }
        catch (SingularMatrixException | NonPositiveDefiniteMatrixException | MathUnsupportedOperationException e) {
            if (LOG.isTraceEnabled()) {
                KalmanFilterPO.LOG.trace(e.getMessage() + ": " + this.filter.getErrorCovarianceMatrix(), e);
            }
            // Take the last estimated covariance
            component = new MultivariateNormalDistribution(state, errorCovariance.getData());
        }
        mvns.add(new Pair<Double, MultivariateNormalDistribution>(1.0, component));

        final NormalDistributionMixture mixture = new NormalDistributionMixture(mvns);

        // And append the new distribution to the end of the array
        outputDistributions[inputDistributions.length] = mixture;

        mixture.setAttributes(outputAttributePositions);
        final ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<>(outputAttributes, outputDistributions, true);
        outputVal.setMetadata((T) object.getMetadata().clone());

        // KTHXBYE
        this.transfer(outputVal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
        return new KalmanFilterPO<>(this);
    }

}
