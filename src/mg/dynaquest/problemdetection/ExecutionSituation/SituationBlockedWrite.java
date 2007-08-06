package mg.dynaquest.problemdetection.ExecutionSituation;

import jcreek.representation.Case;
import jcreek.representation.LocalKnowledgeModel;
import jcreek.representation.NameAlreadyExistException;
import jcreek.representation.NoSuchRelationTypeException;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.problemdetection.Constants;
import mg.dynaquest.problemdetection.PreSituationTable;
import mg.dynaquest.problemdetection.TestEnv;
import mg.dynaquest.queryexecution.event.WriteTimeoutEvent;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.base.PlanOperator;

/**
 * Ausführungssituation die entsteht, wenn ein Operator keine Daten in einen
 * Ausgabestrom schreiben kann und daher blockiert. 
 * 
 * @author Joerg Baeumer
 *
 */
public class SituationBlockedWrite extends BlockedSituation {

	/**
	 * Zustand des Konsumenten - ok/blocked
	 * @uml.property  name="consumerState"
	 */
	private String consumerState = "consumer ok";
	/**
	 * Zustand des Ausgabepuffers - filled/not filled
	 * @uml.property  name="bufferState"
	 */
	private String bufferState = "buffer not filled";
	/**
	 * aktuelle Größe des Austauschpuffers
	 * @uml.property  name="bufferSize"
	 */	
	private Integer bufferSize;
	
	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis 
	 */
	public SituationBlockedWrite(POMonitorEvent event) {

		super(event);
		this.situationType = ExecutionSituation.BLOCKEDWRITE;
		
	}
	
	/** 
	 * Erstellen eines CBR-Falles aus den Attributen der Ausführungssituation.
	 * 
	 * @return generierte CBR-Fall
	 *  
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#generateCase()
	 */
	public Case generateCase() {
		
		// der neue CBR-Problemfall
		Case creekCase;
		WriteTimeoutEvent writeTimeoutEvent = null;
		// Konsument
		PlanOperator consumerPO;
		// Wissensmodel des CBR-Systems
		LocalKnowledgeModel km = TestEnv.getKnowledgeModel();;
		
		// Attributwertbelegung
		
		// Ermitteln des Konsumenten
		writeTimeoutEvent = (WriteTimeoutEvent) monitorEvent.getEvent();
		//*******************************
		//consumerPO = (NAryPlanOperator) po.getRegisteredConsumers().get(writeTimeoutEvent.getPort());
		consumerPO = writeTimeoutEvent.getPort();
		//*******************************
		// Ermitteln, ob der Konsument zum Zeitpunkt der Situationsentstehung blockiert war
		if (PreSituationTable.operatorBlockedAtTime(consumerPO, monitorEvent.getTime()) )
		
			consumerState = "consumer blocked";
			
		else
		
			consumerState = "consumer ok";	
				
		// Ermitteln der WriteTimeOutZeit über POMonitor
		timeOut = Constants.checkAttribute("TimeOutMilliSec", new Integer(pom.getWriteTimeOutMilliSec()));
										
		// Ermitteln des BufferState
		
		// TODO bufferState ermitteln
	/*	if (po.bufferIsFilled(consumerPO)) 
		
			bufferState = "buffer not empty";
			
		else
		
			bufferState = "buffer empty";	
		*/
				
		bufferState = "buffer filled";
		
		// Ermitteln der Buffer-Groesse
		bufferSize = Constants.checkAttribute("BufferSize", new Integer(((NAryPlanOperator) po).getMaxElementsBufferSize()));
						
		// Erstellen des Falles-Falles 
				
		try {
			creekCase = new Case(km , "SituationBlockedWrite_" + Constants.getId(), "");
	
			creekCase.setStatus("UNSOLVEDCASE");
	
			creekCase.addRelation("has operator type", km.getEntity(operatorType));
			creekCase.addRelation("has situation type", km.getEntity("blocked_write"));
			creekCase.addRelation("has consumer state", km.getEntity(consumerState));
			creekCase.addRelation("has out buffer state", km.getEntity(bufferState));		
			creekCase.addRelation("has write timeout", km.getEntity("TimeOut#"+timeOut.toString()));
			creekCase.addRelation("has buffer size", km.getEntity("BufferSize#"+bufferSize));
								
			return creekCase;
	
		} catch (NameAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchRelationTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
		
		
		
	}
	
	public String toString() {
		
		return "SituationBlockedWrite\n\thas operator type: " + operatorType +
		       "\n\thas situation type: blocked_write\n\thas consumer state: " + consumerState +
		       "\n\thas out buffer state: " + bufferState +  "\n\thas write time out: " + "TimeOut#"+timeOut.toString() +
		       "\n\thas buffer size : " + bufferSize + "\n";
		        
		
	}
	
	public String getInternalName(){
		
		return "SituationBlockedWrite";
	
	}

}
