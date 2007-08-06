/*
 * Created on 05.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection.ExecutionSituation;

import mg.dynaquest.monitor.POMonitorEvent;

/**
 * Superklasse aller Ausführungssituationen, die eine Abweichung der tatsächlichen
 * Datenrate von der erwarteten Datenrate bezüglich der Geschwindigkeit oder 
 * der Charakteristik (etwa bursty) aufweisen
 * 
 * @author Joerg Baeumer
 *
 */
public abstract class DatarateSituation extends ImplicitSituation {

	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public DatarateSituation(POMonitorEvent event) {
		super(event);
		
	}

	

}
