package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CACHE")
public class CacheAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5364109842392109977L;

	private long maxElements = -1;
	
	public CacheAO(){
	}
	
	public CacheAO(CacheAO cacheAO) {
		super(cacheAO);
		this.maxElements = cacheAO.maxElements;
	}

	@Parameter(type = IntegerParameter.class, name = "MaxElements", optional = true)
	public void setMaxElements(long maxElements) {
		this.maxElements = maxElements;
	}
	
	public long getMaxElements() {
		return maxElements;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new CacheAO(this);
	}

}
