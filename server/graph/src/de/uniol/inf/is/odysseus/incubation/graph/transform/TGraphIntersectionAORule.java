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
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.graph.functions.GraphIntersection;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.GraphIntersectionAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * GraphIntersection transformation rules.
 * 
 * @author Kristian Bruns
 */
public class TGraphIntersectionAORule extends AbstractTransformationRule<GraphIntersectionAO> {

	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(GraphIntersectionAO ao, TransformationConfiguration config) throws RuleException {
		ao.setOutputSchema(ao.getInputSchema());
		
		final AggregationAO aggregationAo = new AggregationAO();
		
		GraphIntersection<ITimeInterval, Tuple<ITimeInterval>> function = new GraphIntersection<ITimeInterval, Tuple<ITimeInterval>>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FUNCTION", "GraphIntersection");
		DirectAttributeResolver attributeResolver = new DirectAttributeResolver(ao.getInputSchema());
		
		function.createInstance(params, attributeResolver);
		
		List<IAggregationFunction> aggregations = new ArrayList<IAggregationFunction>();
		aggregations.add(function);
		aggregationAo.setAggregations(aggregations);
		
		LogicalPlan.insertOperatorBefore(aggregationAo, ao);
		insert(aggregationAo);
		LogicalPlan.removeOperator(ao, false);
		retract(ao);
	}

	@Override
	public boolean isExecutable(GraphIntersectionAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}
	
	public Class<? super GraphIntersectionAO> getConditionClass() {
		return GraphIntersectionAO.class;
	}
	
	public String getName() {
		return "GraphIntersectionAO -> AggregationAO";
	}

}
