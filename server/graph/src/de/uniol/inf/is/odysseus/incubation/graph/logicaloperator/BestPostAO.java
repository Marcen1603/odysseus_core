package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

@LogicalOperator(name="BestPost", minInputPorts=1, maxInputPorts=1, doc="Calculate Post with most comments in graph", category={LogicalOperatorCategory.TRANSFORM})
public class BestPostAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 3713123005351765502L;
	private long numPosts;

	public BestPostAO() {
		super();
	}
	
	public BestPostAO(BestPostAO other) {
		super(other);
	}
	
	@Parameter(type = LongParameter.class, name="NUMPOSTS", optional=false, isList=false, doc="number of best posts to calculate")
	public void setNumPosts (long numPosts) {
		this.numPosts = numPosts;
	}
	
	@GetParameter(name="NUMPOSTS")
	public long getNumPosts() {
		return this.numPosts;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		BestPostAO newAo = new BestPostAO(this);
		newAo.numPosts = this.numPosts;
		return newAo;
	}

}
