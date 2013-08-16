package de.uniol.inf.is.odysseus.probabilistic.continuous.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc> FIXME rename to Sample after removing SampleAO from core MG: I do not think this is a good idea!
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SAMPLEFROM")
public class SampleAO extends UnaryLogicalOp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5584282351145779955L;
	/** The attributes to sample from. */
	private List<SDFAttribute> attributes;
	/** The number of samples. */
	private int samples;

	/**
	 * Creates a new Sample logical operator.
	 */
	public SampleAO() {

	}

	/**
	 * Clone Constructor.
	 * 
	 * @param sampleAO
	 *            The copy
	 */
	public SampleAO(final SampleAO sampleAO) {
		super(sampleAO);
		this.attributes = new ArrayList<SDFAttribute>(sampleAO.attributes);
		this.samples = sampleAO.samples;
	}

	/**
	 * Sets the attributes to sample from.
	 * 
	 * @param attributes
	 *            The list of attributes
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = false)
	public final void setAttributes(final List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the attributes to sample from.
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
	 * Sets the number of samples .
	 * 
	 * @param samples
	 *            The number of samples
	 */
	@Parameter(type = IntegerParameter.class, name = "SAMPLES", isList = false, optional = false)
	public final void setSamples(final int samples) {
		this.samples = samples;
	}

	/**
	 * Gets the number of samples.
	 * 
	 * @return The number of samples
	 */
	@GetParameter(name = "SAMPLES")
	public final int getSamples() {
		return this.samples;
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
	public final SampleAO clone() {
		return new SampleAO(this);
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
				outputAttributes.add(new SDFAttribute(inAttr.getSourceName(), inAttr.getAttributeName(), SDFDatatype.DOUBLE));
			} else {
				outputAttributes.add(inAttr);
			}
		}

		final SDFSchema outputSchema = new SDFSchema(this.getInputSchema().getURI(), outputAttributes);
		this.setOutputSchema(outputSchema);
	}

}
