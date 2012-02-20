package de.uniol.inf.is.odysseus.salsa.playground.transform;

import java.io.IOException;

import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.NIOServerSocketSource;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessNIOServerSocketSourceRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {		
		return 1;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG, "NIOServerSocketSource");
		String accessPOName = accessAO.getSource().getURI(false);
		try {
			
			ISource<?> accessPO = new NIOServerSocketSource(accessAO.getHost(), accessAO.getPort());
			accessPO.setOutputSchema(accessAO.getOutputSchema());	
			getDataDictionary().putAccessPlan(accessPOName, accessPO);
			replace(accessAO, accessPO, trafo);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean isExecutable(AccessAO accessAO, TransformationConfiguration trafo) {
		if(getDataDictionary().getAccessPlan(accessAO.getSource().getURI()) == null){
			if(accessAO.getSourceType().equals("NIOServerSocketSource")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO(NIOServerSocketSource) -> NIOServerSocketSource";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
}
