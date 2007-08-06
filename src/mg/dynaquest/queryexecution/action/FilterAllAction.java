/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.queryexecution.action;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;

/**
 * @author Marco Grawunder
 *
 * Filter, der kein Objekt passieren lässt (Zu Test-Zwecken)
 * 
 */
public class FilterAllAction implements FilterAction {

    /* 
     * Alle Objekte werden gefiltert
     */
    public boolean positive(Object obj) {
        return false;
    }
    
	
    public String getActionName()
    {
    	return "";
    }
    public void setQuery(SDFQuery query)
    {
    }

    public static void main(String[] args) {
    }
}
