package mg.dynaquest.queryexecution.event;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:10 $ 
 Version: $Revision: 1.3 $
 Log: $Log: POEvent.java,v $
 Log: Revision 1.3  2004/09/16 08:57:10  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.2  2002/01/31 16:14:02  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.EventObject;
import mg.dynaquest.queryexecution.object.POElementBuffer;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author   Marco Grawunder
 */
public abstract class POEvent extends EventObject {

	/**
	 * @uml.property  name="sourceGUID"
	 */
	private String sourceGUID = "";

	public POEvent(TriggeredPlanOperator source) {
		super(source);
		sourceGUID = source.PO_ID;
	}
	
	/**
	 * @return  the sourceGUID
	 * @uml.property  name="sourceGUID"
	 */
	public String getSourceGUID() {
		return sourceGUID;
	}

	protected POEvent(POElementBuffer buffer){
		super(buffer);
	}

	public String toString() {
		return this.getClass().getName();
	}

	/**
	 * @uml.property  name="pOEventType"
	 * @uml.associationEnd  readOnly="true"
	 */
	public abstract POEventType getPOEventType();
	
}