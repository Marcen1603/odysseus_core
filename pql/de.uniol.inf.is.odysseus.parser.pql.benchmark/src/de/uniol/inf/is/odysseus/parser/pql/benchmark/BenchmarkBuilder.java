package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.pql.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

import de.uniol.inf.is.odysseus.benchmarker.impl.BenchmarkAO;

public class BenchmarkBuilder extends AbstractOperatorBuilder {

	private static final String PROCESSING_TIME = "TIME";
	private static final String SELECTIVITY = "SELECTIVITY";

	@Override
	public ILogicalOperator createOperator(Map<String, Object> parameters,
			List<ILogicalOperator> inputOps) {

		Integer processingTimeInns = getParameter(parameters, PROCESSING_TIME,
				Integer.class);
		Double selectivity = getParameter(parameters, SELECTIVITY, Double.class);

		SDFAttributeList schema = inputOps.get(0).getOutputSchema();	
		BenchmarkAO benchmarkAO = new BenchmarkAO(processingTimeInns,
				selectivity);
		createSubscriptions(benchmarkAO, inputOps, 1, schema);

		return benchmarkAO;
	}
}
