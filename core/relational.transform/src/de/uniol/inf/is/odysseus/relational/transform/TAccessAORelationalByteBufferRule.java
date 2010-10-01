package de.uniol.inf.is.odysseus.relational.transform;

import java.io.IOException;
import java.util.Collection;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.access.ByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleObjectHandler;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAORelationalByteBufferRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration transformConfig) {
		String accessPOName = accessAO.getSource().getURI(false);
		ISource accessPO = null;
		try {
			accessPO = new ByteBufferReceiverPO(new RelationalTupleObjectHandler(accessAO.getOutputSchema()), accessAO.getHost(), accessAO.getPort());
		} catch (IOException e) {			
			e.printStackTrace();
		}
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
			if(accessAO.getSourceType().equals("RelationalByteBufferAccessPO")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {		
		return "AccessAO (RelationalByteBuffer) -> AccessPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

}
