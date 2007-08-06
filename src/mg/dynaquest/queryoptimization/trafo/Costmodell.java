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
public interface Costmodell {

    public double calcCosts(NAryPlanOperator physicalPlan);
    
    public double calcCosts(NAryPlanOperator physicalPlan, SDFQuery query, ArrayList context);
    
}
