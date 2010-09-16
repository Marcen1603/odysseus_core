package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TProjectAORule extends AbstractTransformationRule<ProjectAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(ProjectAO projectAO, TransformationConfiguration transformConfig) {		
		RelationalProjectPO projectPO = new RelationalProjectPO(projectAO.determineRestrictList());
		projectPO.setOutputSchema(projectAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(projectAO,projectPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(projectAO);
	}

	@Override
	public boolean isExecutable(ProjectAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getDataType().equals("relational")){
			if(operator.isAllPhysicalInputSet()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ProjectAO -> RelationalProjectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
