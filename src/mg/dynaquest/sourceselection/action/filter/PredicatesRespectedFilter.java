/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.sourceselection.action.filter;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;

/**
 * @author  Marco Grawunder
 */
public class PredicatesRespectedFilter extends AbstractFilterAction {

    
    public PredicatesRespectedFilter() {}
    
    public PredicatesRespectedFilter(SDFQuery query) {
        super(query);
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.action.FilterAction#positive(java.lang.Object)
     */
    public boolean positive(Object obj) {
        AnnotatedPlan ap = (AnnotatedPlan) obj;
        // Testen, ob alle in den Pr�dikaten enthaltenen Attribute
        // auch von der Quelle entgegen genommen werden k�nnen
        SDFAttributeList attrSet = getQuery().getPredicateAttributes();
        return ap.containsInAttributes(attrSet);
    }

}
