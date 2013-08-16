package de.uniol.inf.is.odysseus.p2p_new.lb.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.p2p_new.lb.fragmentation.RRFragmentation;

/**
 * A {@link FragmentAO} can be used to fragment incoming streams. <br />
 * The {@link FragmentAO} has only one parameter for the number of fragments and it must have exact one input.
 * It can be used in PQL: <br />
 * <code>output = FRAGMENT([FRAGMENTS=n], input)</code>
 * @author Michael Brand
 */
@LogicalOperator(name = "FRAGMENT", minInputPorts = 1, maxInputPorts = 1)
public class FragmentAO extends UnaryLogicalOp {
	
	private static final long serialVersionUID = -6789007084291408905L;

	/**
	 * The number of fragments.
	 */
	private long numFragments;
	
	/**
	 * The type of fragmentation.
	 * XXX Till the TransformationConfiguration on other peers contains the QueryBuildConfiguration of the origin peer.
	 */
	private String type;
	
	/**
	 * Constructs a new {@link FragmentAO}.
	 * @see UnaryLogicalOp#UnaryLogicalOp()
	 */
	public FragmentAO() {
		
		super();
		// no fragmentation per default
		this.setNumberOfFragments(1);
		// roundrobin per default
		this.setType(RRFragmentation.NAME);
		
	}

	/**
	 * Constructs a new {@link FragmentAO} as a copy of an existing one.
	 * @param fragmentAO The {@link FragmentAO} to be copied.
	 * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
	 */
	public FragmentAO(FragmentAO fragmentAO) {
		
		super(fragmentAO);
		this.numFragments = fragmentAO.numFragments;
		this.type = fragmentAO.type;
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new FragmentAO(this);
		
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
		this.addParameterInfo("FRAGMENTS", numFragments);
		
	}
	
	/**
	 * Returns the type of fragmentation.
	 */
	@GetParameter(name = "TYPE")
	public String getType() {

		return this.type;
		
	}

	/**
	 * Sets the type of fragmentation.
	 */
	@Parameter(type = StringParameter.class, name = "TYPE", optional = true)
	public void setType(String type) {
		
		this.type = type;
		this.addParameterInfo("TYPE",  "\'" + type + "\'");
		
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		
		return super.getOutputSchemaIntern(0);
		
	}

}