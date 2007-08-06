package mg.dynaquest.queryexecution.action;

import mg.dynaquest.queryexecution.action.FilterAction;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;

// Liefert unhängig von den Daten immer
// abwechselnd wahr oder falsch
// Zum Testen
public class ToggleFilterAction implements FilterAction {
	/**
	 * @uml.property  name="retValue"
	 */
	private boolean retValue;

	public boolean positive(Object obj) {
		retValue = !retValue;
		return retValue;
	}
	
	
    public String getActionName()
    {
    	return "";
    }
    public void setQuery(SDFQuery query)
    {
    }
}