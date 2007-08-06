package mg.dynaquest.queryexecution.action;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:12 $ Version:
 * $Revision: 1.2 $ Log: $Log: FilterAction.java,v $ $Revision: 1.2 $ Log:
 * Revision 1.2 2004/09/16 08:57:12 grawund $Revision: 1.2 $ Log: Quellcode
 * durch Eclipse formatiert $Revision: 1.2 $ Log: Log: Revision 1.1 2003/08/20
 * 13:41:00 grawund Log: Schnittstelle fuer Filter Log:
 */

public interface FilterAction {

	/**
	 * Wird in der Action festgelegt und ist nur für XML-Serialisierung gedacht
	 * 
	 * @return Name der CompensateAction
	 * @uml.property name="actionName"
	 */
	public String getActionName();

	public void setQuery(SDFQuery query);

	/**
	 * Liefert true, wenn das Objekt obj den Filter passiert (d.h. nicht
	 * gefiltert wird)
	 */
	public boolean positive(Object obj);

}