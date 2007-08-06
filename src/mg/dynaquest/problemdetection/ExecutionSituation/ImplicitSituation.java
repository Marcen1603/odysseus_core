/*
 * Created on 08.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection.ExecutionSituation;

import mg.dynaquest.monitor.POMonitorEvent;

/**
 * Superklasse aller impliziten Ausf�hrungssituationen, also aller Ausf�hrungssituationen,
 * die nur durch direkte Beobachtung der Ausf�hrung erkannt werden k�nnen.
 * 
 * @author Joerg Baeumer
 *
 */
public abstract class ImplicitSituation extends ExecutionSituation {

	/**
	 * Konstruktor
	 * 
	 * @param event situationsausl�sendes Ereignis
	 */
	public ImplicitSituation(POMonitorEvent event) {
		super(event);
	}

}
