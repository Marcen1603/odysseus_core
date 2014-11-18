package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

/**
 * A {@link AbstractFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link AbstractFragmentAO} has only one parameter for the number of fragments and it must have exact one input.
 * @author Michael Brand
 */
public abstract class AbstractFragmentAO extends UnaryLogicalOp {
	
	private static final long serialVersionUID = -6789007084291408905L;

	/**
	 * The number of fragments.
	 */
	private long numFragments;
	
	/**
	 * The heartbeatrate 
	 */
	private int heartbeatrate = 10;
	
	/**
	 * Constructs a new {@link AbstractFragmentAO}.
	 * @see UnaryLogicalOp#UnaryLogicalOp()
	 */
	public AbstractFragmentAO() {
		
		super();
		
	}

	/**
	 * Constructs a new {@link AbstractFragmentAO} as a copy of an existing one.
	 * @param fragmentAO The {@link AbstractFragmentAO} to be copied.
	 * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
	 */
	public AbstractFragmentAO(AbstractFragmentAO fragmentAO) {
		
		super(fragmentAO);
		this.numFragments = fragmentAO.numFragments;
		this.heartbeatrate = fragmentAO.heartbeatrate;
		
	}

	@Override
	public abstract AbstractLogicalOperator clone();
	
	/**
	 * Returns the number of fragments.
	 */
	@GetParameter(name = "FRAGMENTS")
	public long getNumberOfFragments() {

		return this.numFragments;
		
	}

	/**
	 * Sets the number of fragments.
	 */
	@Parameter(type = LongParameter.class, name = "FRAGMENTS", optional = false)
	public void setNumberOfFragments(long numFragments) {
		
		this.numFragments = numFragments;
		this.addParameterInfo("FRAGMENTS", numFragments);
	}
	
	@Parameter(type = IntegerParameter.class, name ="Heartbeatreate", optional = true, doc ="Send heartbeats to all other ports. Default is 10")
	public void setHeartbeatrate(int heartbeatrate) {
		this.heartbeatrate = heartbeatrate;
	}
	
	public int getHeartbeatrate() {
		return heartbeatrate;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		
		return super.getOutputSchemaIntern(0);
		
	}

}