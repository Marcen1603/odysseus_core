package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.CollectorPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PhysicalCollectorPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;

public class CollectorPOTransformationRule extends TransformationRule {

	@Override
	public int getNoOfTransformations() {
		return 1;
	}

	@Override
	public PlanOperator transform(SupportsCloneMe logicalPO, int no)
			throws TransformationNotApplicableExeception {
		CollectorPO collectorPO = (CollectorPO)logicalPO;
        PlanOperator newPlan = new PhysicalCollectorPO(collectorPO); 
        for (int i=0;i<collectorPO.getNumberOfInputs();i++){
        	newPlan.setInputPO(i,((CollectorPO)logicalPO).getPhysInputPO(i));
        }
        return newPlan;
	}

}
