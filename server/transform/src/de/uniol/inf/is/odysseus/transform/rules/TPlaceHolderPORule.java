package de.uniol.inf.is.odysseus.transform.rules;


import java.util.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.PlaceHolderPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPlaceHolderPORule extends AbstractTransformationRule<PlaceHolderPO<IStreamObject<?>, IStreamObject<?>>> {

	@Override
	public void execute(PlaceHolderPO<IStreamObject<?>, IStreamObject<?>> placeholder, TransformationConfiguration config)
			throws RuleException {
		// TODO Auto-generated method stub
		ILogicalOperator replacement = placeholder.getPlaceHolder().getReplacement();
		if (replacement == null) {
			throw new TransformationException("Found placeholder with no logical operator!");
		}
		Optional<IPhysicalOperator> newSource = this.getCurrentWorkingMemory().getTranslationFor(replacement);
		if (newSource.isPresent() && newSource.get() instanceof ISource) {
			@SuppressWarnings("unchecked")
			ISource<IStreamObject<?>> realTargetSource = (ISource<IStreamObject<?>>) newSource.get();
			for(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub:placeholder.getSubscriptions()) {
				// placeholder remove subscription
				sub.getSource().unsubscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(),sub.getSchema());
				
				// Does this work?
				sub.getSink().subscribeToSource(realTargetSource, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
			}
			
		}else {
			throw new TransformationException("Did not find a matching translation for logical operator "+replacement);
		}
		
		retract(placeholder);
	}

	@Override
	public boolean isExecutable(PlaceHolderPO<IStreamObject<?>, IStreamObject<?>> operator,
			TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.PLACEHOLDER;
	}
	
	@Override
	public Class<? super PlaceHolderPO<IStreamObject<?>, IStreamObject<?>>> getConditionClass() {
		return PlaceHolderPO.class;
	}

}
