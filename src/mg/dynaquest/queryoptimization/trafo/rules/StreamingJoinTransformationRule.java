package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.streaming.relational.RelationalStreamingRippleJoinPO;

public class StreamingJoinTransformationRule extends TransformationRule {

	@Override
	public int getNoOfTransformations() {
		return 1;
	}

	@Override
	public PlanOperator transform(SupportsCloneMe logicalPO, int no)
			throws TransformationNotApplicableExeception {
		return transformToTemporalRippleJoin((JoinPO) logicalPO);
	}
	

	private PlanOperator transformToTemporalRippleJoin(JoinPO joinPO) {
		RelationalStreamingRippleJoinPO newPlan = new RelationalStreamingRippleJoinPO(joinPO);
		newPlan.setInputPO(0, (PlanOperator)joinPO.getPhysInputPO(0));
        newPlan.setInputPO(1, (PlanOperator)joinPO.getPhysInputPO(1));
        return newPlan;
	}

}
