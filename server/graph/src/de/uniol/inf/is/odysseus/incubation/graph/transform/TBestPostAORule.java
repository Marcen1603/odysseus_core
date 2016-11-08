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
import de.uniol.inf.is.odysseus.incubation.graph.functions.BestPost;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.BestPostAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Best Post transformation rules.
 * @author Kristian
 *
 */
public class TBestPostAORule extends AbstractTransformationRule<BestPostAO> {

	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(BestPostAO ao, TransformationConfiguration config) throws RuleException {
		ao.setOutputSchema(ao.getInputSchema());

		final AggregationAO aggregationAo = new AggregationAO();

		BestPost<ITimeInterval, Tuple<ITimeInterval>> function = new BestPost<ITimeInterval, Tuple<ITimeInterval>>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FUNCTION", "BestPost");
		params.put("numPosts", ao.getNumPosts());
		DirectAttributeResolver attributeResolver = new DirectAttributeResolver(ao.getInputSchema());

		IAggregationFunction instance = function.createInstance(params, attributeResolver);

		List<IAggregationFunction> aggregations = new ArrayList<IAggregationFunction>();
		aggregations.add(instance);
		aggregationAo.setAggregations(aggregations);


		RestructHelper.insertOperatorBefore(aggregationAo, ao);
		insert(aggregationAo);
		RestructHelper.removeOperator(ao, false);
		retract(ao);
	}

	@Override
	public boolean isExecutable(BestPostAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

	public Class<? super BestPostAO> getConditionClass() {
		return BestPostAO.class;
	}

	@Override
	public String getName() {
		return "BestPostAO -> AggregationAO";
	}

}
