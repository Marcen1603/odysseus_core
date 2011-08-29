package de.uniol.inf.is.odysseus.ac.reaction;

import java.util.List;

import de.uniol.inf.is.odysseus.ac.IAdmissionReaction;
import de.uniol.inf.is.odysseus.ac.IPossibleExecution;
import de.uniol.inf.is.odysseus.costmodel.ICost;

public class LeastLossReaction implements IAdmissionReaction {

	@Override
	public IPossibleExecution react(List<IPossibleExecution> possibilities) {
		
		IPossibleExecution least = null;
		ICost leastCost = null;
		
		for( IPossibleExecution poss : possibilities ) {
			if( least == null ) {
				least = poss;
				leastCost = poss.getCostEstimation();
				continue;
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
