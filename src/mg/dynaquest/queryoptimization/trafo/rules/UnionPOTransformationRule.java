/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.relational.RelationalUnionPO;

/**
 * @author Marco Grawunder
 *
 */
public class UnionPOTransformationRule extends TransformationRule{


    static private PlanOperator transformToRelationalUnion(UnionPO unionPO) {
    	RelationalUnionPO po = new RelationalUnionPO(unionPO);
    	po.setLeftInput(unionPO.getPhysInputPO(0));
    	po.setRightInput(unionPO.getPhysInputPO(1));
		return po;
	}

	
    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no) {
        return transformToRelationalUnion((UnionPO) logicalPO);
    }
    
	@Override
    public int getNoOfTransformations() {
        return 1;
    }
}
