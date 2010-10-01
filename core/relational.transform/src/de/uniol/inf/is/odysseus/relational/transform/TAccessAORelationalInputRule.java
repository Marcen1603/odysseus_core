package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.access.IdentityTransformation;
import de.uniol.inf.is.odysseus.physicaloperator.access.InputStreamAccessPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAORelationalInputRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {		
		return 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG, "Standard InputStream");
		String accessPOName = accessAO.getSource().getURI(false);
		ISource<?> accessPO = new InputStreamAccessPO(accessAO.getHost(), accessAO.getPort(), new IdentityTransformation());
		accessPO.setOutputSchema(accessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(accessPO);
		retract(accessAO);
	}

	@Override
	public boolean isExecutable(AccessAO accessAO, TransformationConfiguration trafo) {
		if(WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null){
			if(accessAO.getSourceType().equals("RelationalInputStreamAccessPO")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO (RelationalInputStream) -> AccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
}
