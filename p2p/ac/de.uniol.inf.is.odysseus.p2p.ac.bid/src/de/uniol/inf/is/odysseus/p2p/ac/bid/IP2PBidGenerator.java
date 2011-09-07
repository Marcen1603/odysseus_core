package de.uniol.inf.is.odysseus.p2p.ac.bid;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.costmodel.ICost;

/**
 * Schnittstelle für Algorithmen, die Gebote für Teilpläne aus aktuellen Kosten,
 * Maximalkosten und potenziellen neuen Kosten des Teilplans generieren. Diese
 * Schnittstelle wird hauptsächlich im P2P-Kontext in Operator-Peers eingesetzt.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IP2PBidGenerator {

	/**
	 * Erstellt auf Grundlage der gegebenen aktuellen Systemlast, den
	 * potenziellen Kosten eines Teilplans sowie den Maximalkosten ein Gebot für
	 * den Teilplan. Diese werden in Operator-Peers verwendet.
	 * 
	 * @param sender
	 *            Admission Control, welcher die Kostenschätzungen durchgeführt
	 *            hat.
	 * @param actSystemLoad
	 *            Aktuelle Systemlast
	 * @param queryCost
	 *            Potenzielle Kosten des Teilplans
	 * @param maxCost
	 *            Maximalkosten
	 * @return Gebot
	 */
	public double generateBid(IAdmissionControl sender, ICost actSystemLoad, ICost queryCost, ICost maxCost);
}
