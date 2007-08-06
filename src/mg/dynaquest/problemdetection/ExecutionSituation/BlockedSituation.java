/*
 * Created on 08.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mg.dynaquest.problemdetection.ExecutionSituation;

import mg.dynaquest.monitor.POMonitorEvent;

/**
 * Superklasse aller Ausführungssituationen, die eine Blockierung des 
 * situationsauslösenden Operators verursacht. 
 * 
 * @author Joerg Baeumer
 *
 */
public abstract class BlockedSituation extends ImplicitSituation {

	/**
	 * TimeOut-Zeit des Operators
	 * @uml.property  name="timeOut"
	 */
	protected Integer timeOut;

	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Eeignis
	 */
	public BlockedSituation(POMonitorEvent event) {
		super(event);
		
		timeOut = new Integer(0);
		
		
	}

	

}
