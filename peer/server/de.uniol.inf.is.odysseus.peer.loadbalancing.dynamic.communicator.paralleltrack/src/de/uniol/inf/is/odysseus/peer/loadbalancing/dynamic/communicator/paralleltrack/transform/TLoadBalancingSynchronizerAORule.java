package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.paralleltrack.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.paralleltrack.logicaloperator.LoadBalancingSynchronizerAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.paralleltrack.physicaloperator.LoadBalancingSynchronizerPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.AbstractIntervalTransformationRule;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * The rule of transformation for the {@link LoadBalancingSynchronizerAO}. Any
 * {@link LoadBalancingSynchronizerAO} will be transformed into a new
 * {@link LoadBalancingSynchronizerPO}.
 * 
 * @author Michael Brand
 */
public class TLoadBalancingSynchronizerAORule extends
		AbstractIntervalTransformationRule<LoadBalancingSynchronizerAO> {

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