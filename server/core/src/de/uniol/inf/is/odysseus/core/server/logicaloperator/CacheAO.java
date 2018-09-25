package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CACHE", doc="This operator can keep some stream elements. At runtime, every time a new operator is connected it will get the cached elements. This can be usefull when reading from a csv file and multiple parts of a query need this information.", category={LogicalOperatorCategory.PROCESSING})
public class CacheAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -5364109842392109977L;

	private long maxElements = -1;

	private TimeValueItem timeSize = null;
	
	public CacheAO(){
	}
	
	public CacheAO(CacheAO cacheAO) {
		super(cacheAO);
		this.maxElements = cacheAO.maxElements;
		this.timeSize = cacheAO.timeSize;
	}

	@Parameter(type = IntegerParameter.class, name = "MaxElements", optional = true)
	public void setMaxElements(long maxElements) {
		this.maxElements = maxElements;
	}
	
	
	@Parameter(type = TimeParameter.class, name = "TimeSize", optional = true)
	public void setWindowSize(TimeValueItem windowSize) {
		this.timeSize = windowSize;
	}
	
	public long getMaxElements() {
		return maxElements;
	}
	
	public TimeValueItem getTimeSize() {
		return timeSize;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new CacheAO(this);
	}

}
