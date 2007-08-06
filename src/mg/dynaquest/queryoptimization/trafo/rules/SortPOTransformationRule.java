/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.SortPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.relational.SortMemPO;

/**
 * @author Marco Grawunder
 *
 */
public class SortPOTransformationRule extends TransformationRule{

   
    private static PlanOperator transformToSimpleSortPO(SortPO sortPO) {
        PlanOperator newPlan = new SortMemPO(sortPO);
       	newPlan.setInputPO(0,sortPO.getPhysInputPO());
        return newPlan;
    }
 
	
    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no){
        return transformToSimpleSortPO((SortPO)logicalPO);
    }

    @Override
    public int getNoOfTransformations() {
        // TODO Auto-generated method stub
        return 1;
    }
}
