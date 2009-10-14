package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.BenchmarkAO;
import de.uniol.inf.is.odysseus.parser.pql.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class BenchmarkBuilder extends AbstractOperatorBuilder {

	private static final String PROCESSING_TIME = "TIME";
	private static final String SELECTIVITY = "SELECTIVITY";

	@Override
	public ILogicalOperator createOperator(Map<String, Object> parameters,
			List<ILogicalOperator> inputOps) {

		Integer processingTimeInns = getParameter(parameters, PROCESSING_TIME,
				Integer.class);
		Double selectivity = getParameter(parameters, SELECTIVITY, Double.class);

		BenchmarkAO benchmarkAO = new BenchmarkAO(processingTimeInns,
				selectivity);
		createSubscriptions(benchmarkAO, inputOps, 1);
		SDFAttributeList schema = inputOps.get(0).getOutputSchema();
		benchmarkAO.setInputSchema(0, schema);
		benchmarkAO.setOutputSchema(schema);
		return benchmarkAO;
	}
}
