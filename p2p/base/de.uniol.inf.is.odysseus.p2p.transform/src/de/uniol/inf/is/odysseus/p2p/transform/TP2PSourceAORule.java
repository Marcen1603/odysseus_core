package de.uniol.inf.is.odysseus.p2p.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSourceAO;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base.P2PInputStreamAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.access.IdentityTransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TP2PSourceAORule extends AbstractTransformationRule<P2PSourceAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(P2PSourceAO p2pSourceAO, TransformationConfiguration transformConfig) {
		P2PInputStreamAccessPO<?,?> p2pAccessPO = new P2PInputStreamAccessPO(new IdentityTransformation(), p2pSourceAO.getAdv(), PeerGroupTool.getPeerGroup());
		p2pAccessPO.setOutputSchema(p2pSourceAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(p2pSourceAO, p2pAccessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(p2pSourceAO);
	}

	@Override
	public boolean isExecutable(P2PSourceAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "P2PSourceAO -> P2PInputStreamAccessPO";
	}

	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
