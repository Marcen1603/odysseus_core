package de.uniol.inf.is.odysseus.objecttracking.transform.rules.projection;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.ObjectTrackingProjectPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TObjectTrackingProjectAORule extends AbstractTransformationRule<ObjectTrackingProjectAO>{

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(ObjectTrackingProjectAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE ProjectMVPO.");
		ObjectTrackingProjectPO projectPO = new ObjectTrackingProjectPO(operator);
		projectPO.setOutputSchema(operator.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, projectPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(projectPO);
		retract(operator);
		System.out.println("CREATE ProjectMVPO finished.");
	}

	@Override
	public boolean isExecutable(ObjectTrackingProjectAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability")
//		projectAO : ObjectTrackingProjectAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "ProjectAO -> ProjectMVPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
