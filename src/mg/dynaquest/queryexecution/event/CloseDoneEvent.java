package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:10 $ Version:
 * $Revision: 1.4 $ Log: $Log: CloseDoneEvent.java,v $
 * $Revision: 1.4 $ Log: Revision 1.4  2004/09/16 08:57:10  grawund
 * $Revision: 1.4 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.4 $ Log: Log: Revision 1.3
 * 2004/09/16 08:53:52 grawund Log: *** empty log message *** Log: Log: Revision
 * 1.2 2002/01/31 16:15:18 grawund Log: Versionsinformationskopfzeilen
 * eingefuegt Log:
 */

public class CloseDoneEvent extends DoneEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8212480909058886543L;
	
	
	public CloseDoneEvent(TriggeredPlanOperator source) {
		super(source);
	}


	@Override
	public POEventType getPOEventType() {
		return POEventType.CloseDone;
	}
}