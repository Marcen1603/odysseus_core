/*
 * Created on 01.02.2005
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import java.util.List;

import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;

/**
 * @author Marco Grawunder
 *
 */
public class SelectJoinPOTransformationRule extends TransformationRule {

    /* (non-Javadoc)
     * @see mg.dynaquest.queryoptimization.trafo.rules.TransformationRule#transform(mg.dynaquest.queryexecution.po.base.PlanOperator)
     */
    public List<PlanOperator> transform(SupportsCloneMe logicalPO) {
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String[] args) {
    }

    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNoOfTransformations() {
        // TODO Auto-generated method stub
        return 0;
    }
}
