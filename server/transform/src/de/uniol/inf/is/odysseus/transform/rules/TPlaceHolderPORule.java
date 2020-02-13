package de.uniol.inf.is.odysseus.transform.rules;


import java.util.Optional;
import java.util.Set;

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
		ILogicalOperator replacement = placeholder.getPlaceHolder().getReplacement();
		Optional<IPhysicalOperator> newSource = determineSource(replacement);
		if (newSource.isPresent() && newSource.get() instanceof ISource) {
			@SuppressWarnings("unchecked")
			ISource<IStreamObject<?>> realTargetSource = (ISource<IStreamObject<?>>) newSource.get();
			replaceOperator(placeholder, realTargetSource);	
		}else {
			throw new TransformationException("Did not find a matching translation for logical operator "+replacement);
		}
		
		retract(placeholder);
	}

	private Optional<IPhysicalOperator> determineSource(ILogicalOperator replacement) {
		if (replacement == null) {
			throw new TransformationException("Found placeholder with no logical operator!");
		}
		// This logical operator is not found in the current plan, because it gets cloned (maybe many times)
		// First try to find the logical operator that is the final target of the clone operation 
		Set<ILogicalOperator> currentLogOps = getCurrentWorkingMemory().getAllKeysForTranslations();
		ILogicalOperator clonedReplacement = null;
		for (ILogicalOperator op: currentLogOps) {
			if (isClonedFrom(op, replacement)) {
				clonedReplacement = op;
				break;
			}
		}
		if (clonedReplacement == null) {
			throw new TransformationException("Cannot create rekursive query. Operator not found!");
		}
		
		Optional<IPhysicalOperator> newSource = this.getCurrentWorkingMemory().getTranslationFor(clonedReplacement);
		return newSource;
	}
	
	private boolean isClonedFrom(ILogicalOperator target, ILogicalOperator origin) {
		ILogicalOperator op = target;
		while(op.getClonedFrom().isPresent()) {
			if (op.getClonedFrom().get() == origin) {
				return true;
			}else {
				op = op.getClonedFrom().get();
			}
		}
		return false;
	}

	private void replaceOperator(PlaceHolderPO<IStreamObject<?>, IStreamObject<?>> placeholder,
			ISource<IStreamObject<?>> realTargetSource) {
		for(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub:placeholder.getSubscriptions()) {
			// placeholder remove subscription
			sub.getSource().unsubscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(),sub.getSchema());
			
			// subscribe new source
			sub.getSink().subscribeToSource(realTargetSource, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
		}
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
