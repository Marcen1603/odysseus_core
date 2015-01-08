package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryConstructAO;
import de.uniol.inf.is.odysseus.trajectory.physical.FulltrajectoryConstructStrategy;
import de.uniol.inf.is.odysseus.trajectory.physical.ITrajectoryConstructStrategy;
import de.uniol.inf.is.odysseus.trajectory.physical.SubtrajectoryConstructStrategy;
import de.uniol.inf.is.odysseus.trajectory.physical.TrajectoryConstructPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


/**
 * 
 * @author marcus
 *
 */
public class TTrajectoryContructAORule extends AbstractTransformationRule<TrajectoryConstructAO> {

	@Override
	public void execute(TrajectoryConstructAO operator, TransformationConfiguration config) {
		
		ITrajectoryConstructStrategy strategy = operator.getSubtrajectories() ?
				new SubtrajectoryConstructStrategy() : new FulltrajectoryConstructStrategy();
		this.defaultExecute(operator, new TrajectoryConstructPO<Tuple<ITimeInterval>>(strategy), config, true, true);
	}

	@Override
	 public boolean isExecutable(TrajectoryConstructAO operator, TransformationConfiguration transformConfig) {
		 return operator.isAllPhysicalInputSet();
	 }
	 
	 @Override
	 public String getName() {
		 return "TrajectoryConstructAO -> TrajectoryConstructPO";
	 }
	  
	 @Override
	 public IRuleFlowGroup getRuleFlowGroup() {
		 return TransformRuleFlowGroup.TRANSFORMATION;
	 }

}
