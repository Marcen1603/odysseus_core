package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

/**
 * A {@link AbstractStaticFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link AbstractStaticFragmentAO} has only one parameter for the number of
 * fragments and it must have exact one input.
 * 
 * @author Michael Brand
 * @author Christian Kuka <christian@kuka.cc>
 */
public abstract class AbstractStaticFragmentAO extends AbstractFragmentAO {

    /**
     * 
     */
    private static final long serialVersionUID = -7713478708449376992L;
    /**
     * The number of fragments.
     */
    private long numFragments;

    /**
     * The heartbeatrate
     */
    private int heartbeatrate = 10;

    /**
     * Constructs a new {@link AbstractStaticFragmentAO}.
     * 
     * @see UnaryLogicalOp#UnaryLogicalOp()
     */
    public AbstractStaticFragmentAO() {

        super();

    }

    /**
     * Constructs a new {@link AbstractStaticFragmentAO} as a copy of an
     * existing one.
     * 
     * @param fragmentAO
     *            The {@link AbstractStaticFragmentAO} to be copied.
     * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
     */
    public AbstractStaticFragmentAO(AbstractStaticFragmentAO fragmentAO) {

        super(fragmentAO);
        this.numFragments = fragmentAO.numFragments;
        this.heartbeatrate = fragmentAO.heartbeatrate;
    }

    @Override
    public abstract AbstractLogicalOperator clone();

    /**
     * Returns the number of fragments.
     */
    @Override
    @GetParameter(name = "FRAGMENTS")
    public long getNumberOfFragments() {

        return this.numFragments;

    }

    /**
     * Sets the number of fragments.
     */
    @Override
    @Parameter(type = LongParameter.class, name = "FRAGMENTS", optional = false)
    public void setNumberOfFragments(long numFragments) {

        this.numFragments = numFragments;
        this.addParameterInfo("FRAGMENTS", numFragments);
    }

    @Parameter(type = IntegerParameter.class, name = "Heartbeatreate", optional = true, doc = "Send heartbeats to all other ports. Default is 10")
    public void setHeartbeatrate(int heartbeatrate) {
        this.heartbeatrate = heartbeatrate;
    }

    public int getHeartbeatrate() {
        return heartbeatrate;
    }

    @Override
    public boolean isValid() {
        boolean isValid = true;
        if (numFragments <= 0) {
            addError("Number of fragmenents must be greater than 0");
            isValid = false;
        }
        return super.isValid() && isValid;
    }

}