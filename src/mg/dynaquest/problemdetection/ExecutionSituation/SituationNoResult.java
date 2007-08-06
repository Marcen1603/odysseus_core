/*
 * Created on 08.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection.ExecutionSituation;

import mg.dynaquest.monitor.POMonitorEvent;
import jcreek.representation.Case;

/**
 * Ausführungssituation, die entsteht, wenn die Anfrage kein ergebnis liefert,
 * wenn also der TOP-Operator keine Daten liefert
 *
 * @author Ich
**/
public class SituationNoResult extends ImplicitSituation{

	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public SituationNoResult(POMonitorEvent event) {
		super(event);
	}
		
	/** 
	 * Erstellen eines CBR-Falles aus den Attributen der Ausführungssituation.
	 * 
	 * @return generierte CBR-Fall
	 * 
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#generateCase()
	 */
	public Case generateCase() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#getInternalName()
	 */
	public String getInternalName() {
		
		return "SituationNoResult";
	}

}
