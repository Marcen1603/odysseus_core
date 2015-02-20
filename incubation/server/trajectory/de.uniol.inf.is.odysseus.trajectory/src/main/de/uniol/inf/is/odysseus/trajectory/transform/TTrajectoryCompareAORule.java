package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryCompareAO;
import de.uniol.inf.is.odysseus.trajectory.physicaloperator.TrajectoryComparePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TTrajectoryCompareAORule extends AbstractTransformationRule<TrajectoryCompareAO> {
	
	 @Override
	 public int getPriority() {
		 return 0;
	 }
	 
	@Override
	 public void execute(final TrajectoryCompareAO trajectoryAO, final TransformationConfiguration config) {
		 
		 this.defaultExecute(
				 trajectoryAO, 
				 new TrajectoryComparePO<Tuple<ITimeInterval>>(
						 trajectoryAO.getK(),
						 trajectoryAO.getQueryTrajectory().getAbsolutePath(),
						 trajectoryAO.getUtmZone(),
						 trajectoryAO.getLambda(),
						 trajectoryAO.getAlgorithm(),
						 trajectoryAO.getTextualAttr(),
						 trajectoryAO.getOptionsMap()),
				 config, 
				 true,
				 true);
	 }
	 
	 @Override
	 public boolean isExecutable(final TrajectoryCompareAO operator, final TransformationConfiguration transformConfig) {
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
