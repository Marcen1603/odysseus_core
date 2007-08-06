/*
 * Created on 08.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection.ExecutionSituation;

import mg.dynaquest.monitor.POMonitorEvent;

/**
 * Superklasse aller Ausführungssituationen, die sich dadurch auszeichnen, dass
 * ein Operator blockiert, da er keine Daten mehr lesen kann
 * 
 * @author Joerg Baeumer
 *
 */
public abstract class ReadBlockedSituation extends BlockedSituation {

	/**
	 * Konstruktor
	 * 
	 * @param situationsauslösendes Ereignis
	 */
	public ReadBlockedSituation(POMonitorEvent event) {
		super(event);
	}

}
