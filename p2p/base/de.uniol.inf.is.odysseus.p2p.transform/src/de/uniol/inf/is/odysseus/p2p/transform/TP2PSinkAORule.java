package de.uniol.inf.is.odysseus.p2p.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base.P2PSinkPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TP2PSinkAORule extends AbstractTransformationRule<P2PSinkAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(P2PSinkAO p2pSinkAO, TransformationConfiguration trafo) {		
		P2PSinkPO<?> p2pSinkPO = new P2PSinkPO(p2pSinkAO.getAdv(), PeerGroupTool.getPeerGroup());
		p2pSinkPO.setOutputSchema(p2pSinkAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(p2pSinkAO, p2pSinkPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(p2pSinkAO);
	}

	@Override
	public boolean isExecutable(P2PSinkAO operator, TransformationConfiguration transformConfig) {		
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "P2pSinkAO -> P2pSinkPO";
	}
	
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
