package mg.dynaquest.problemdetection.ExecutionSituation;

import jcreek.representation.Case;
import jcreek.representation.LocalKnowledgeModel;
import jcreek.representation.NameAlreadyExistException;
import jcreek.representation.NoSuchRelationTypeException;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.problemdetection.Constants;
import mg.dynaquest.problemdetection.TestEnv;
import mg.dynaquest.queryexecution.event.BurstyBlockSizeUnexpected;

/**
 * Ausführungssituation die entsteht, wenn ein Operator wie erwartet die Daten blockweise einliest, aber die Blockgröße von der erwarteten Blockgröße abweicht.
 * @author  Joerg Baeumer
 */
public class SituationBurstyUnexpectedBlockSize extends BurstySituation {

	/**
	 * @uml.property  name="blockSize"
	 */
	private int blockSize = 0;
	/**
	 * @uml.property  name="event"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private POMonitorEvent event;
	
	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public SituationBurstyUnexpectedBlockSize(POMonitorEvent event) {
		super(event);
		this.situationType = ExecutionSituation.BURSTYUNEXPECTEDBLOCKSIZE;
		this.event = event;
	}
	
	/**
	 * Erstellen eines CBR-Falles aus den Attributen der Ausführungssituation.
	 * 
	 * @return generierte CBR-Fall
	 *  
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#generateCase()
	 */
	public Case generateCase() {
				
		//der neue CBR-Problemfall
		Case creekCase;
		// Wissensmodel des CBR-Systems
		LocalKnowledgeModel km = TestEnv.getKnowledgeModel();;

		// TODO blockSize ermitteln
		blockSize = ((BurstyBlockSizeUnexpected) event.getEvent()).getBlocksize(); 

		// Erstellen des Falles-Falles 
		
		 try {
			 creekCase = new Case(km , "SituationBurstyUnexpectedBlockSize_" + Constants.getId(), "");

			 creekCase.setStatus("UNSOLVEDCASE");

			 creekCase.addRelation("has operator type", km.getEntity(operatorType));
			 creekCase.addRelation("has situation type", km.getEntity("bursty_unexpected_block_size"));
			 creekCase.addRelation("has block size", km.getEntity("BlockSize#"+blockSize));
	 
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
	
	public String getInternalName(){
		
			return "SituationBurstyUnexpectedBlockSize";
		
		}

}
