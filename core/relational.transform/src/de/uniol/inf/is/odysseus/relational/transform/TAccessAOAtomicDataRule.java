package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.access.AtomicDataInputStreamAccessPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAOAtomicDataRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration transformConfig) {
		String accessPOName = accessAO.getSource().getURI(false);
		ISource accessPO = new AtomicDataInputStreamAccessPO(accessAO.getHost(), accessAO.getPort(), accessAO.getOutputSchema());
		accessPO.setOutputSchema(accessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(accessAO);
		insert(accessPO);
		
	}

	@Override
	public boolean isExecutable(AccessAO accessAO, TransformationConfiguration transformConfig) {		
		if(WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null){
			if(accessAO.getSourceType().equals("RelationalAtomicDataInputStreamAccessPO") || accessAO.getSourceType().equals("RelationalStreaming")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO (AtomicData) -> AccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;		
	}


}
