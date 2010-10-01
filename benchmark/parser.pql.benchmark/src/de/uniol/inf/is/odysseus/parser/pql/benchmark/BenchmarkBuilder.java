package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

/**
 * @author Jonas Jacobi
 */
public class BenchmarkBuilder extends AbstractOperatorBuilder {

	private static final String PROCESSING_TIME = "TIME";
	private static final String SELECTIVITY = "SELECTIVITY";

	private final DirectParameter<Double> selectivity = new DirectParameter<Double>(
			SELECTIVITY, REQUIREMENT.MANDATORY);

	private final IntegerParameter processingTimeInns = new IntegerParameter(
			PROCESSING_TIME, REQUIREMENT.MANDATORY);

	public BenchmarkBuilder() {
		super(1, 1);
		setParameters(selectivity, processingTimeInns);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		Integer processingTimeInns = this.processingTimeInns.getValue();
		Double selectivity = this.selectivity.getValue();

		BenchmarkAO benchmarkAO = new BenchmarkAO(processingTimeInns,
				selectivity);

		return benchmarkAO;
	}

	@Override
	protected boolean internalValidation() {
		boolean isValid = true;
		if (selectivity.getValue() < 0) {
			addError(new IllegalParameterException("selectivity has to be greater than 0"));
			isValid = false;
		}
		if (processingTimeInns.getValue() < 0) {
			addError(new IllegalParameterException("time has to be greater than 0"));
			isValid = false;
		}
		return isValid;
	}
}
