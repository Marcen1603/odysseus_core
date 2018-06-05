package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, deprecation = true, name = "SAMPLEFROM", doc = "Create samples from a given distribution", category = { LogicalOperatorCategory.PROBABILISTIC })
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
        super();
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
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = false, doc = "The distribution to sample from.")
    public final void setAttributes(final List<SDFAttribute> attributes) {
        Objects.requireNonNull(attributes);
        Preconditions.checkArgument(!attributes.isEmpty());
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
        return Collections.unmodifiableList(this.attributes);
    }

    /**
     * Sets the number of samples .
     * 
     * @param samples
     *            The number of samples
     */
    @Parameter(type = IntegerParameter.class, name = "SAMPLES", isList = false, optional = false, doc = "The number of samples to create.")
    public final void setSamples(final int samples) {
        Preconditions.checkArgument(samples > 0);
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
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #clone()
     */
    @Override
    public final SampleAO clone() {
        return new SampleAO(this);
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
        super.initialize();
        Objects.requireNonNull(this.attributes);
        Preconditions.checkArgument(!this.attributes.isEmpty());
        Preconditions.checkArgument(this.samples > 0);
        final Collection<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
        for (final SDFAttribute inAttr : this.getInputSchema().getAttributes()) {
            if (this.getAttributes().contains(inAttr)) {
                outputAttributes.add(new SDFAttribute(inAttr.getSourceName(), inAttr.getAttributeName(), SDFDatatype.DOUBLE, inAttr.getUnit(), inAttr.getDtConstraints()));
            }
            else {
                outputAttributes.add(inAttr);
            }
        }

        final SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema());
        this.setOutputSchema(outputSchema);
    }

}
