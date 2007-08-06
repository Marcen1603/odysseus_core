/*
 * Created on 01.02.2005
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;

/**
 * @author Marco Grawunder
 *
 */
abstract public class TransformationRule {

    // Wendet auf den logischen Operator die Transformation no an
    abstract public PlanOperator transform(SupportsCloneMe logicalPO, int no) 
     throws TransformationNotApplicableExeception;
    // Auslesen der Menge der möglichen Transformationen
    abstract public int getNoOfTransformations();
    
    public static void main(String[] args) {
    }
}
