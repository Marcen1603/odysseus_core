package de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.bidding;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Query;

/**
 * Diese Strategy entscheidet ob sich beworben werden soll oder nicht.
 * @author christian
 *
 */
public interface IBiddingStrategy {
	public boolean bidding(Query q);
}
