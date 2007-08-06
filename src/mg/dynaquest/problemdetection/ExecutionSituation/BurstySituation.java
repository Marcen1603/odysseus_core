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
 * Datenrate von der erwarteten Datenrate bezüglich der Charakteristik des c
 * Datenstroms aufweisen 
 * 
 * @author Joerg Baeumer
 *
 */
public abstract class BurstySituation extends DatarateSituation {

	/**
	 * @uml.property  name="blockSize"
	 */
	protected int BlockSize = 0;

	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public BurstySituation(POMonitorEvent event) {
		super(event);
	}

}
