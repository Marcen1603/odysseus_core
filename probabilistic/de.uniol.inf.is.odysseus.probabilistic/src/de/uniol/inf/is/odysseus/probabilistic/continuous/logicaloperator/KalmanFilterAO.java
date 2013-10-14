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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MatrixParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

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
	private double[][] stateTransitionAttribute;
	/** The control matrix. */
	private double[][] controlAttribute;
	/** The noise of the process. */
	private double[][] processNoiseAttribute;
	/** The noise of the measurement. */
	private double[][] measurementNoiseAttribute;

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
		this.stateTransitionAttribute = kalmanAO.stateTransitionAttribute;
		this.controlAttribute = kalmanAO.controlAttribute;
		this.processNoiseAttribute = kalmanAO.processNoiseAttribute;
		this.measurementNoiseAttribute = kalmanAO.measurementNoiseAttribute;
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
	 * @param stateTransition
	 *            The state transition
	 */
	@Parameter(type = MatrixParameter.class, name = "TRANSITION", isList = false, optional = true)
	public final void setStateTransition(final double[][] stateTransition) {
		this.stateTransitionAttribute = stateTransition;
	}

	/**
	 * Gets the state transition for the Kalman correction.
	 * 
	 * @return The state transition
	 */
	@GetParameter(name = "TRANSITION")
	public final double[][] getStateTransition() {
		return this.stateTransitionAttribute;
	}

	/**
	 * Sets the control for the Kalman correction.
	 * 
	 * @param control
	 *            The control
	 */
	@Parameter(type = MatrixParameter.class, name = "CONTROL", isList = false, optional = true)
	public final void setControl(final double[][] control) {
		this.controlAttribute = control;
	}

	/**
	 * Gets the control for the Kalman correction.
	 * 
	 * @return The control
	 */
	@GetParameter(name = "CONTROL")
	public final double[][] getControl() {
		return this.controlAttribute;
	}

	/**
	 * Sets the process noise for the Kalman correction.
	 * 
	 * @param noise
	 *            The process noise
	 */
	@Parameter(type = MatrixParameter.class, name = "PROCESSNOISE", isList = false, optional = true)
	public final void setProcessNoise(final double[][] noise) {
		this.processNoiseAttribute = noise;
	}

	/**
	 * Gets the state transition for the Kalman correction.
	 * 
	 * @return The state transition
	 */
	@GetParameter(name = "PROCESSNOISE")
	public final double[][] getProcessNoise() {
		return this.processNoiseAttribute;
	}

	/**
	 * Sets the measurement noise for the Kalman correction.
	 * 
	 * @param noise
	 *            The measurement noise
	 */
	@Parameter(type = MatrixParameter.class, name = "MEASUREMENTNOISE", isList = false, optional = true)
	public final void setMeasurementNoise(final double[][] noise) {
		this.measurementNoiseAttribute = noise;
	}

	/**
	 * Gets the measurement noise for the Kalman correction.
	 * 
	 * @return The measurement noise
	 */
	@GetParameter(name = "MEASUREMENTNOISE")
	public final double[][] getMeasurementNoise() {
		return this.measurementNoiseAttribute;
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
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public final KalmanFilterAO clone() {
		return new KalmanFilterAO(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#initialize()
	 */
	@Override
	public final void initialize() {
		final Collection<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		for (final SDFAttribute inAttr : this.getInputSchema().getAttributes()) {
			if (this.getAttributes().contains(inAttr)) {
				outputAttributes.add(new SDFAttribute(inAttr.getSourceName(), inAttr.getAttributeName(), SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
			} else {
				outputAttributes.add(inAttr);
			}
		}

		final SDFSchema outputSchema = new SDFSchema(this.getInputSchema().getURI(), this.getInputSchema().getType(), outputAttributes);
		this.setOutputSchema(outputSchema);
	}

}
