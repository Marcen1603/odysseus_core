package de.uniol.inf.is.odysseus.relational_interval.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopKAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.AbstractRelationalTopKPO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalDynamicScoreTopKPO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalTopKPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TTopKAORule extends AbstractRelationalIntervalTransformationRule<TopKAO> {

	@Override
	public void execute(TopKAO operator, TransformationConfiguration config) throws RuleException {
		IGroupProcessor<Tuple<ITimeInterval>, Tuple<ITimeInterval>> groupProcessor;
		List<SDFAttribute> grouping = operator.getGroupingAttributes();
		if (grouping != null) {
			groupProcessor = new RelationalGroupProcessor<>(operator.getInputSchema(0), operator.getOutputSchema(),
					grouping, null, operator.isFastGrouping());
		} else {
			groupProcessor = new NoGroupProcessor<Tuple<ITimeInterval>, Tuple<ITimeInterval>>();
		}

		SDFExpression setupFunction = operator.getSetupFunction() != null ? operator.getSetupFunction().expression
				: null;
		SDFExpression tearDownFunction = operator.getTearDownFunction() != null
				? operator.getTearDownFunction().expression : null;
		SDFExpression preScoreFunction = operator.getPreScoreFunction() != null
				? operator.getPreScoreFunction().expression : null;
		SDFExpression scoringFunction = operator.getScoringFunction().expression;
		SDFExpression cleanUpPredicate = operator.getCleanupPredicate() != null
				? operator.getCleanupPredicate().expression : null;

		AbstractRelationalTopKPO<Tuple<ITimeInterval>, ITimeInterval> po;
		if (!operator.isRecalcScore()) {

			po = new RelationalTopKPO<>(
					operator.getInputSchema(0), operator.getOutputSchema(), setupFunction, preScoreFunction,
					scoringFunction, tearDownFunction, cleanUpPredicate, operator.getK(), operator.isDescending(),
					operator.isSuppressDuplicates(), operator.getUniqueAttributes(), groupProcessor, operator.isTriggerOnlyByPunctuation(),
					operator.isAddScore());
		} else {
			// TODO: different Implemenations for TopK 
			po = new RelationalDynamicScoreTopKPO<>(
					operator.getInputSchema(0), operator.getOutputSchema(), setupFunction, preScoreFunction,
					scoringFunction, tearDownFunction, cleanUpPredicate, operator.getK(), operator.isDescending(),
					operator.isSuppressDuplicates(), operator.getUniqueAttributes(), groupProcessor, operator.isTriggerOnlyByPunctuation(),
					operator.isAddScore());
		}
		po.setOrderByTimestamp(operator.isTiWithTimestamp());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super TopKAO> getConditionClass() {
		return TopKAO.class;
	}

}
