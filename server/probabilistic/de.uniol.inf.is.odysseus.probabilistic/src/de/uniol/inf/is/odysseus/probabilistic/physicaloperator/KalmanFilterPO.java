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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class KalmanFilterPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(KalmanFilterPO.class);
    private final LinkedList<ProbabilisticTuple<T>> lastObjects = new LinkedList<>();
    /** The attribute positions. */
    private final int[] attributes;
    /** The process model. */
    private final ProcessModel processModel;
    /** The measurement model. */
    private final MeasurementModel measurementModel;
    /** The Kalman filter. */
    private final KalmanFilterPatched filter;
    /** The last evaluated covariance. */
    private RealMatrix errorCovariance;
    private final double[][] stateTransitionRef;
    private final double[][] processNoiseRef;
    private final double[][] controlRef;
    private final double[][] measurementNoiseRef;
    private final double[][] measurementRef;

    private final SDFExpression stateTransitionExpression;
    private final SDFExpression processNoiseExpression;
    private final SDFExpression controlExpression;
    private final SDFExpression measurementNoiseExpression;
    private final SDFExpression measurementExpression;
    private int maxHistoryElements;

    private VarHelper[] processNoiseVariables;
    private VarHelper[] stateTransitionVariables;
    private VarHelper[] controlVariables;
    private VarHelper[] measurementNoiseVariables;
    private VarHelper[] measurementVariables;
    private final SDFSchema schema;

    /**
     * 
     * Creates a new Kalman filter operator.
     * 
     * @param schema
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
    public KalmanFilterPO(final SDFSchema schema, final int[] attributes, final SDFExpression stateTransitionExpression, final SDFExpression controlExpression,
            final SDFExpression processNoiseExpression, final SDFExpression measurementExpression, final SDFExpression measurementNoiseExpression, final double[] initialState,
            final double[][] initialError) {
        this.schema = schema;
        this.attributes = attributes;
        Array2DRowRealMatrix controlMatrix = null;
        RealVector initialStateMatrix = null;
        Array2DRowRealMatrix initialErrorMatrix = null;
        if (controlExpression != null) {
            this.controlExpression = controlExpression;
            controlMatrix = new Array2DRowRealMatrix(this.initialEvaluateExpression(this.controlExpression));
            this.controlRef = controlMatrix.getDataRef();
        }
        else {
            this.controlExpression = null;
            this.controlRef = null;
        }
        if (initialState != null) {
            initialStateMatrix = new ArrayRealVector(initialState);
        }
        if (initialError != null) {
            initialErrorMatrix = new Array2DRowRealMatrix(initialError);
        }
        this.stateTransitionExpression = stateTransitionExpression;
        final Array2DRowRealMatrix stateTransitionMatrix = new Array2DRowRealMatrix(this.initialEvaluateExpression(this.stateTransitionExpression));
        this.stateTransitionRef = stateTransitionMatrix.getDataRef();

        this.processNoiseExpression = processNoiseExpression;
        final Array2DRowRealMatrix processNoiseMatrix = new Array2DRowRealMatrix(this.initialEvaluateExpression(this.processNoiseExpression));
        this.processNoiseRef = processNoiseMatrix.getDataRef();

        this.measurementExpression = measurementExpression;
        final Array2DRowRealMatrix measurementeMatrix = new Array2DRowRealMatrix(this.initialEvaluateExpression(this.measurementExpression));
        this.measurementRef = measurementeMatrix.getDataRef();

        this.measurementNoiseExpression = measurementNoiseExpression;
        final Array2DRowRealMatrix measurementNoiseMatrix = new Array2DRowRealMatrix(this.initialEvaluateExpression(this.measurementNoiseExpression));
        this.measurementNoiseRef = measurementNoiseMatrix.getDataRef();

        this.processModel = new DefaultProcessModel(stateTransitionMatrix, controlMatrix, processNoiseMatrix, initialStateMatrix, initialErrorMatrix);
        this.measurementModel = new DefaultMeasurementModel(measurementeMatrix, measurementNoiseMatrix);
        this.initStateTransition(schema, stateTransitionExpression);
        if (controlExpression != null) {
            this.initControl(schema, controlExpression);
        }
        this.initProcessNoise(schema, processNoiseExpression);
        this.initMeasurementNoise(schema, measurementNoiseExpression);
        this.initMeasurement(schema, measurementExpression);

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
        this.schema = kalmanPO.schema.clone();
        this.attributes = kalmanPO.attributes.clone();

        RealVector initialStateEstimate = null;
        RealMatrix initialErrorCovariance = null;
        Array2DRowRealMatrix controlMatrix = null;
        if (kalmanPO.processModel.getControlMatrix() != null) {
            this.controlExpression = kalmanPO.controlExpression.clone();
            controlMatrix = new Array2DRowRealMatrix(kalmanPO.processModel.getControlMatrix().getData());
            this.controlRef = controlMatrix.getDataRef();
        }
        else {
            this.controlExpression = null;
            this.controlRef = null;
        }
        if (kalmanPO.processModel.getInitialStateEstimate() != null) {
            initialStateEstimate = kalmanPO.processModel.getInitialStateEstimate().copy();
        }
        if (kalmanPO.processModel.getInitialErrorCovariance() != null) {
            initialErrorCovariance = kalmanPO.processModel.getInitialErrorCovariance().copy();
        }
        this.stateTransitionExpression = kalmanPO.stateTransitionExpression;
        final Array2DRowRealMatrix stateTransitionMatrix = new Array2DRowRealMatrix(kalmanPO.processModel.getStateTransitionMatrix().getData());
        this.stateTransitionRef = stateTransitionMatrix.getDataRef();

        this.processNoiseExpression = kalmanPO.processNoiseExpression;
        final Array2DRowRealMatrix processNoiseMatrix = new Array2DRowRealMatrix(kalmanPO.processModel.getProcessNoise().getData());
        this.processNoiseRef = processNoiseMatrix.getDataRef();

        this.measurementExpression = kalmanPO.measurementExpression;
        final Array2DRowRealMatrix measurementMatrix = new Array2DRowRealMatrix(kalmanPO.measurementModel.getMeasurementMatrix().getData());
        this.measurementRef = measurementMatrix.getDataRef();

        this.measurementNoiseExpression = kalmanPO.measurementNoiseExpression;
        final Array2DRowRealMatrix measurementNoiseMatrix = new Array2DRowRealMatrix(kalmanPO.measurementModel.getMeasurementNoise().getData());
        this.measurementNoiseRef = measurementNoiseMatrix.getDataRef();

        this.measurementModel = new DefaultMeasurementModel(measurementMatrix, measurementNoiseMatrix);
        this.processModel = new DefaultProcessModel(stateTransitionMatrix, controlMatrix, processNoiseMatrix, initialStateEstimate, initialErrorCovariance);
        this.initStateTransition(kalmanPO.schema, this.stateTransitionExpression);
        if (this.controlExpression != null) {
            this.initControl(kalmanPO.schema, this.controlExpression);
        }
        this.initProcessNoise(kalmanPO.schema, this.processNoiseExpression);
        this.initMeasurementNoise(kalmanPO.schema, this.measurementNoiseExpression);
        this.initMeasurement(kalmanPO.schema, this.measurementExpression);

        this.filter = new KalmanFilterPatched(this.processModel, this.measurementModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final AbstractPipe.OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    private ProbabilisticTuple<T> getObject(final ProbabilisticTuple<T> object, final VarHelper var) {
        ProbabilisticTuple<T> obj = object;
        if (this.lastObjects.size() > var.objectPosToUse) {
            obj = this.lastObjects.get(var.objectPosToUse);
        }
        return obj;
    }

    private void update(final ProbabilisticTuple<T> object, final VarHelper[] variables, final SDFExpression expression, final double[][] ref) {
        final Object[] values = new Object[variables.length];
//        final IMetaAttribute[] meta = new IMetaAttribute[variables.length];
        for (int j = 0; j < variables.length; ++j) {
            final ProbabilisticTuple<T> obj = this.getObject(object, variables[j]);
            if (obj != null) {
                values[j] = obj.getAttribute(variables[j].pos);
//                meta[j] = obj.getMetadata();
            }
        }
        try {
//            expression.bindMetaAttribute(object.getMetadata());
//            expression.bindAdditionalContent(object.getAdditionalContent());
            expression.bindVariables(values);
            final double[][] out = expression.getValue();
            for (int i = 0; i < ref.length; i++) {
                System.arraycopy(out[i], 0, ref[i], 0, out[i].length);
            }
        }
        catch (final Exception e) {
            KalmanFilterPO.LOG.error(String.format("Unable to update %s with %s because of %s", expression, object, e.getMessage()), e);
        }
    }

    private double[][] initialEvaluateExpression(final SDFExpression expression) {
        if (!expression.getMEPExpression().isConstant()) {
            final List<SDFAttribute> neededAttributes = expression.getAllAttributes();
            final Object[] values = new Object[neededAttributes.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = 1.0;
            }
            expression.bindVariables(values);
        }
        return expression.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
        if (this.lastObjects.size() > this.maxHistoryElements) {
            this.lastObjects.removeLast();
        }
        this.lastObjects.addFirst(object);
        if (!this.stateTransitionExpression.getMEPExpression().isConstant()) {
            this.update(object, this.stateTransitionVariables, this.stateTransitionExpression, this.stateTransitionRef);
        }
        if ((this.controlExpression != null) && (!this.controlExpression.getMEPExpression().isConstant())) {
            this.update(object, this.controlVariables, this.controlExpression, this.controlRef);
        }
        if (!this.processNoiseExpression.getMEPExpression().isConstant()) {
            this.update(object, this.processNoiseVariables, this.processNoiseExpression, this.processNoiseRef);
        }
        if (!this.measurementNoiseExpression.getMEPExpression().isConstant()) {
            this.update(object, this.measurementNoiseVariables, this.measurementNoiseExpression, this.measurementNoiseRef);
        }
        if (!this.measurementExpression.getMEPExpression().isConstant()) {
            this.update(object, this.measurementVariables, this.measurementExpression, this.measurementRef);
        }
        final MultivariateMixtureDistribution[] inputDistributions = object.getDistributions();
        final Object[] inputAttributes = object.getAttributes();

        this.filter.predict();

        final double[] value = new double[this.attributes.length];
        for (int i = 0; i < this.attributes.length; i++) {
            value[i] = ((Number) object.getAttribute(this.attributes[i])).doubleValue();
        }
        this.filter.correct(value);

        final double[] state = this.filter.getStateEstimationVector().toArray();
        final double[][] covariance = this.filter.getErrorCovarianceMatrix().getData();
        final MultivariateMixtureDistribution[] outputDistributions = new MultivariateMixtureDistribution[inputDistributions.length + 1];
        final Object[] outputAttributes = new Object[inputAttributes.length + state.length];
        // Copy the old distributions to the new tuple
        System.arraycopy(inputDistributions, 0, outputDistributions, 0, inputDistributions.length);
        // Copy the old attributes to the new tuple
        System.arraycopy(inputAttributes, 0, outputAttributes, 0, inputAttributes.length);
        // And append the new references
        final int[] outputAttributePositions = new int[state.length];
        for (int i = 0; i < state.length; i++) {
            outputAttributes[inputAttributes.length + i] = new ProbabilisticDouble(inputDistributions.length);
            outputAttributePositions[i] = inputAttributes.length + i;
        }
        final List<Pair<Double, IMultivariateDistribution>> mvns = new ArrayList<Pair<Double, IMultivariateDistribution>>();
        IMultivariateDistribution component;
        try {
            component = new MultivariateNormalDistribution(state, covariance);
            this.errorCovariance = this.filter.getErrorCovarianceMatrix().copy();
        }
        catch (SingularMatrixException | NonPositiveDefiniteMatrixException | MathUnsupportedOperationException e) {
            if (KalmanFilterPO.LOG.isDebugEnabled()) {
                KalmanFilterPO.LOG.debug(e.getMessage() + ": " + this.filter.getErrorCovarianceMatrix(), e);
            }
            // Take the last estimated covariance
            component = new MultivariateNormalDistribution(state, this.errorCovariance.getData());
        }
        mvns.add(new Pair<Double, IMultivariateDistribution>(1.0, component));

        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(mvns);

        // And append the new distribution to the end of the array
        outputDistributions[inputDistributions.length] = mixture;

        mixture.setAttributes(outputAttributePositions);
        final ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(outputAttributes, outputDistributions, true);
        outputVal.setMetadata((T) object.getMetadata().clone());

        // KTHXBYE
        this.transfer(outputVal);
    }

    
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
    private void initStateTransition(final SDFSchema schema, final SDFExpression stateTransitionExpression) {
        final List<SDFAttribute> neededAttributes = stateTransitionExpression.getAllAttributes();
        final VarHelper[] stateTransitionVariables = new VarHelper[neededAttributes.size()];
        int j = 0;
        for (final SDFAttribute curAttribute : neededAttributes) {
            stateTransitionVariables[j++] = this.initAttribute(schema, curAttribute);
            if (stateTransitionVariables[j - 1].objectPosToUse > 0) {
                this.maxHistoryElements = Math.max(this.maxHistoryElements, stateTransitionVariables[j - 1].objectPosToUse);
            }
        }
        this.stateTransitionVariables = stateTransitionVariables;
    }

    private void initControl(final SDFSchema schema, final SDFExpression controlExpression) {
        final List<SDFAttribute> neededAttributes = controlExpression.getAllAttributes();
        final VarHelper[] controlVariables = new VarHelper[neededAttributes.size()];
        int j = 0;
        for (final SDFAttribute curAttribute : neededAttributes) {
            controlVariables[j++] = this.initAttribute(schema, curAttribute);
            if (controlVariables[j - 1].objectPosToUse > 0) {
                this.maxHistoryElements = Math.max(this.maxHistoryElements, controlVariables[j - 1].objectPosToUse);
            }
        }
        this.controlVariables = controlVariables;
    }

    private void initProcessNoise(final SDFSchema schema, final SDFExpression processNoiseExpression) {
        final List<SDFAttribute> neededAttributes = processNoiseExpression.getAllAttributes();
        final VarHelper[] processNoiseVariables = new VarHelper[neededAttributes.size()];
        int j = 0;
        for (final SDFAttribute curAttribute : neededAttributes) {
            processNoiseVariables[j++] = this.initAttribute(schema, curAttribute);
            if (processNoiseVariables[j - 1].objectPosToUse > 0) {
                this.maxHistoryElements = Math.max(this.maxHistoryElements, processNoiseVariables[j - 1].objectPosToUse);
            }
        }
        this.processNoiseVariables = processNoiseVariables;
    }

    private void initMeasurementNoise(final SDFSchema schema, final SDFExpression measurementNoiseExpression) {
        final List<SDFAttribute> neededAttributes = measurementNoiseExpression.getAllAttributes();
        final VarHelper[] measurementNoiseVariables = new VarHelper[neededAttributes.size()];
        int j = 0;
        for (final SDFAttribute curAttribute : neededAttributes) {
            measurementNoiseVariables[j++] = this.initAttribute(schema, curAttribute);
            if (measurementNoiseVariables[j - 1].objectPosToUse > 0) {
                this.maxHistoryElements = Math.max(this.maxHistoryElements, measurementNoiseVariables[j - 1].objectPosToUse);
            }
        }
        this.measurementNoiseVariables = measurementNoiseVariables;
    }

    private void initMeasurement(final SDFSchema schema, final SDFExpression measurementExpression) {
        final List<SDFAttribute> neededAttributes = measurementExpression.getAllAttributes();
        final VarHelper[] measurementVariables = new VarHelper[neededAttributes.size()];
        int j = 0;
        for (final SDFAttribute curAttribute : neededAttributes) {
            measurementVariables[j++] = this.initAttribute(schema, curAttribute);
            if (measurementVariables[j - 1].objectPosToUse > 0) {
                this.maxHistoryElements = Math.max(this.maxHistoryElements, measurementVariables[j - 1].objectPosToUse);
            }
        }
        this.measurementVariables = measurementVariables;
    }

    private VarHelper initAttribute(final SDFSchema schema, final SDFAttribute curAttribute) {
        if (curAttribute.getNumber() > 0) {
            final int pos = curAttribute.getNumber();
            if (pos > this.maxHistoryElements) {
                this.maxHistoryElements = pos + 1;
            }
            final int index = schema.indexOf(curAttribute);
            return new VarHelper(index, pos);
        }
        else {
            return new VarHelper(schema.indexOf(curAttribute), 0);
        }
    }

}

class VarHelper {
    int pos;
    int objectPosToUse;

    public VarHelper(final int pos, final int objectPosToUse) {
        super();
        this.pos = pos;
        this.objectPosToUse = objectPosToUse;
    }

    @Override
    public String toString() {
        return this.pos + " " + this.objectPosToUse;
    }
}