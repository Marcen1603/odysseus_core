package de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

/**
 * A {@link RRFragmentAO} can be used to fragment incoming streams by round-robin. <br />
 * The {@link RRFragmentAO} does not have one parameter for the number of fragments and it must have exact one input.
 * It can be used in PQL: <br />
 * <code>output = RR_FRAGMENT(input)</code> 
 * @author Michael Brand
 */
@LogicalOperator(name = "RR_FRAGMENT", minInputPorts = 1, maxInputPorts = 1)
public class RRFragmentAO extends UnaryLogicalOp {
	
	/**
	 * The number of fragments.
	 */
	private long numFragments;
	
	/**
	 * Constructs a new {@link RRFragmentAO}.
	 * @see UnaryLogicalOp#UnaryLogicalOp()
	 */
	public RRFragmentAO() {
		
		super();
		this.numFragments = 0;
		
	}

	/**
	 * Constructs a new {@link RRFragmentAO} as a copy of an existing one.
	 * @param fragmentAO The {@link RRFragmentAO} to be copied.
	 * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
	 */
	public RRFragmentAO(RRFragmentAO fragmentAO) {
		
		super(fragmentAO);
		this.numFragments = fragmentAO.numFragments;
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new RRFragmentAO(this);
		
	}
	
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
		
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		
		return super.getOutputSchemaIntern(0);
		
	}

}