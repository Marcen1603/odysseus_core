package de.uniol.inf.is.odysseus.incubation.graph.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.ScoreUpdaterAO;
import de.uniol.inf.is.odysseus.mep.MEP;
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
		ao.setOutputSchema(ao.getInputSchema());
		
		final MapAO mapAo = new MapAO();
		String function = "scoreUpdater(" + ao.getGraphAttribute() + ", " + ao.getTimeInterval() + ")";
		SDFExpression expression = new SDFExpression(function, null, MEP.getInstance());
		NamedExpression namedExpression = new NamedExpression("result", expression, null);
		List<NamedExpression> expressions = new ArrayList<NamedExpression>();
		expressions.add(namedExpression);
		mapAo.setExpressions(expressions);
		RestructHelper.insertOperatorBefore(mapAo, ao);
		insert(mapAo);
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
	
	@Override
	public String getName() {
		return "ScoreUpdaterAO -> MapAO";
	}

}
