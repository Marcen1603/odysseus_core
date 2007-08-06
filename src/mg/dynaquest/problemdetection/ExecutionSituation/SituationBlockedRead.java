package mg.dynaquest.problemdetection.ExecutionSituation;

import jcreek.representation.Case;
import jcreek.representation.LocalKnowledgeModel;
import jcreek.representation.NameAlreadyExistException;
import jcreek.representation.NoSuchRelationTypeException;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.problemdetection.Constants;
import mg.dynaquest.problemdetection.PreSituationTable;
import mg.dynaquest.problemdetection.TestEnv;
import mg.dynaquest.queryexecution.event.ReadTimeoutEvent;
import mg.dynaquest.queryexecution.po.access.PhysicalAccessPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;


/**
 * Ausführungssituation die entsteht, wenn ein Operator keine Daten aus einem
 * Eingabestrom lesen kann und daher blockiert. 
 * 
 * @author Joerg Baeumer
 *
 */
public class SituationBlockedRead extends ReadBlockedSituation {
	
	/**
	 * Zustand des Produzenten - blocked/ok
	 * @uml.property  name="producerState"
	 */
	private String producerState = "producer ok";
	/**
	 * Zustand des Eingabepuffers - empty/not empty
	 * @uml.property  name="bufferState"
	 */
	private String bufferState = "buffer not empty";
	/**
	 * Typ des Produzenten
	 * @uml.property  name="producerType"
	 */
	private String producerType = "";
		
	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public SituationBlockedRead(POMonitorEvent event) {
		super(event);
		situationType = ExecutionSituation.BLOCKEDREAD;
	}

	/** 
	 * Erstellen eines CBR-Falles aus den Attributen der Ausführungssituation.
	 * 
	 * @return generierte CBR-Fall
	 * 
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#generateCase()
	 */
	@SuppressWarnings("unused")
	protected Case generateCase() {
		
		// der neue CBR-Problemfall
		Case creekCase; 
		// Port des Eingabestroms, aus dem keine Daten mehr gelesen werden können 
		int port, i;
		ReadTimeoutEvent readTimeoutEvent = null;
		// Porduzent
		PlanOperator producerPO;
		// Wissensmodel des CBR-Systems
		LocalKnowledgeModel km = TestEnv.getKnowledgeModel();
						
		// Ermitteln der Attribute der Ausfuehrungssituation;
		// es wurde darauf verzichtet, explizit eine Ausführungssituation
		// blocked_read_access zu erstellen, denn sie unterscheidet sich 
		// von der blocked_read Ausführungssituation nur in der Anzahl ihrer
		// Attribute, daher werden hier wahlweise verschiedene Cases erstellt
		
		// ist der ausloesende Operator ein Zugriffsoperator? --> blocked_read_access
		
		if ((po instanceof PhysicalAccessPO)) {
			
			// Attributwertbelegung
			// Ermitteln der ReadTimeOutZeit über POMonitor
			
			Constants.checkAttribute("TimeOutMilliSec", timeOut = new Integer(pom.getReadTimeOutMilliSec()));
			
			// Erstellen des Falles
			
			try {
				creekCase = new Case(km , "SituationBlockedReadAccess_" + Constants.getId(), "");
	
				creekCase.setStatus("UNSOLVEDCASE");
								
				creekCase.addRelation("has operator type", km.getEntity(operatorType));
				creekCase.addRelation("has situation type", km.getEntity("blocked_read_access"));
				creekCase.addRelation("has read time out", km.getEntity("TimeOut#"+timeOut.toString()));
				
				return creekCase;
	
			} catch (NameAlreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchRelationTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		// der situationsauslösende Operator ist kein Zugriffsoperator --> blocked_read
		else {
			
			// Attributwertbelegung	
			
			readTimeoutEvent = (ReadTimeoutEvent) monitorEvent.getEvent();
			// Ermitteln des ausloesenden Produzenten 
			producerPO = po.getInputPO(readTimeoutEvent.getPort());
						
			// Ermitteln, ob Operator zum Zeitpunkt der Situationsentstehung blockiert war
			// --> PreSituationTable 
			if (PreSituationTable.operatorBlockedAtTime(producerPO, monitorEvent.getTime()) )
			
				producerState = "producer blocked";
				
			else
			
				producerState = "producer ok";	
			
			// Ermitteln des Typs des Produzenten
			
			producerType = producerPO.getPOName();
									
			// Ermitteln der ReadTimeOutZeit über POMonitor
			
			Constants.checkAttribute("TimeOutMilliSec", timeOut = new Integer(pom.getReadTimeOutMilliSec()));
					
			// Ermitteln des BufferState
			/*
			if (producerPO.bufferIsEmpty(po)) 
			
				bufferState = "buffer empty";
				
			else
			
				bufferState = "buffer not empty";	
			*/
			// *********************************	
			bufferState = "buffer empty";
			// *********************************
			
			// Erstellen des Falles 
						
			try {
				creekCase = new Case(km , "SituationBlockedRead_" + Constants.getId(), "");
				
				creekCase.setStatus("UNSOLVEDCASE");
														
				creekCase.addRelation("has operator type", km.getEntity(operatorType));
				creekCase.addRelation("has situation type", km.getEntity("blocked_read"));
				creekCase.addRelation("has producer state", km.getEntity(producerState));
				creekCase.addRelation("has in buffer state", km.getEntity(bufferState));		
				creekCase.addRelation("has read time out", km.getEntity("TimeOut#"+timeOut.toString()));
				creekCase.addRelation("has producer type", km.getEntity(producerType));
											
				return creekCase;
				
			} catch (NameAlreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchRelationTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}		
		return null;
	}

	public String toString() {
		
		return "SituationBlockRead\n\thas operator type: " + operatorType +
		       "\n\thas situation type: blocked_read\n\thas producer state: " + producerState +
		       "\n\thas in buffer state: " + bufferState +  "\n\thas read time out: " + "TimeOut#"+timeOut.toString() +
		       "\n\thas prodcer type: " + producerType + "\n";
		
	}
	
	public String getInternalName(){
		
			return "SituationBlockedRead";
		
		}

}
