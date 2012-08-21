package de.uniol.inf.is.odysseus.interval.transform;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.SecurityShieldPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSecurityShieldAORule extends AbstractTransformationRule<TopAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(TopAO topAO,
			TransformationConfiguration transformConfig) {
		if(transformConfig.getOption("isSecurityAware") != null) {		
			if(transformConfig.getOption("isSecurityAware")) {
				Collection<IPhysicalOperator> physInputPOs = topAO.getPhysInputPOs();
				ISource oldFather = null;

				Collection<ILogicalOperator> topAOCollection = new ArrayList<ILogicalOperator>();
				topAOCollection.add(topAO);
				
				for(IPhysicalOperator e:physInputPOs) {
					oldFather = (ISource<?>) e;
					IPipe securityShieldPO = new SecurityShieldPO();
					securityShieldPO.setOutputSchema(oldFather.getOutputSchema()); 					
					transformConfig.getTransformationHelper().insertNewFather(oldFather, topAOCollection, securityShieldPO);
				}
						
			}	
		}
	}

	@Override
	public boolean isExecutable(TopAO topAO,
			TransformationConfiguration config) {		
		return true;
//		return topAO.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SecurityShieldAO -> SecurityShieldPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SECURITY;
	}

	@Override
	public Class<? super TopAO> getConditionClass() {	
		return TopAO.class;
	}


}
