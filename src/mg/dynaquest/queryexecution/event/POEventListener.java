package mg.dynaquest.queryexecution.event;

import java.util.EventListener;

/**
 * Alle Objekte, die die Events empfangen möchten, die von den TriggeredPO
 * ausgesendet werden, müssen dieses Interface implementieren, damit sie die
 * Events empfangen können.
 * 
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:11 $ Version:
 * $Revision: 1.4 $ Log: $Log: POEventListener.java,v $
 * $Revision: 1.4 $ Log: Revision 1.4  2004/09/16 08:57:11  grawund
 * $Revision: 1.4 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.4 $ Log: Log: Revision 1.3
 * 2002/02/18 13:45:11 grawund Log: Version in der der Collector funktioniert
 * und keine illegalen Zustaende mehr liefert Log: Log: Revision 1.2 2002/01/31
 * 16:14:05 grawund Log: Versionsinformationskopfzeilen eingefuegt Log:
 * 
 *  
 */

public interface POEventListener extends EventListener {
	public void poEventOccured(POEvent poEvent);
}