package de.uniol.inf.is.odysseus.core.sla;

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
	public double getCost();
	
}
