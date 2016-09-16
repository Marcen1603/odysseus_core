package de.uniol.inf.is.odysseus.incubation.graph.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.CountNodesAO;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TCountNodesAORule extends AbstractTransformationRule<CountNodesAO> {

	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(CountNodesAO ao, TransformationConfiguration config) throws RuleException {
		ao.setOutputSchema(ao.getInputSchema());
		
		final MapAO mapAo = new MapAO();
		String function = "countNodes(" + ao.getGraphAttribute() + ")";
		SDFExpression expression = new SDFExpression(function, null, MEP.getInstance());
		NamedExpression namedExpression = new NamedExpression("nodeAmount", expression, null);
		List<NamedExpression> expressions = new ArrayList<NamedExpression>();
		expressions.add(namedExpression);
		mapAo.setExpressions(expressions);
		RestructHelper.insertOperatorBefore(mapAo, ao);
		insert(mapAo);
		RestructHelper.removeOperator(ao, false);
		retract(ao);
	}

	@Override
	public boolean isExecutable(CountNodesAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}
	
	public Class<? super CountNodesAO> getConditionClass() {
		return CountNodesAO.class;
	}
	
	@Override
	public String getName() {
		return "CountNodesAO -> MapAO";
	}

}
