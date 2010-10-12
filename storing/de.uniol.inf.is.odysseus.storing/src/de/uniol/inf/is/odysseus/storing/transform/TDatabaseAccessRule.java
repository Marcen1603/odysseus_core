package de.uniol.inf.is.odysseus.storing.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.storing.logicaloperator.DatabaseAccessAO;
import de.uniol.inf.is.odysseus.storing.physicaloperator.DatabaseAccessPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDatabaseAccessRule extends AbstractTransformationRule<DatabaseAccessAO>{

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(DatabaseAccessAO accessAO, TransformationConfiguration config) {
		String accessPOName = accessAO.getSource().getURI(false);		
		ISource<?> accessPO = null;	
		if(WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null){
			accessPO = new DatabaseAccessPO(accessAO.getTable());
			accessPO.setOutputSchema(accessAO.getOutputSchema());
			WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);			
		}else{
			accessPO = WrapperPlanFactory.getAccessPlan(accessPOName);
		}
		
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(accessAO);
		insert(accessPO);		
	}

	@Override
	public boolean isExecutable(DatabaseAccessAO accessAO, TransformationConfiguration config) {		
		return true;					
	}

	@Override
	public String getName() {
		return "DatabaseAccessAO -> DatabaseAccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
	

}
