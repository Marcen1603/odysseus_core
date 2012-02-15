package de.uniol.inf.is.odysseus.p2p.ac.bid.standard;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.p2p.ac.bid.IP2PBidGenerator;

/**
 * Standardimplementierung der Gebotsgenerieren. Das Gebot wird aus der
 * Kombination aus Prozessorkosten und Speicherkosten gebildet. Dabei werden die
 * potenziellen Kosten des neuen Teilplans mit einbezogen. Je höher also das
 * Gebot, desto schlechter. Dementsprechend muss die Implementierung von
 * IP2PBidSelector aufgebaut sein. Dieser Generator kann nur mit dem
 * Kostenmodell nach Operatoreigenschaften eingesetzt werden. Absonsten wird als
 * Gebot immer 1 zurückgegeben.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardP2PBidGenerator implements IP2PBidGenerator {

	/**
	 * Liefert ein Gebot zu einem Teilplan. Dabei werden die aktuellen Kosten
	 * des Ausführungsplans mit den neuen Kosten des Teilplans addiert.
	 * Anschließend werden die Komponenten (Speicherkosten und Prozessorkosten)
	 * addiert. Das Ergebnis ist das Gebot.
	 */
	@SuppressWarnings("restriction")
	public double generateBid(IAdmissionControl sender, ICost actSystemLoad, ICost queryCost, ICost maxCost) {

		if (actSystemLoad instanceof OperatorCost) {

			Runtime runtime = Runtime.getRuntime();

			// calculate potencial system load
			OperatorCost actSystemLoad2 = (OperatorCost) actSystemLoad;
			OperatorCost queryCost2 = (OperatorCost) queryCost;
			double cpu = actSystemLoad2.getCpuCost() + queryCost2.getCpuCost();
			double memFactor = (actSystemLoad2.getMemCost() + queryCost2.getMemCost()) / (double) runtime.totalMemory();

			// higher cost --> higher bid
			return cpu + memFactor;
		} else {
			return 1;
		}
	}

}
