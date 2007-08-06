/*
 * Created on 29.11.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection.ExecutionSituation;

import jcreek.reasoning.RetrieveResult;
import jcreek.reasoning.ReuseResult;
import jcreek.representation.Case;
import mg.dynaquest.monitor.POMonitor;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.problemdetection.Constants;
import mg.dynaquest.problemdetection.solutionobjects.SolutionObject;
import mg.dynaquest.queryexecution.po.base.PlanOperator;

/**
 * Superklasse allser Ausführungssituationen.
 * @author   Joerg Baeumer
 */

public abstract class ExecutionSituation {


	public final static Integer BLOCKEDREAD = new Integer(1);
	public final static Integer BLOCKEDREADACCESS = new Integer(2);
	public final static Integer BLOCKEDWRITE = new Integer(3);
	public final static Integer MEMORYOVERFLOW = new Integer(4);
	public final static Integer NORESULT = new Integer(5);
	public final static Integer SLOWDATARATE = new Integer(6);
	public final static Integer DEFERREDDATA = new Integer(7);
	public final static Integer BURSTYDELIVERY = new Integer(8);
	public final static Integer BURSTYUNEXPECTEDBLOCKSIZE = new Integer(9);	
	public final static Integer EXPLICITSITUATION = new Integer(10);


	/**
	 * Referenz auf den situationsauslösenden Planoperator
	 * @uml.property  name="po"
	 * @uml.associationEnd  
	 */
	protected PlanOperator po = null;
	/**
	 * Beschreibt den typ des Operators (internalName)
	 * @uml.property  name="operatorType"
	 */		
	protected String operatorType;
	/**
	 * Beschreibt den Typ der Ausführungssituation
	 * @uml.property  name="situationType"
	 */
	protected Integer situationType;
	/**
	 * Zeitpunkt der Entstehung des zur Situation gehörenden Events
	 * @uml.property  name="time"
	 */
	protected long time;
	/**
	 * Das die Sitaution auslösende Ereigniss des Monitors
	 * @uml.property  name="monitorEvent"
	 * @uml.associationEnd  
	 */	
	protected POMonitorEvent monitorEvent = null;
	/**
	 * Der zum Event gehörende POMonitor
	 * @uml.property  name="pom"
	 * @uml.associationEnd  
	 */
	protected POMonitor pom = null;
	
	/**
	 * @uml.property  name="reuseResultObject"
	 * @uml.associationEnd  
	 */
	protected ReuseResult reuseResultObject = null;
			
	/**
	 * Konstruktor
	 * 
	 * @param event das auslösende Ereignis eines POMonitors
	 */
	public ExecutionSituation(POMonitorEvent event) {
		
		super();
		this.monitorEvent = event;
				
		pom = (POMonitor) monitorEvent.getSource();
		po = (PlanOperator) pom.getPlanOperator();
						
		operatorType = po.getPOName();
		time = event.getTime();
		
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Liefert den situationsauslösenden Planoperatoren
	 * 
	 * @return situationsauslösender Planoperatoren
	 */
	public PlanOperator getPlanOperator() {		
		return po;
		
	}
	
	/**
	 * Liefert das situationsauslösende POMonitorEvent
	 * 
	 * @return situationsauslösendes POMonitorEvent
	 */
	public POMonitorEvent getEvent() {
		
		return monitorEvent;
		
	}
	
	/**
	 * Factory-Methode, die aus einem Event und der Beschreibung der zu erstellenden
	 * Ausführungssituation (über SituationsId) eine neue Ausführungssituation
	 * generiert
	 * 
	 * @param evt das situationsauslösende Ereignis
	 * @param situationID die AusführungssituationsID
	 * @return
	 */
	public static ExecutionSituation createSituation(POMonitorEvent evt, Integer situationID) {
		
		if (situationID.compareTo(BLOCKEDREAD) == 0)
		
			return new SituationBlockedRead(evt);
			
		/*if (situationID.compareTo(BLOCKEDREADACCESS) == 0)
		
			return new SituationBlockedReadAccess(evt);
		*/					
		if (situationID.compareTo(BLOCKEDWRITE) == 0)
		
			return new SituationBlockedWrite(evt);
			
		if (situationID.compareTo(MEMORYOVERFLOW) == 0)
		
			return new SituationMemoryOverflow(evt);
				
		if (situationID.compareTo(NORESULT) == 0)
		
			return new SituationNoResult(evt);
				
		if (situationID.compareTo(SLOWDATARATE) == 0)
		
			return new SituationSlowDatarate(evt);
				
		if (situationID.compareTo(BURSTYDELIVERY) == 0)
		
			return new SituationBurstyDelivery(evt);
							
		if (situationID.compareTo(BURSTYUNEXPECTEDBLOCKSIZE) == 0)
		
			return new SituationBurstyUnexpectedBlockSize(evt);
		
		if (situationID.compareTo(EXPLICITSITUATION) == 0)
		
			return new SituationBlockedRead(evt);
					
							
		return null;	
			
	}
	
	/**
	 * Liefert ein Creek-ReuseResult für die Ausführungssituation. Dieses Objekt enthält eine nach Ähnlichkeit zur Ausführungssituation (bzw. dem Problemfall)  sortierte Anzahl von gelösten Fällen (Cases) des Creek-Systems
	 * @return  Objekt mit den retrievten Fällen
	 * @uml.property  name="reuseResultObject"
	 */		
	protected ReuseResult getReuseResultObject(){
		
		Case creekCase;
		
		if (reuseResultObject == null){
			
			// Generierung des neuen Problemfalls						
			creekCase = this.generateCase();
			
			// Retrieval
			reuseResultObject = new ReuseResult(new RetrieveResult(creekCase), Constants.numberOfRetrieveCases);
					
										
		}
						
		return reuseResultObject;
		
	}
	
	/**
	 * Liefert das Lösungsobjekt des zur Ausführungssituation (bzw. des zum
	 * neuen Problemfalls) ähnlichsten Falles
	 * 
	 * @return Lösung des ähnlichsten Falles
	 */
	public SolutionObject getBestSolution() {
						
		SolutionObject solution = (SolutionObject) getReuseResultObject().getSolution(0).getEntityObject(); 
		
		if (Constants.debug)
			System.out.println("getBestSolution(): " + this);
		
		return solution;	
		
	}
	
	/**
	 * Liefert eine zur besten Lösung alternative Lösung. 
	 * 
	 * @param i i-beste Lösung
	 * @return Lösungsobjekt der i-besten Lösung
	 */
	public SolutionObject getAlternativeSolution(int i) {
				
		if ( i > getReuseResultObject().getK() )
			i = getReuseResultObject().getK();
		
		SolutionObject solution = (SolutionObject) getReuseResultObject().getSolution(i-1).getEntityObject(); 

		return solution;	
		
	}
	
	/**
	 * Erstellen eines CBR-Falles aus den Attributen der Ausführungssituation.
	 * 
	 * @return CBR-Fall
	 */
	protected abstract Case generateCase();
	
	/**
	 * @uml.property  name="internalName"
	 */
	public abstract String getInternalName();
	

}
