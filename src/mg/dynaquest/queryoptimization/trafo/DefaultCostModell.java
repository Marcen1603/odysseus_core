/*
 * Created on 15.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo;

import java.util.ArrayList;

import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;

/**
 * @author Marco Grawunder
 *
 */
public class DefaultCostModell implements Costmodell {

    public double calcCosts(NAryPlanOperator physicalPlan) {
        // Die Kosten der Kinder berechnen
        double subtreeCosts = 0;
        for (int i=0;i<physicalPlan.getNumberOfInputs();i++){
            subtreeCosts = subtreeCosts + calcCosts((NAryPlanOperator) physicalPlan.getInputPO(i));
        }
        // Die eigenen Kosten berechnen
        double cpu = physicalPlan.getCPUCost();
        double mem = physicalPlan.getMemCost();
        double communicationCost = physicalPlan.getCommCost(); 
        
        return subtreeCosts+cpu+mem+communicationCost;
    }
    
    public double calcCosts(NAryPlanOperator physicalPlan, SDFQuery query, ArrayList context)
    {
    	return 0;
    }

}
