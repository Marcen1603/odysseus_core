package de.uniol.inf.is.odysseus.incubation.graph.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.graph.functions.ScoreUpdater;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.ScoreUpdaterAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TScoreUpdaterAORule extends AbstractTransformationRule<ScoreUpdaterAO> {

	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(ScoreUpdaterAO ao, TransformationConfiguration config) throws RuleException {				
		final AggregationAO aggregationAo = new AggregationAO();
		
		ScoreUpdater<ITimeInterval, Tuple<ITimeInterval>> function = new ScoreUpdater<ITimeInterval, Tuple<ITimeInterval>>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FUNCTION", "ScoreUpdater");
		DirectAttributeResolver attributeResolver = new DirectAttributeResolver(ao.getInputSchema());
		
		function.createInstance(params, attributeResolver);
		
		List<IAggregationFunction> aggregations = new ArrayList<IAggregationFunction>();
		aggregations.add(function);
		aggregationAo.setAggregations(aggregations);
		
		RestructHelper.insertOperatorBefore(aggregationAo, ao);
		insert(aggregationAo);
		RestructHelper.removeOperator(ao, false);
		retract(ao);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}
	
	public Class<? super ScoreUpdaterAO> getConditionClass() {
		return ScoreUpdaterAO.class;
	}

	@Override
	public boolean isExecutable(ScoreUpdaterAO operator, TransformationConfiguration config) {
		return true;
	}

}
