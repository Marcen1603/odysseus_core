/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.relational.RelationalProjectPO;

/**
 * @author Marco Grawunder
 *
 */
public class ProjectPOTransformationRule extends TransformationRule{

  
    
    private static PlanOperator transformToRelationalProject(ProjectPO projectPO) {
        PlanOperator newPlan = new RelationalProjectPO(projectPO);
       	newPlan.setInputPO(0,projectPO.getPhysInputPO());
        return newPlan;
    }

    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no) {
        return transformToRelationalProject((ProjectPO)logicalPO);
    }

    @Override
    public int getNoOfTransformations() {
        return 1;
    }


    
    
}
