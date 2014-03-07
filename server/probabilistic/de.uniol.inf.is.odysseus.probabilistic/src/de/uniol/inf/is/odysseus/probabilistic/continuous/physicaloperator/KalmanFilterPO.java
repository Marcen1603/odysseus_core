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

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3_patch.filter.KalmanFilterPatched;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 *         FIXME not fully working, have to handle the input and output right.
 *         Also no prediction is implemented yet
 * @param <T>
 */
public class KalmanFilterPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** The attribute positions. */
    private final int[] attributes;
    /** The process model. */
    private final ProcessModel processModel;
    /** The measurement model. */
    private final MeasurementModel measurementModel;
    /** The Kalman filter. */
    private final KalmanFilterPatched filter;

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
            final double[][] measurementNoise) {
        this.attributes = attributes;
        if (control != null) {
            this.processModel = new DefaultProcessModel(new Array2DRowRealMatrix(stateTransition), new Array2DRowRealMatrix(control), new Array2DRowRealMatrix(processNoise), null, null);
        }
        else {
            this.processModel = new DefaultProcessModel(new Array2DRowRealMatrix(stateTransition), null, new Array2DRowRealMatrix(processNoise), null, null);
        }
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
        final NormalDistributionMixture[] distributions = object.getDistributions();
        final ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(new Object[object.getAttributes().length + 2], new NormalDistributionMixture[distributions.length],
                object.requiresDeepClone());
        System.arraycopy(distributions, 0, outputVal.getDistributions(), 0, distributions.length);
        System.arraycopy(object.getAttributes(), 0, outputVal.getAttributes(), 0, object.getAttributes().length);

        this.filter.predict();

        final double[] value = new double[this.attributes.length];
        for (int i = 0; i < this.attributes.length; i++) {
            value[i] = ((Number) object.getAttribute(this.attributes[i])).doubleValue();
        }
        this.filter.correct(value);

        RealVector state = this.filter.getStateEstimationVector();

        RealMatrix covariance = this.filter.getErrorCovarianceMatrix();
        outputVal.setAttribute(object.getAttributes().length, state.toArray());
        outputVal.setAttribute(object.getAttributes().length + 1, covariance.getData());
        outputVal.setMetadata((T) object.getMetadata().clone());

        // KTHXBYE
        this.transfer(outputVal);

        // try {
        // final List<Pair<Double, MultivariateNormalDistribution>> mvns = new
        // ArrayList<Pair<Double, MultivariateNormalDistribution>>();
        // final MultivariateNormalDistribution component = new
        // MultivariateNormalDistribution(state.toArray(),
        // covariance.getData());
        // mvns.add(new Pair<Double, MultivariateNormalDistribution>(1.0,
        // component));
        //
        // final NormalDistributionMixture mixture = new
        // NormalDistributionMixture(mvns);
        // mixture.setAttributes(this.attributes);
        //
        // final NormalDistributionMixture[] outputValDistributions = new
        // NormalDistributionMixture[distributions.length + 1];
        //
        // for (final int attribute : this.attributes) {
        // outputVal.setAttribute(attribute, new
        // ProbabilisticContinuousDouble(distributions.length));
        // }
        // // Copy the old distribution to the new tuple
        // System.arraycopy(distributions, 0, outputValDistributions, 0,
        // distributions.length);
        // // And append the new distribution to the end of the array
        // outputValDistributions[distributions.length] = mixture;
        // outputVal.setDistributions(outputValDistributions);
        //
        // // KTHXBYE
        // this.transfer(outputVal);
        // }
        // catch (SingularMatrixException | NonPositiveDefiniteMatrixException
        // e) {
        // System.out.println(e.getClass());
        // System.out.println(state);
        // System.out.println(covariance);
        // }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
        return new KalmanFilterPO<>(this);
    }

}
