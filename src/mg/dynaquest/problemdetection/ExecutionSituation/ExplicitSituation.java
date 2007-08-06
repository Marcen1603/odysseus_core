/*
 * Created on 29.11.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection.ExecutionSituation;

import jcreek.representation.Case;
import jcreek.representation.LocalKnowledgeModel;
import jcreek.representation.NameAlreadyExistException;
import jcreek.representation.NoSuchRelationTypeException;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.problemdetection.Constants;
import mg.dynaquest.problemdetection.TestEnv;
import mg.dynaquest.queryexecution.event.ExceptionEvent;

/**
 * Diese Klasse repräsentiert die expliziten Ausführungssituationen, also
 * die Situationen, die nur durch eine entsprechende Fehlermeldung eines Operators
 * (bzw. einer Datenquelle) erkannt werden. 
 * 
 * @author Joerg Baeumer
 *
 */
public class ExplicitSituation extends ExecutionSituation
 {
 	/**
	 * @uml.property  name="errorCode"
	 */
 	private String errorCode; 	
	
	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 * @param ErrorCode fehlermeldung des Operators (der Datenquelle)
	 */
	public ExplicitSituation(POMonitorEvent event, String ErrorCode) {
		super(event);
		this.situationType = ExecutionSituation.EXPLICITSITUATION;
		this.errorCode = ErrorCode;
	}
	
	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public ExplicitSituation(POMonitorEvent event){
		super(event);	
		errorCode = (((ExceptionEvent) monitorEvent.getEvent()).getMessage());
	}
		
	/**
	 * 
	 * 
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#generateCase()
	 */
	public Case generateCase() {
		
		Case creekCase;
		LocalKnowledgeModel km = null;
		
		// Generierung eines CBR-Falles 
		km = TestEnv.getKnowledgeModel();
		
		try {
			
			creekCase = new Case(km , "ExplicitSituation_" + Constants.getId(), "");
			
			creekCase.setStatus("UNSOLVEDCASE");
	
			creekCase.addRelation("has operator type", km.getEntity(operatorType));
			creekCase.addRelation("has situation type", km.getEntity("memory_overflow"));
			creekCase.addRelation("has error code", km.getEntity(errorCode));
			
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
		
		return "ExplicitSituation " + errorCode;
		
	}
	
	
}
