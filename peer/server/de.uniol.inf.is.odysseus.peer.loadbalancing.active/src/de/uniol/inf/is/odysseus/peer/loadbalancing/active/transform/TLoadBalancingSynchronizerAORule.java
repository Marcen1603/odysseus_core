package de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.LoadBalancingSynchronizerAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingSynchronizerPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link LoadBalancingSynchronizerAO}. Any
 * {@link LoadBalancingSynchronizerAO} will be transformed into a new
 * {@link LoadBalancingSynchronizerPO}.
 * 
 * @author Michael Brand
 */
public class TLoadBalancingSynchronizerAORule extends
		AbstractTransformationRule<LoadBalancingSynchronizerAO> {

	@Override
	public int getPriority() {

		return 0;

	}

	@Override
	public void execute(LoadBalancingSynchronizerAO syncAO,
			TransformationConfiguration config) throws RuleException {

		defaultExecute(syncAO,
				new LoadBalancingSynchronizerPO<IStreamObject<ITimeInterval>>(
						syncAO), config, true, true);

	}

	@Override
	public boolean isExecutable(LoadBalancingSynchronizerAO syncAO,
			TransformationConfiguration transformConfig) {

		return syncAO.isAllPhysicalInputSet()
				&& transformConfig.getMetaTypes().contains(
						ITimeInterval.class.getCanonicalName());

	}

	@Override
	public String getName() {

		return "LoadBalancingSynchronizerAO -> LoadBalancingSynchronizerPO";

	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {

		return TransformRuleFlowGroup.TRANSFORMATION;

	}

	@Override
	public Class<? super LoadBalancingSynchronizerAO> getConditionClass() {

		return LoadBalancingSynchronizerAO.class;

	}

}