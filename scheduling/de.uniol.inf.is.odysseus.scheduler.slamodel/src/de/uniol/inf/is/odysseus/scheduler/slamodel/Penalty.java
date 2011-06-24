package de.uniol.inf.is.odysseus.scheduler.slamodel;

/**
 * Interface defining penalties for sla violations
 * 
 * @author Thomas Vogelgesang
 *
 */
public interface Penalty {
	
	/**
	 * @return the amount that must be payed if a penalty is payable
	 */
	public int getCost();
	
}
