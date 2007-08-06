/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.DifferencePO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.relational.RelationalDifferencePO;

/**
 * @author Marco Grawunder
 *
 */
public class DifferencePOTransformationRule extends TransformationRule{


    /**
     * Transformationsregel, die in einem Plan den übergebenen logischen Planoperator
     * in einen physischen Operator umsetzt
     * @param differencePO ist der logische umzusentzende Planoperator
     * @return den neuen Operator inklusive der notwendigen Eingaben
     */
    private static PlanOperator transformToRelDifference(DifferencePO differencePO) {
        PlanOperator newPlan = new RelationalDifferencePO();
        newPlan.setInputPO(0, differencePO.getPhysInputPO(0));
        newPlan.setInputPO(1, differencePO.getPhysInputPO(1));
        return newPlan;
    }


    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no) {
        return transformToRelDifference((DifferencePO)logicalPO);
    }


    @Override
    public int getNoOfTransformations() {
        return 1;
    }
}
