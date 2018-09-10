package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * A {@link AbstractDynamicFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link AbstractDynamicFragmentAO} has only one parameter for the number
 * of
 * fragments and it must have exact one input.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public abstract class AbstractDynamicFragmentAO extends AbstractFragmentAO {

    /**
     * 
     */
    private static final long serialVersionUID = 4226004663297576784L;
    /**
     * The heartbeatrate
     */
    private int heartbeatrate = 10;

    /**
     * Constructs a new {@link AbstractDynamicFragmentAO}.
     * 
     * @see UnaryLogicalOp#UnaryLogicalOp()
     */
    public AbstractDynamicFragmentAO() {

        super();

    }

    /**
     * Constructs a new {@link AbstractDynamicFragmentAO} as a copy of an
     * existing one.
     * 
     * @param fragmentAO
     *            The {@link AbstractDynamicFragmentAO} to be copied.
     * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
     */
    public AbstractDynamicFragmentAO(AbstractDynamicFragmentAO fragmentAO) {

        super(fragmentAO);
        this.heartbeatrate = fragmentAO.heartbeatrate;
    }

    @Override
    public abstract AbstractLogicalOperator clone();

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

        return super.isValid() && isValid;
    }

}