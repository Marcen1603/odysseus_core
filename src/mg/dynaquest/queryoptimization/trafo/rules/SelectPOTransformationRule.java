/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.relational.RelationalSelectPO;

/**
 * @author Marco Grawunder
 *
 */
public class SelectPOTransformationRule extends TransformationRule{
   
    private static PlanOperator transformToRelationalSelect(SelectPO selectPO) {
        PlanOperator newPlan = new RelationalSelectPO(selectPO);
       	newPlan.setInputPO(0,selectPO.getPhysInputPO());
        return newPlan;
    }

    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no) {
        return transformToRelationalSelect((SelectPO)logicalPO);
    }

    @Override
    public int getNoOfTransformations() {
        return 1;
    }
}
