/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.sourceselection.action.filter;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourceselection.AnnotatedPlan;

/**
 * @author  Marco Grawunder  Die Klasse �berpr�ft, ob bei einem annotierten Plan alle Ausgabeattribute  enthalten sind, die in der entsprechenden Anfrage gesucht sind
 */
public class NecessaryOutAttributesFilter extends AbstractFilterAction {
 
    
    public NecessaryOutAttributesFilter(){ }
    
    public NecessaryOutAttributesFilter(SDFQuery query){
        super(query);
    }
    
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.action.FilterAction#positive(java.lang.Object)
     */
    public boolean positive(Object obj) {
        // Es muss hier �berpr�ft werden, ob alle in der Anfrage als
        // notwendig definierten Attribute in dem �bergebenen vom Typ 
        // AnnotatedPlan enthalten sind
        AnnotatedPlan plan = (AnnotatedPlan) obj;
        return plan.containsOutAttributes(getQuery().getRequiredAttributes());
    }


}
