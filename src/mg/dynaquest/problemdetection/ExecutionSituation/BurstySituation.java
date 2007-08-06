/*
 * Created on 05.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection.ExecutionSituation;

import mg.dynaquest.monitor.POMonitorEvent;

/**
 * Superklasse aller Ausf�hrungssituationen, die eine Abweichung der tats�chlichen
 * Datenrate von der erwarteten Datenrate bez�glich der Charakteristik des c
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
	 * @param event situationsausl�sendes Ereignis
	 */
	public BurstySituation(POMonitorEvent event) {
		super(event);
	}

}
