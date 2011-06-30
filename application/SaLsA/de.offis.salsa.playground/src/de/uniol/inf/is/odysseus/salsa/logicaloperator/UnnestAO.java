package de.uniol.inf.is.odysseus.salsa.logicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "UNNEST")
public class UnnestAO extends UnaryLogicalOp {

    /**
     * 
     */
    private static final long serialVersionUID = -5918972476973244744L;
    private static Logger LOG = LoggerFactory.getLogger(UnnestAO.class);

    SDFAttribute attribute;

    /**
     * 
     */
    public UnnestAO() {
        super();
    }

    /**
     * @param ao
     */
    public UnnestAO(final UnnestAO ao) {
        super(ao);
        this.attribute = ao.getAttribute();
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new UnnestAO(this);
    }

    /**
     * @return The attribute for unnest
     */
    public SDFAttribute getAttribute() {
        return this.attribute;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getOutputSchema()
     */
    @Override
    public SDFAttributeList getOutputSchema() {
        return this.getInputSchema();
    }

    /**
     * @param attribute
     *            The attribute for unnest
     */
    @Parameter(name = "ATTRIBUTE", type = ResolvedSDFAttributeParameter.class)
    public void setAttribute(final SDFAttribute attribute) {
        UnnestAO.LOG.debug("Set unnest attribute to {}", attribute.getAttributeName());
        this.attribute = attribute;
    }

}
