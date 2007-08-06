package mg.dynaquest.queryexecution.event;

import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:10 $ Version:
 * $Revision: 1.3 $ Log: $Log: InitEvent.java,v $
 * $Revision: 1.3 $ Log: Revision 1.3  2004/09/16 08:57:10  grawund
 * $Revision: 1.3 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.3 $ Log: Log: Revision 1.2 2002/01/31
 * 16:15:01 grawund Log: Versionsinformationskopfzeilen eingefuegt Log:
 */

public abstract class InitEvent extends POEvent {

	public InitEvent(TriggeredPlanOperator source) {
		super(source);
	}

}