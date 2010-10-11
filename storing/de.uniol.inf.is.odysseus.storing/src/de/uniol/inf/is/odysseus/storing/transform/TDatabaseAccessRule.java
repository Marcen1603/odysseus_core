package de.uniol.inf.is.odysseus.storing.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.storing.physicaloperator.DatabaseAccessPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDatabaseAccessRule extends AbstractTransformationRule<AccessAO>{

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration config) {
		String accessPOName = accessAO.getSource().getURI(false);
		ISource<?> accessPO = new DatabaseAccessPO(accessPOName);		
		accessPO.setOutputSchema(accessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(accessAO);
		insert(accessPO);		
	}

	@Override
	public boolean isExecutable(AccessAO accessAO, TransformationConfiguration config) {
		if(WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null){
			if(accessAO.getSourceType().equals("databaseReading")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO -> DatabaseAccessAO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
	

}
