package de.uniol.inf.is.odysseus.recovery.incomingelements.trustpunctuation.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recovery.incomingelements.trustpunctuation.logicaloperator.TrustPunctuationReaderAO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.trustpunctuation.physicaloperator.TrustPunctuationReaderPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Rule to transform an {@link TrustPunctuationReaderAO} into a
 * {@link TrustPunctuationReaderPO}.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class TTrustPunctuationReaderAORule extends AbstractTransformationRule<TrustPunctuationReaderAO> {

	@Override
	public void execute(TrustPunctuationReaderAO operator, TransformationConfiguration config) throws RuleException {
		TrustPunctuationReaderPO<?> po = new TrustPunctuationReaderPO<>(operator.getTrustValue(),
				operator.isInRecoveryMode());
		this.defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(TrustPunctuationReaderAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "TrustPunctuationReaderAO -> TrustPunctuationReaderPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super TrustPunctuationReaderAO> getConditionClass() {
		return TrustPunctuationReaderAO.class;
	}

}