package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkAO;
import de.uniol.inf.is.odysseus.parser.pql.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.pql.DirectParameter;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;

/**
 * @author Jonas Jacobi
 */
public class BenchmarkBuilder extends AbstractOperatorBuilder {

	private static final String PROCESSING_TIME = "TIME";
	private static final String SELECTIVITY = "SELECTIVITY";

	private final DirectParameter<Double> selectivity = new DirectParameter<Double>(
			SELECTIVITY, REQUIREMENT.MANDATORY);

	private final DirectParameter<Integer> processingTimeInns = new DirectParameter<Integer>(
			PROCESSING_TIME, REQUIREMENT.MANDATORY);

	public BenchmarkBuilder() {
		setParameters(selectivity, processingTimeInns);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		Integer processingTimeInns = this.processingTimeInns.getValue();
		Double selectivity = this.selectivity.getValue();

		BenchmarkAO benchmarkAO = new BenchmarkAO(processingTimeInns,
				selectivity);

		return benchmarkAO;
	}
}
