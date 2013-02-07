package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TProbabilisticValidatorRule extends
		AbstractTransformationRule<IHasMetadataMergeFunction<?>> {

	@Override
	public int getPriority() {
		return 1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(IHasMetadataMergeFunction<?> operator,
			TransformationConfiguration config) {
		if (!((CombinedMergeFunction) operator.getMetadataMerge())
				.providesMergeFunctionFor(IProbabilistic.class)) {
			// TODO: Make logger
			System.err.println(this
					+ " WARN: No Probabilistic merge function set for "
					+ operator);
		}
	}

	@Override
	public boolean isExecutable(IHasMetadataMergeFunction<?> operator,
			TransformationConfiguration config) {
		if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
			if (config.getMetaTypes().contains(
					IProbabilistic.class.getCanonicalName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Probabilistic Validation";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.VALIDATE;
	}

}
