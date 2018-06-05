package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CreateNewFileNamePunctuationAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalCreateNewFilenamePunctuationPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TRelationalCreateNewFilenamePunctuationRule extends
		AbstractRelationalIntervalTransformationRule<CreateNewFileNamePunctuationAO> {

	@Override
	public void execute(CreateNewFileNamePunctuationAO operator,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new RelationalCreateNewFilenamePunctuationPO<Tuple<? extends ITimeInterval>>(operator), config, true, true);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super CreateNewFileNamePunctuationAO> getConditionClass() {
		return CreateNewFileNamePunctuationAO.class;
	}

}
