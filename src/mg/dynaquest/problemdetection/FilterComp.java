/*
 * Created on 15.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection;

import java.util.HashMap;
import java.util.Vector;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.monitor.POMonitorEventListener;
import mg.dynaquest.queryexecution.event.POEventType;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.pom.POManagerEvent;
import mg.dynaquest.queryexecution.pom.POManagerEventListener;

/**
 * Diese Klasse nimmt die Ereignisse der POMonitore entgegen und leitet nur solche einer weiteren zu, die Problemsitutationen kennzeichnen. Treten Ereignisse auf, die eine Blockierung eines Operators signalisieren, so wird der  PreSituationTable der Operator hinzugefügt (bzw. der Entry-Zeitpunkt). Entsprechendes geschieht bei Auftreten von Ereignissen, die eine Aufhebung der Blockierung signalisieren (hinzufügen des Leave-Zeitpunkts). 
 * @author  Joerg Baeumer
 */
public class FilterComp implements POMonitorEventListener, POManagerEventListener{
				
	private static Vector<POEventType> filterEvents;
	private static HashMap<POEventType,Integer> blockedEvents;
	/**
	 * @uml.property  name="execTable"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ExecutionSituationTable execTable;	
	/**
	 * @uml.property  name="eventBuffer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private EventBuffer eventBuffer;
	
	/**
	 * Modus für FilterComp.addBlockedEventId(java.lang.Integer, java.lang.Integer)
	 */	
	public static final Integer BLOCK = new Integer(1);  
	/**
	 * Modus für FilterComp.addBlockedEventId(java.lang.Integer, java.lang.Integer)
	 */	
	public static final Integer UNBLOCK = new Integer (2);
	
	private static FilterComp filterComp;
	
	private FilterComp() {
		
		blockedEvents = new HashMap<POEventType,Integer>();
		filterEvents = new Vector<POEventType>();
		execTable = ExecutionSituationTable.getInstance();	
		eventBuffer = EventBuffer.getInstance();			
	}

	/**
	 *  Hinzufügen von Events, die einer weitergehenden Verarbeitung unterzogen 
	 *	werden sollen (also 'situationsauslösende' Events)
	 * 
	 * @param eventId Id des Ereignisses 
	 */
	public void addFilterEventId(POEventType eventId){
			
		filterEvents.addElement(eventId);	
	
	}
	
	/**
	 * Hinzufügen von Events, die eine Blockierung des Operator
	 * signalisieren; also aller Events, deren situationsauslösenden 
	 * Operatoren in die 'PreSituationTable' geschrieben werden sollen
	 * oder deren Operatoren aus ihr entfernt werden sollen (Modus BLOCK oder UNBLOCK).
	 * 	
	 * @param eventId Id des Ereignisses
	 * @param modus	Modus: FilterComp.BLOCK bzw. FilterComp.UNBLOCK 	
	 */
	public void addBlockedEventId(POEventType eventId, Integer modus){
		
		blockedEvents.put(eventId, modus);	

	}
	
	public static boolean isBlockedEvent(POMonitorEvent event){
		
		return blockedEvents.containsKey(event.getEvent().getPOEventType());
		
		
		
	}
	
	/**
	 * Liefert eine Instanz (Singleton)
	 * 
	 * @return Instanz der FilterComp-Klasse
	 */
	public static FilterComp getInstance(){
		
		if (filterComp == null){
			filterComp = new FilterComp();
		}
		return filterComp ;
	}

	/**
	 * Implementierung des poMonitorEventListeners
	 * 
	 * @see mg.dynaquest.monitor.POMonitorEventListener#poMonitorEventOccured(mg.dynaquest.monitor.POMonitorEvent)
	 */
	public void poMonitorEventOccured(POMonitorEvent ev) {
				
		if (blockedEvents.containsKey(ev.getEvent().getPOEventType())){
		   	   		   	   		
			//Wenn ein blockierendes Event, dann Einfuegen des Operators
			// in 'PreSituationTable'
			if (((Integer) blockedEvents.get(ev.getEvent().getPOEventType())).compareTo(BLOCK) == 0) {
	   						
				PreSituationTable.setOperatorBlocked((PlanOperator) ev.getEvent().getSource(), ev.getTime());
					
			}
	
			// Wenn ein die Blockierung aufhebendes Event, dann 'Loeschen' des Operators/Situation
			// aus 'ExecutionSituationTable' und 'PreSituationTable'
			if (((Integer) blockedEvents.get(ev.getEvent().getPOEventType())).compareTo(UNBLOCK) == 0) {
	   				
				//Operator durch einfuegen eines 'leave' Zeitpunktes als nicht mehr blockiert markieren
				PreSituationTable.setOperatorUnblocked((SupportsCloneMe) ev.getEvent().getSource(), ev.getTime());
			
				execTable.deleteOperator((SupportsCloneMe) ev.getEvent().getSource());
			
			}
		}
		
		// Filtern und Einfuegen in EventBuffer
		if (filterEvents.contains(ev.getEvent().getPOEventType())) {
		
			eventBuffer.put(ev);
		
		}
		
	}

	/**
	 * Implementierung des POManagerEventListeners
	 *  
	 * @see mg.dynaquest.queryexecution.pom.POManagerEventListener#poManagerEventOccured(mg.dynaquest.queryexecution.pom.POManagerEvent)
	 */
	public void poManagerEventOccured(POManagerEvent poEvent) {
	
	// Hier muss nch realisiert, was geschieht, wenn...
	
	// ... PO eingesetzt wird
	// regristieren als POMonitorEventListener
	
	// ... PO entfernt wird
	// löschen der zugehoerigen Eintraege aus PreSituationTable und ExecutionSituationTable 
	// abmelden als POMonitorEventListener
		
	}
	
	
		
	
	
	
		
	

}
