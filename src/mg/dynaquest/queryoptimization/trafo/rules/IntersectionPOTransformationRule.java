/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.IntersectionPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.relational.RelationalIntersectionPO;

/**
 * @author Marco Grawunder
 *
 */
public class IntersectionPOTransformationRule extends TransformationRule{

	
    static private PlanOperator transformToRelationalIntersection(IntersectionPO intersectionPO) {
    	RelationalIntersectionPO po = new RelationalIntersectionPO(intersectionPO);
    	po.setLeftInput(intersectionPO.getPhysInputPO(0));
    	po.setRightInput(intersectionPO.getPhysInputPO(1));
		return po;
	}
	
    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no) {
        return transformToRelationalIntersection((IntersectionPO)logicalPO);
    }

    @Override
    public int getNoOfTransformations() {
        return 1;
    }

 
}
