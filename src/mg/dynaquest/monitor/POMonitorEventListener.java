package mg.dynaquest.monitor;

/** POMonitorEventListener:
 * Schnittstelle für das Empfangen von POMonitorEvents
 * $Log: POMonitorEventListener.java,v $
 * Revision 1.2  2004/09/16 08:57:11  grawund
 * Quellcode durch Eclipse formatiert
 *
 * Revision 1.1  2003/06/10 05:53:32  hobelmann
 * *** empty log message ***
 *
 */

import java.util.EventListener;

public interface POMonitorEventListener extends EventListener {
	/**
	 * wird bei Auftreten einer Event aufgerufen
	 * 
	 * @param ev
	 *            die POMonitorEvent
	 */
	public void poMonitorEventOccured(POMonitorEvent ev);
}