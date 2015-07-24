package de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator;




import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAO;
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAORule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public abstract class AbstractODLAORule<T extends IODLAO> extends AbstractTransformationRule<T> implements IODLAORule<T> {

	@Override
	public boolean isExecutable(IODLAO operator,TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}



}
