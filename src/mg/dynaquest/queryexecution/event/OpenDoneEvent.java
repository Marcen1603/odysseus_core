package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:10 $ Version:
 * $Revision: 1.4 $ Log: $Log: OpenDoneEvent.java,v $
 * $Revision: 1.4 $ Log: Revision 1.4  2004/09/16 08:57:10  grawund
 * $Revision: 1.4 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.4 $ Log: Log: Revision 1.3
 * 2004/09/16 08:53:52 grawund Log: *** empty log message *** Log: Log: Revision
 * 1.2 2002/01/31 16:15:14 grawund Log: Versionsinformationskopfzeilen
 * eingefuegt Log:
 */

public class OpenDoneEvent extends DoneEvent {


	private static final long serialVersionUID = 6779903202017247866L;
	
	public OpenDoneEvent(TriggeredPlanOperator source) {
		super(source);
	}

	@Override
	public POEventType getPOEventType() {
		return POEventType.OpenDone;
	}
}