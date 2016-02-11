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
package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "KALMAN", deprecation = true, category = { "Probabilistic" }, doc = "Kalman filter operator")
public class KalmanFilterAO extends UnaryLogicalOp {
    /**
	 * 
	 */
    private static final long serialVersionUID = 5147945532482073567L;
    /** The attributes to feed the Kalman Filter. */
    private List<SDFAttribute> attributes;
    /** The variable names. */
    private List<String> variables;
    /** The state transition matrix. */
    private NamedExpression stateTransitionExpression;
    /** The control matrix. */
    private NamedExpression controlExpression;
    /** The measurement matrix. */
    private NamedExpression measurementExpression;
    /** The noise of the process. */
    private NamedExpression processNoiseExpression;
    /** The noise of the measurement. */
    private NamedExpression measurementNoiseExpression;
    /** The initial state. */
    private NamedExpression initialStateExpression;
    /** The initial error. */
    private NamedExpression initialErrorExpression;

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
        this.variables = new ArrayList<String>(kalmanAO.variables);
        this.stateTransitionExpression = kalmanAO.stateTransitionExpression;
        this.controlExpression = kalmanAO.controlExpression;
        this.processNoiseExpression = kalmanAO.processNoiseExpression;
        this.measurementExpression = kalmanAO.measurementExpression;
        this.measurementNoiseExpression = kalmanAO.measurementNoiseExpression;
        this.initialStateExpression = kalmanAO.initialStateExpression;
        this.initialErrorExpression = kalmanAO.initialErrorExpression;
    }

    /**
     * Sets the variable names for the Kalman correction.
     * 
     * @param attributes
     *            The list of variable names
     */
    @Parameter(type = StringParameter.class, name = "VARIABLES", isList = true, optional = false)
    public final void setVariabless(final List<String> variables) {
        this.variables = variables;
    }

    /**
     * Gets the variable names for the Kalman correction.
     * 
     * @return The list of variable names
     */
    @GetParameter(name = "VARIABLES")
    public final List<String> getVariables() {
        if (this.variables == null) {
            this.variables = new ArrayList<String>();
        }
        return this.variables;
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
    @Parameter(type = NamedExpressionParameter.class, name = "TRANSITION", isList = false, optional = false)
    public final void setStateTransitionExpression(final NamedExpression stateTransitionExpression) {
        this.stateTransitionExpression = stateTransitionExpression;
    }

    /**
     * Gets the state transition for the Kalman correction.
     * 
     * @return The state transition
     */
    @GetParameter(name = "TRANSITION")
    public final NamedExpression getStateTransitionExpression() {
        return this.stateTransitionExpression;
    }

    private final double[][] getStateTransition() {
        if (!this.stateTransitionExpression.expression.getMEPExpression().isConstant()) {
            final List<SDFAttribute> neededAttributes = this.stateTransitionExpression.expression.getAllAttributes();
            final Object[] values = new Object[neededAttributes.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = 1.0;
            }
            this.stateTransitionExpression.expression.bindVariables(values);
        }
        return this.stateTransitionExpression.expression.getValue();
    }

    /**
     * Sets the control for the Kalman correction.
     * 
     * @param control
     *            The control
     */
    @Parameter(type = NamedExpressionParameter.class, name = "CONTROL", isList = false, optional = true)
    public final void setControlExpression(final NamedExpression controlExpression) {
        this.controlExpression = controlExpression;
    }

    /**
     * Gets the control for the Kalman correction.
     * 
     * @return The control
     */
    @GetParameter(name = "CONTROL")
    public final NamedExpression getControlExpression() {
        return this.controlExpression;
    }

    private final double[][] getControl() {
        if (this.controlExpression == null) {
            return null;
        }
        if (!this.controlExpression.expression.getMEPExpression().isConstant()) {
            final List<SDFAttribute> neededAttributes = this.controlExpression.expression.getAllAttributes();
            final Object[] values = new Object[neededAttributes.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = 1.0;
            }
            this.controlExpression.expression.bindVariables(values);
        }
        return this.controlExpression.expression.getValue();
    }

    /**
     * Sets the process noise for the Kalman correction.
     * 
     * @param noise
     *            The process noise
     */
    @Parameter(type = NamedExpressionParameter.class, name = "PROCESSNOISE", isList = false, optional = false)
    public final void setProcessNoiseExpression(final NamedExpression processNoiseExpression) {
        this.processNoiseExpression = processNoiseExpression;
    }

    /**
     * Gets the state transition for the Kalman correction.
     * 
     * @return The state transition
     */
    @GetParameter(name = "PROCESSNOISE")
    public final NamedExpression getProcessNoiseExpression() {
        return this.processNoiseExpression;
    }

    private final double[][] getProcessNoise() {
        if (!this.processNoiseExpression.expression.getMEPExpression().isConstant()) {
            final List<SDFAttribute> neededAttributes = this.processNoiseExpression.expression.getAllAttributes();
            final Object[] values = new Object[neededAttributes.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = 1.0;
            }
            this.processNoiseExpression.expression.bindVariables(values);
        }
        return this.processNoiseExpression.expression.getValue();
    }

    /**
     * Sets the measurement for the Kalman correction.
     * 
     * @param measurementExpression
     *            The measurement
     */
    @Parameter(type = NamedExpressionParameter.class, name = "MEASUREMENT", isList = false, optional = false)
    public final void setMeasurementExpression(final NamedExpression measurementExpression) {
        this.measurementExpression = measurementExpression;
    }

    /**
     * Gets the measurement for the Kalman correction.
     * 
     * @return The measurement
     */
    @GetParameter(name = "MEASUREMENT")
    public final NamedExpression getMeasurementExpression() {
        return this.measurementExpression;
    }

    private final double[][] getMeasurement() {
        if (!this.measurementExpression.expression.getMEPExpression().isConstant()) {
            final List<SDFAttribute> neededAttributes = this.measurementExpression.expression.getAllAttributes();
            final Object[] values = new Object[neededAttributes.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = 1.0;
            }
            this.measurementExpression.expression.bindVariables(values);
        }
        return this.measurementExpression.expression.getValue();
    }

    /**
     * Sets the measurement noise for the Kalman correction.
     * 
     * @param measurementNoiseExpression
     *            The measurement noise
     */
    @Parameter(type = NamedExpressionParameter.class, name = "MEASUREMENTNOISE", isList = false, optional = false)
    public final void setMeasurementNoiseExpression(final NamedExpression measurementNoiseExpression) {
        this.measurementNoiseExpression = measurementNoiseExpression;
    }

    /**
     * Gets the measurement noise for the Kalman correction.
     * 
     * @return The measurement noise
     */
    @GetParameter(name = "MEASUREMENTNOISE")
    public final NamedExpression getMeasurementNoiseExpression() {
        return this.measurementNoiseExpression;
    }

    private final double[][] getMeasurementNoise() {
        if (!this.measurementNoiseExpression.expression.getMEPExpression().isConstant()) {
            final List<SDFAttribute> neededAttributes = this.measurementNoiseExpression.expression.getAllAttributes();
            final Object[] values = new Object[neededAttributes.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = 1.0;
            }
            this.measurementNoiseExpression.expression.bindVariables(values);
        }
        return this.measurementNoiseExpression.expression.getValue();
    }

    /**
     * Sets the initial state for the Kalman correction.
     * 
     * @param initialStateExpression
     *            The initial state
     */
    @Parameter(type = NamedExpressionParameter.class, name = "INITIALSTATE", isList = false, optional = true)
    public final void setInitialStateExpression(final NamedExpression initialStateExpression) {
        this.initialStateExpression = initialStateExpression;
    }

    /**
     * Gets the initial state for the Kalman correction.
     * 
     * @return The initial state
     */
    @GetParameter(name = "INITIALSTATE")
    public final NamedExpression getInitialStateExpression() {
        return this.initialStateExpression;
    }

    public final double[] getInitialState() {
        return ((double[][]) this.initialStateExpression.expression.getValue())[0];
    }

    /**
     * Sets the initial error for the Kalman correction.
     * 
     * @param initialErrorExpression
     *            The initial error
     */
    @Parameter(type = NamedExpressionParameter.class, name = "INITIALERROR", isList = false, optional = true)
    public final void setInitialErrorExpression(final NamedExpression initialErrorExpression) {
        this.initialErrorExpression = initialErrorExpression;
    }

    /**
     * Gets the initial error for the Kalman correction.
     * 
     * @return The initial error
     */
    @GetParameter(name = "INITIALERROR")
    public final NamedExpression getInitialErrorExpression() {
        return this.initialErrorExpression;
    }

    public final double[][] getInitialError() {
        return this.initialErrorExpression.expression.getValue();
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
            outputAttributes.add(inAttr);
        }
        for (int i = 0; i < this.getStateTransition().length; i++) {
            outputAttributes.add(new SDFAttribute("", this.getVariables().get(i), SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
        }
        final SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema());
        this.setOutputSchema(outputSchema);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        final List<String> variables = this.getVariables();
        final double[] initialState = this.getInitialState();
        final double[][] initialError = this.getInitialError();
        final double[][] stateTransition = this.getStateTransition();
        final double[][] processNoise = this.getProcessNoise();
        final double[][] measurement = this.getMeasurement();
        final double[][] measurementNoise = this.getMeasurementNoise();
        final double[][] control = this.getControl();
        if ((this.initialStateExpression != null) && (!this.initialStateExpression.expression.getMEPExpression().isConstant())) {
            return false;
        }
        if ((this.initialErrorExpression != null) && (!this.initialErrorExpression.expression.getMEPExpression().isConstant())) {
            return false;
        }
        if ((stateTransition == null) || (stateTransition.length < 1) || (stateTransition.length != stateTransition[0].length)) {
            return false;
        }
        if ((processNoise == null) || (processNoise.length < 1) || (processNoise.length != processNoise[0].length) || (processNoise.length != stateTransition.length)) {
            return false;
        }
        if ((this.getAttributes() == null) || (this.getAttributes().isEmpty())) {
            return false;
        }

        if ((measurement == null) || (measurement.length < 1) || (measurement[0].length != stateTransition.length) || (measurement.length != this.getAttributes().size())) {
            return false;
        }
        if ((measurementNoise == null) || (measurementNoise.length < 1) || (measurementNoise.length != this.getAttributes().size())) {
            return false;
        }
        if ((control != null) && ((control.length < 1) || (control.length != control[0].length) || (control.length != stateTransition.length))) {
            return false;
        }
        if ((initialState != null) && ((initialState.length < 1) || (initialState.length != stateTransition.length))) {
            return false;
        }
        if ((initialError != null) && ((initialError.length < 1) || (initialError.length != initialError[0].length) || (initialError.length != stateTransition.length))) {
            return false;
        }
        if ((variables == null) || (variables.size() != stateTransition.length)) {
            return false;
        }
        return super.isValid();
    }
}
