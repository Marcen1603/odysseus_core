
package mg.dynaquest.problemdetection.ExecutionSituation;

import mg.dynaquest.monitor.POMonitorEvent;

/**
 * Superklasse aller Ausführungssituationen, die eine Abweichung der tatsächlichen
 * Datenrate von der erwarteten Datenrate bezüglich der Geschwindigkeit aufweisen
 * 
 * @author Joerg Baeumer
 *
 */
public abstract class RateSituation extends DatarateSituation {

	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public RateSituation(POMonitorEvent event) {
		super(event);
		// TODO Auto-generated constructor stub
	}
	
	

}
