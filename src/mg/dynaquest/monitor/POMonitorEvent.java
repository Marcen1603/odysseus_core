package mg.dynaquest.monitor;

/**
 * POMonitorEvent: Ereignis von einem POMonitor wird an einen
 * POMonitorEventListener weitergegeben $Log: POMonitorEvent.java,v $
 * POMonitorEventListener weitergegeben Revision 1.6  2004/09/16 08:57:11  grawund
 * POMonitorEventListener weitergegeben Quellcode durch Eclipse formatiert
 * POMonitorEventListener weitergegeben Revision
 * 1.5 2004/09/16 08:53:53 grawund *** empty log message ***
 * 
 * Revision 1.4 2004/07/28 13:12:22 grawund Speichert jetzt das vollständige
 * Event der POs und nicht nur die ID
 * 
 * Revision 1.3 2004/06/11 10:20:29 grawund - Komplettes umschreiben - Events
 * haben keine Dauer mehr, sondern nur Zeitpunkte
 * 
 * Revision 1.2 2003/07/25 09:55:30 hobelmann Erweitert um ExceptionEvent
 * (ERROR)
 * 
 * Revision 1.1 2003/06/10 05:53:26 hobelmann *** empty log message ***
 *  
 */

import java.util.EventObject;
import mg.dynaquest.queryexecution.event.POEvent;

//import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class POMonitorEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6596951090207995322L;

	// private static final long serialVersionUID = 1L;
	/* Event data */
	/**
	 * @uml.property  name="event"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private POEvent event;

	/**
	 * @uml.property  name="time"
	 */
	private long time;

	/**
	 * @param src
	 *            QuellPO des Ereignisses
	 * @param evt
	 *            Typ der Event
	 * @param time
	 *            Zeitpunkt des Events
	 */
	public POMonitorEvent(POMonitor src, POEvent evt, long time) {
		super(src);
		event = evt;
		this.time = time;
	}

	/**
	 * @return  the event
	 * @uml.property  name="event"
	 */
	public POEvent getEvent() {
		return event;
	}

	/**
	 * @returns  die Eintrittszeit des Events
	 * @uml.property  name="time"
	 */
	public long getTime() {
		return time;
	}

}