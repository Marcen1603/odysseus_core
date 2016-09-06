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
import de.uniol.inf.is.odysseus.incubation.graph.functions.BestPostsDebs;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.BestPostsDebsAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBestPostsDebsAORule extends AbstractTransformationRule<BestPostsDebsAO> {

	@Override
	public void execute(BestPostsDebsAO ao, TransformationConfiguration config) throws RuleException {
		ao.setOutputSchema(ao.getInputSchema());
		
		final AggregationAO aggregationAo = new AggregationAO();
		
		BestPostsDebs<ITimeInterval, Tuple<ITimeInterval>> function = new BestPostsDebs<ITimeInterval, Tuple<ITimeInterval>>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FUNCTION", "BestPostsDebs");
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
	public boolean isExecutable(BestPostsDebsAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}
	
	public Class<? super BestPostsDebsAO> getConditionClass() {
		return BestPostsDebsAO.class;
	}
	
	public String getName() {
		return "BestPostsDebsAO -> AggregationAO";
	}

}
