package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class RuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?,?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TAlgebra2BenchmarkAORule());
		rules.add(new TBatchProducerAORule());
		rules.add(new TBenchmarkAORule());
		rules.add(new TBenchmarkBufferedPipeRule());
		rules.add(new TBenchmarkDirectInterlinkBufferRule());
		rules.add(new TBenchmarkOutofOrderBufferRule());
		rules.add(new TBenchmarkStrongOrderBufferRule());
		rules.add(new TTestProducerAORule());
		return rules;
	}

}
