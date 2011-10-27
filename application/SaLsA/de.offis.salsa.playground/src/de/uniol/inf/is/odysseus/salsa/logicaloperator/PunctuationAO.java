package de.uniol.inf.is.odysseus.salsa.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "PUNCTUATION")
public class PunctuationAO extends BinaryLogicalOp {

    /**
     * 
     */
    private static final long serialVersionUID = 6293636144390168094L;
    private long ratio;

    public PunctuationAO() {
        super();
    }

    public PunctuationAO(PunctuationAO ao) {
        super(ao);
        this.ratio = ao.ratio;
    }

    @Override
    public SDFAttributeList getOutputSchema() {
        return getInputSchema(LEFT);
    }

    @Override
    public PunctuationAO clone() {
        return new PunctuationAO(this);
    }

    /**
     * @param ratio
     *            The ratio for punctuation
     */
    @Parameter(type = LongParameter.class, name = "RATIO", optional = false)
    public void setRatio(final long ratio) {
        this.ratio = ratio;
    }

    /**
     * @return The ratio
     */
    public long getRatio() {
        return ratio;
    }
}
