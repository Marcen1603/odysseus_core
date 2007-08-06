package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:11 $ Version:
 * $Revision: 1.4 $ Log: $Log: DoneEvent.java,v $
 * $Revision: 1.4 $ Log: Revision 1.4  2004/09/16 08:57:11  grawund
 * $Revision: 1.4 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.4 $ Log: Log: Revision 1.3 2004/09/16
 * 08:53:52 grawund Log: *** empty log message *** Log: Log: Revision 1.2
 * 2002/01/31 16:15:04 grawund Log: Versionsinformationskopfzeilen eingefuegt
 * Log:
 */

public abstract class DoneEvent extends POEvent {

	//private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4044053818312622092L;

	public DoneEvent(TriggeredPlanOperator source) {
		super(source);
	}

}