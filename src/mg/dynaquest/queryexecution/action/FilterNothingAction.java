/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.queryexecution.action;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;

/**
 * @author Marco Grawunder
 *
 * Einfacher Filter, der alle Elemente passieren lässt 
 *
 */
public class FilterNothingAction implements FilterAction {

    /* Liefert immer true
     * @see mg.dynaquest.queryexecution.action.FilterAction#positive(java.lang.Object)
     */
    public boolean positive(Object obj) {
        return true;
    }
    
	
    public String getActionName()
    {
    	return "";
    }
    public void setQuery(SDFQuery query)
    {
    }
}
