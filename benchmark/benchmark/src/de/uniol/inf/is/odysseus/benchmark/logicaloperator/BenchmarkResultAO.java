package de.uniol.inf.is.odysseus.benchmark.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="BENCHMARKRESULT")
public class BenchmarkResultAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 5482639974274293586L;

	private long maxResults = -1;
	private String resultType = "Latency";

	private String statisticsType = "ALL";
	
	public BenchmarkResultAO() {

	}

	public BenchmarkResultAO(BenchmarkResultAO other) {
		super(other);
		maxResults = other.maxResults;
		resultType = other.resultType;
		statisticsType = other.statisticsType;
	}
	
	@Parameter(type = IntegerParameter.class, name = "maxResults", optional=true)
	public void setMaxResults(long maxResults) {
		this.maxResults = maxResults;
	}
	
	public long getMaxResults() {
		return maxResults;
	}
	

	public String getResultType() {
		return resultType;
	}

	@Parameter(type = StringParameter.class, name = "resultType", optional=true)
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}	

	@Override
	public AbstractLogicalOperator clone() {
		return new BenchmarkResultAO(this);
	}

	public String getStatisticsType() {
		return statisticsType;
	}
	@Parameter(type = StringParameter.class, name = "statistics", optional=true)
	public void setStatisticsType(String statisticsType) {
		this.statisticsType = statisticsType;
	}

}
