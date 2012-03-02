package de.uniol.inf.is.odysseus.ac.reaction;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionReaction;
import de.uniol.inf.is.odysseus.core.server.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;

/**
 * Auswahl eines Vorschlags für die Auflösung einer Überlastung.
 * In dieser Klasse wird genau der Vorschlag ausgewählt, welches
 * die höchsten Kosten verursacht. Die höchsten Kosten sind noch unter
 * den Maximalkosten, sodass diese den geringsten Verlust an Leistung
 * verspricht.
 * 
 * @author Timo Michelsen
 *
 */
public class LeastLossReaction implements IAdmissionReaction {

	@Override
	public IPossibleExecution react(List<IPossibleExecution> possibilities) {
		
		IPossibleExecution least = null;
		ICost leastCost = null;
		
		for( IPossibleExecution poss : possibilities ) {
			if( least == null ) {
				least = poss;
				leastCost = poss.getCostEstimation();
			} else {
				int cmp = poss.getCostEstimation().compareTo(leastCost);
				if( cmp < 0 ) {
					least = poss;
					leastCost = poss.getCostEstimation();
				}
			}
		}
		
		return least;
	}

}
