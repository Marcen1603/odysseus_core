package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

/**
 * A {@link AbstractFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link AbstractFragmentAO} must have exact one input.
 * 
 * @author Michael Brand
 */
public abstract class AbstractFragmentAO extends UnaryLogicalOp {

    private static final long serialVersionUID = -6789007084291408905L;

    /**
     * Constructs a new {@link AbstractFragmentAO}.
     * 
     * @see UnaryLogicalOp#UnaryLogicalOp()
     */
    public AbstractFragmentAO() {

        super();

    }

    /**
     * Constructs a new {@link AbstractFragmentAO} as a copy of an existing one.
     * 
     * @param fragmentAO
     *            The {@link AbstractFragmentAO} to be copied.
     * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
     */
    public AbstractFragmentAO(AbstractFragmentAO fragmentAO) {

        super(fragmentAO);

    }

    @Override
    public abstract AbstractLogicalOperator clone();

    public long getNumberOfFragments() {
        return this.getSubscriptions().size();
    }

    /**
     * Sets the number of fragments.
     */
    @Deprecated
    public void setNumberOfFragments(long numFragments) {

        this.addParameterInfo("FRAGMENTS", getNumberOfFragments());
    }

    @Override
    protected SDFSchema getOutputSchemaIntern(int pos) {

        return super.getOutputSchemaIntern(0);

    }

    @Override
    public boolean isValid() {
        boolean isValid = true;

        return super.isValid() && isValid;
    }

}