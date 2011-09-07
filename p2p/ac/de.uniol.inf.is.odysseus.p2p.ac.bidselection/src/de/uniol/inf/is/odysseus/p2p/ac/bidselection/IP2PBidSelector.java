package de.uniol.inf.is.odysseus.p2p.ac.bidselection;

import java.util.List;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;

/**
 * Schnittstelle für Implementierungen, die aus einer Liste von Geboten das
 * "beste" Gebot wählen.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IP2PBidSelector {

	/**
	 * Liefert aus einer gegebenen Liste von Geboten das "beste" Gebot aus und
	 * liefert es zurück.
	 * 
	 * @param bids
	 *            Liste aller Gebote
	 * @return "Bestes" Gebot.
	 */
	public Bid selectBid(List<Bid> bids);

}
