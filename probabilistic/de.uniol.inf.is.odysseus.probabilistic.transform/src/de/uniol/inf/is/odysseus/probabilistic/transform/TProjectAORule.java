package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.ProbabilisticProjectPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TProjectAORule extends AbstractTransformationRule<ProjectAO> {
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ProjectAO projectAO,
			TransformationConfiguration transformConfig) {
		ProbabilisticProjectPO<?> projectPO = new ProbabilisticProjectPO<IMetaAttribute>(
				projectAO.determineRestrictList());
		defaultExecute(projectAO, projectPO, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(ProjectAO operator,
			TransformationConfiguration transformConfig) {
		if (transformConfig.getDataType().equals("probabilistic")) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ProjectAO -> ProbabilisticProjectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ProjectAO> getConditionClass() {
		return ProjectAO.class;
	}
}
