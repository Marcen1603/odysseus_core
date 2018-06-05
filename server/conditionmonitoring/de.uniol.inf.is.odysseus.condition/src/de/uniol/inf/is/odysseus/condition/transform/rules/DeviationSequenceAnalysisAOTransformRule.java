package de.uniol.inf.is.odysseus.condition.transform.rules;

import java.util.List;

import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationSequenceAnalysisAO;
import de.uniol.inf.is.odysseus.condition.physicaloperator.DeviationSequenceAnalysisPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class DeviationSequenceAnalysisAOTransformRule extends AbstractTransformationRule<DeviationSequenceAnalysisAO> {

	@Override
	public void execute(DeviationSequenceAnalysisAO operator, TransformationConfiguration config) throws RuleException {

		// Defining the group processor
		IGroupProcessor<Tuple<ITimeInterval>, Tuple<ITimeInterval>> groupProcessor;
		List<SDFAttribute> grouping = operator.getGroupingAttributes();
		if (grouping != null) {
			groupProcessor = new RelationalGroupProcessor<>(operator.getInputSchema(0), operator.getOutputSchema(),
					grouping, null, operator.isFastGrouping());
		} else {
			groupProcessor = new NoGroupProcessor<Tuple<ITimeInterval>, Tuple<ITimeInterval>>();
		}

		DeviationSequenceAnalysisPO<Tuple<ITimeInterval>, ITimeInterval> po = new DeviationSequenceAnalysisPO<Tuple<ITimeInterval>, ITimeInterval>(
				operator, groupProcessor);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(DeviationSequenceAnalysisAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class && operator.getInputSchema(1).getType() == Tuple.class) {
			return operator.isAllPhysicalInputSet();
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
