package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryCompareAO;
import de.uniol.inf.is.odysseus.trajectory.physical.TrajectoryComparePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TTrajectoryCompareAORule extends AbstractTransformationRule<TrajectoryCompareAO> {

	 @Override
	 public int getPriority() {
		 return 0;
	 }
	 
	  
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	 public void execute(TrajectoryCompareAO trajectoryAO, TransformationConfiguration config) {
		 this.defaultExecute(trajectoryAO, new TrajectoryComparePO(trajectoryAO.getK(), trajectoryAO.getQueryTrajectory(), trajectoryAO.getReferenceSystem()), config, true ,true);
	 }
	 
	 @Override
	 public boolean isExecutable(TrajectoryCompareAO operator, TransformationConfiguration transformConfig) {
		 return operator.isAllPhysicalInputSet();
	 }
	 
	 @Override
	 public String getName() {
		 return "TrajectoryCompareAO -> TrajectoryComparePO";
	 }
	  
	 @Override
	 public IRuleFlowGroup getRuleFlowGroup() {
		 return TransformRuleFlowGroup.TRANSFORMATION;
	 }
}
