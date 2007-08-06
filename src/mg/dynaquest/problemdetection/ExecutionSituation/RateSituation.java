
package mg.dynaquest.problemdetection.ExecutionSituation;

import mg.dynaquest.monitor.POMonitorEvent;

/**
 * Superklasse aller Ausf�hrungssituationen, die eine Abweichung der tats�chlichen
 * Datenrate von der erwarteten Datenrate bez�glich der Geschwindigkeit aufweisen
 * 
 * @author Joerg Baeumer
 *
 */
public abstract class RateSituation extends DatarateSituation {

	/**
	 * Konstruktor
	 * 
	 * @param event situationsausl�sendes Ereignis
	 */
	public RateSituation(POMonitorEvent event) {
		super(event);
		// TODO Auto-generated constructor stub
	}
	
	

}
