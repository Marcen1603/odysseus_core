/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.sourceselection.action.filter;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.query.WSimplePredicateSet;
import mg.dynaquest.sourceselection.AnnotatedPlan;

/**
 * @author  Marco Grawunder
 */
public class OperatorCompatibiltyFilter extends AbstractFilterAction {
	   
    public OperatorCompatibiltyFilter() {}
    
    public OperatorCompatibiltyFilter(SDFQuery query) {
        super(query);
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.action.FilterAction#positive(java.lang.Object)
     */
    public boolean positive(Object obj) {
        // Hier müssen die Operatoren der Prädikate der Anfrage und die der
        // Quelle verglichen werden
        // Alle Prädikate der Anfrage durchgehen und schauen, ob dies
        // von der Quelle ausführbar ist
        AnnotatedPlan ap = (AnnotatedPlan) obj;
        WSimplePredicateSet predSet = getQuery().getQueryPredicates();
        for (int i=0;i<predSet.getPredicateCount();i++){
            SDFSimplePredicate pred =  predSet.getPredicate(i).getPredicate();
            if (!ap.canProcess(pred)){
                return false;
            }
        }
        return true;
    }

}
