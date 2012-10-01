package de.uniol.inf.is.odysseus.benchmark.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="BENCHMARKRESULT")
public class BenchmarkResultAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 5482639974274293586L;

	private long maxResults = -1;
	
	public BenchmarkResultAO() {

	}

	public BenchmarkResultAO(BenchmarkResultAO other) {
		
	}
	
	@Parameter(type = IntegerParameter.class, name = "maxResults", optional=true)
	public void setMaxResults(long maxResults) {
		this.maxResults = maxResults;
	}
	
	public long getMaxResults() {
		return maxResults;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new BenchmarkResultAO(this);
	}

}
