package mg.dynaquest.problemdetection.ExecutionSituation;

import jcreek.representation.Case;
import jcreek.representation.LocalKnowledgeModel;
import jcreek.representation.NameAlreadyExistException;
import jcreek.representation.NoSuchRelationTypeException;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.problemdetection.Constants;
import mg.dynaquest.problemdetection.TestEnv;

/**
 * Ausführungssitutaion die entsteht, wenn die Daten einem Operators
 * nicht wie erwartet konstant, sondern blockweise geliefert werden.
 *  
 * @author Joerg Baeumer
 *
 */
public class SituationBurstyDelivery extends BurstySituation {

	/**
	 * @uml.property  name="blockSize"
	 */
	private int blockSize = 0;

	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public SituationBurstyDelivery(POMonitorEvent event) {
		super(event);
		this.situationType = ExecutionSituation.BURSTYDELIVERY;
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
		//blockSize = 
		
		// Erstellen des Falles-Falles 
				
		 try {
			 creekCase = new Case(km , "SituationBurstyDelivery_" + Constants.getId(), "");

			 creekCase.setStatus("UNSOLVEDCASE");

			 creekCase.addRelation("has operator type", km.getEntity(operatorType));
			 creekCase.addRelation("has situation type", km.getEntity("bursty_delivery"));
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
		
			return "SituationBursryDelivery";
		
		}

}
