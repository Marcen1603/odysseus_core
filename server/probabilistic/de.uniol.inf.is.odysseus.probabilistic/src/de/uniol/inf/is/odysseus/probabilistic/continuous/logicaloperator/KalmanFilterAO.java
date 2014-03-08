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
package de.uniol.inf.is.odysseus.probabilistic.continuous.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "KALMAN", category = { "Probabilistic" }, doc = "Kalman filter operator")
public class KalmanFilterAO extends UnaryLogicalOp {
    /**
	 * 
	 */
    private static final long serialVersionUID = 5147945532482073567L;
    /** The attributes to build the distribution from. */
    private List<SDFAttribute> attributes;
    /** The state transition matrix. */
    private NamedExpressionItem stateTransitionExpression;
    /** The control matrix. */
    private NamedExpressionItem controlExpression;
    /** The measurement matrix. */
    private NamedExpressionItem measurementExpression;
    /** The noise of the process. */
    private NamedExpressionItem processNoiseExpression;
    /** The noise of the measurement. */
    private NamedExpressionItem measurementNoiseExpression;

    /**
     * Creates a new Kalman Filter logical operator.
     */
    public KalmanFilterAO() {

    }

    /**
     * Clone Constructor.
     * 
     * @param kalmanAO
     *            The copy
     */
    public KalmanFilterAO(final KalmanFilterAO kalmanAO) {
        super(kalmanAO);
        this.attributes = new ArrayList<SDFAttribute>(kalmanAO.attributes);
        this.stateTransitionExpression = kalmanAO.stateTransitionExpression;
        this.controlExpression = kalmanAO.controlExpression;
        this.processNoiseExpression = kalmanAO.processNoiseExpression;
        this.measurementExpression = kalmanAO.measurementExpression;
        this.measurementNoiseExpression = kalmanAO.measurementNoiseExpression;
    }

    /**
     * Sets the attributes for the Kalman correction.
     * 
     * @param attributes
     *            The list of attributes
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = false)
    public final void setAttributes(final List<SDFAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Gets the attributes for the Kalman correction.
     * 
     * @return The list of attributes
     */
    @GetParameter(name = "ATTRIBUTES")
    public final List<SDFAttribute> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new ArrayList<SDFAttribute>();
        }
        return this.attributes;
    }

    /**
     * Sets the state transition for the Kalman correction.
     * 
     * @param stateTransitionExpression
     *            The state transition
     */
    @Parameter(type = SDFExpressionParameter.class, name = "TRANSITION", isList = false, optional = false)
    public final void setStateTransitionExpression(final NamedExpressionItem stateTransitionExpression) {
        this.stateTransitionExpression = stateTransitionExpression;
    }

    /**
     * Gets the state transition for the Kalman correction.
     * 
     * @return The state transition
     */
    @GetParameter(name = "TRANSITION")
    public final NamedExpressionItem getStateTransitionExpression() {
        return this.stateTransitionExpression;
    }

    public final double[][] getStateTransition() {
        return this.stateTransitionExpression.expression.getValue();
    }

    /**
     * Sets the control for the Kalman correction.
     * 
     * @param control
     *            The control
     */
    @Parameter(type = SDFExpressionParameter.class, name = "CONTROL", isList = false, optional = true)
    public final void setControlExpression(final NamedExpressionItem controlExpression) {
        this.controlExpression = controlExpression;
    }

    /**
     * Gets the control for the Kalman correction.
     * 
     * @return The control
     */
    @GetParameter(name = "CONTROL")
    public final NamedExpressionItem getControlExpression() {
        return this.controlExpression;
    }

    public final double[][] getControl() {
        if (this.controlExpression == null) {
            return null;
        }
        return this.controlExpression.expression.getValue();
    }

    /**
     * Sets the process noise for the Kalman correction.
     * 
     * @param noise
     *            The process noise
     */
    @Parameter(type = SDFExpressionParameter.class, name = "PROCESSNOISE", isList = false, optional = false)
    public final void setProcessNoiseExpression(final NamedExpressionItem processNoiseExpression) {
        this.processNoiseExpression = processNoiseExpression;
    }

    /**
     * Gets the state transition for the Kalman correction.
     * 
     * @return The state transition
     */
    @GetParameter(name = "PROCESSNOISE")
    public final NamedExpressionItem getProcessNoiseExpression() {
        return this.processNoiseExpression;
    }

    public final double[][] getProcessNoise() {
        return this.processNoiseExpression.expression.getValue();
    }

    /**
     * Sets the measurement for the Kalman correction.
     * 
     * @param measurementExpression
     *            The measurement
     */
    @Parameter(type = SDFExpressionParameter.class, name = "MEASUREMENT", isList = false, optional = false)
    public final void setMeasurementExpression(final NamedExpressionItem measurementExpression) {
        this.measurementExpression = measurementExpression;
    }

    /**
     * Gets the measurement for the Kalman correction.
     * 
     * @return The measurement
     */
    @GetParameter(name = "MEASUREMENT")
    public final NamedExpressionItem getMeasurementExpression() {
        return this.measurementExpression;
    }

    public final double[][] getMeasurement() {
        return this.measurementExpression.expression.getValue();
    }

    /**
     * Sets the measurement noise for the Kalman correction.
     * 
     * @param measurementNoiseExpression
     *            The measurement noise
     */
    @Parameter(type = SDFExpressionParameter.class, name = "MEASUREMENTNOISE", isList = false, optional = false)
    public final void setMeasurementNoiseExpression(final NamedExpressionItem measurementNoiseExpression) {
        this.measurementNoiseExpression = measurementNoiseExpression;
    }

    /**
     * Gets the measurement noise for the Kalman correction.
     * 
     * @return The measurement noise
     */
    @GetParameter(name = "MEASUREMENTNOISE")
    public final NamedExpressionItem getMeasurementNoiseExpression() {
        return this.measurementNoiseExpression;
    }

    public final double[][] getMeasurementNoise() {
        return this.measurementNoiseExpression.expression.getValue();
    }

    /**
     * Gets the positions of the attributes.
     * 
     * @return The positions of the attributes
     */
    public final int[] determineAttributesList() {
        return SchemaUtils.getAttributePos(this.getInputSchema(), this.getAttributes());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #clone()
     */
    @Override
    public final KalmanFilterAO clone() {
        return new KalmanFilterAO(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #initialize()
     */
    @Override
    public final void initialize() {
        final Collection<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
        for (final SDFAttribute inAttr : this.getInputSchema().getAttributes()) {
            if (this.getAttributes().contains(inAttr)) {
                outputAttributes.add(new SDFAttribute(inAttr.getSourceName(), inAttr.getAttributeName(), SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE, null, null, null));
            }
            else {
                outputAttributes.add(inAttr);
            }
        }

        final SDFSchema outputSchema = new SDFSchema(this.getInputSchema(), outputAttributes);
        this.setOutputSchema(outputSchema);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        double[][] stateTransition = getStateTransition();
        double[][] processNoise = getProcessNoise();
        double[][] measurement = getMeasurement();
        double[][] measurementNoise = getMeasurementNoise();
        double[][] control = getControl();
        if ((stateTransition == null) || (stateTransition.length < 1) || (stateTransition.length != stateTransition[0].length)) {
            return false;
        }
        if ((processNoise == null) || (processNoise.length < 1) || (processNoise.length != processNoise[0].length) || (processNoise.length != stateTransition.length)) {
            return false;
        }
        if ((getAttributes() == null) || (getAttributes().isEmpty())) {
            return false;
        }

        if ((measurement == null) || (measurement.length < 1) || (measurement[0].length != stateTransition.length) || (measurement.length != getAttributes().size())) {
            return false;
        }
        if ((measurementNoise == null) || (measurementNoise.length < 1) || (measurementNoise.length != getAttributes().size())) {
            return false;
        }
        if ((control != null) && ((control.length < 1) || (control.length != control[0].length) || (control.length != stateTransition.length))) {
            return false;
        }
        return super.isValid();
    }
}
