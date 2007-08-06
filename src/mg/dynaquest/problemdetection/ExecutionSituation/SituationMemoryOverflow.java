package mg.dynaquest.problemdetection.ExecutionSituation;

import jcreek.representation.Case;
import jcreek.representation.LocalKnowledgeModel;
import jcreek.representation.NameAlreadyExistException;
import jcreek.representation.NoSuchRelationTypeException;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.problemdetection.Constants;
import mg.dynaquest.problemdetection.TestEnv;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;

/**
 * Ausführungssituation die entsteht, wenn einem Operator zu wenig Spiecher zur 
 * Verarbeitung zur verfügung steht
 * 
 * @author Joerg Baeumer
 */

public class SituationMemoryOverflow extends ImplicitSituation{

	/**
	 * aktuelle Größe des verfügbareb Speichers
	 * @uml.property  name="operatorMemSize"
	 */
	private Integer operatorMemSize;
	
	/**
	 * Konstruktor
	 * 
	 * @param event situationsuslösender Operator
	 */
	public SituationMemoryOverflow(POMonitorEvent event) {
		super(event);
		this.situationType = ExecutionSituation.MEMORYOVERFLOW;
	}
		
	/** 
	 * Erstellen eines CBR-Falles aus den Attributen der Ausführungssituation.
	 * 
	 * @return generierte CBR-Fall
	 * 
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#generateCase()
	 */
	public Case generateCase() {
		
		Case creekCase;
		LocalKnowledgeModel km = TestEnv.getKnowledgeModel();
		
		// Ermitteln der Attribute der Ausführungssituation 
		operatorMemSize = new Integer(((NAryPlanOperator) po).getMaxMem());
		
		// Erstellen des Falles 
	
		try {
			 creekCase = new Case(km , "SituationMemoryOverFlow_" + Constants.getId(), "");
		
			 creekCase.setStatus("UNSOLVEDCASE");
								
			 creekCase.addRelation("has operator type", km.getEntity(operatorType));
			 creekCase.addRelation("has situation type", km.getEntity("memory_overflow"));
			 creekCase.addRelation("has operator mem size", km.getEntity("OpMemSize#"+operatorMemSize.toString()));
			 									
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

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#getInternalName()
	 */
	public String getInternalName() {
		
		return "SituationMemoryOverflow";
	}
	
	

}
