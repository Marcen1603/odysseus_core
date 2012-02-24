package de.uniol.inf.is.odysseus.core.server.sla.factories;

import de.uniol.inf.is.odysseus.core.server.sla.penalty.AbsolutePenalty;
import de.uniol.inf.is.odysseus.core.server.sla.penalty.PercentagePenalty;
import de.uniol.inf.is.odysseus.core.sla.Penalty;

public class PenaltyFactory {
	
	public static final String ABSOLUTE_PENALTY = "absolute";
	public static final String PERCENTAGE_PENALTY = "percentage";

	public Penalty buildPenalty(String penaltyID, double value) {
		if (ABSOLUTE_PENALTY.equals(penaltyID)) {
			return new AbsolutePenalty(value);
		} else if (PERCENTAGE_PENALTY.equals(penaltyID)) {
			return new PercentagePenalty(value);
		} else {
			throw new RuntimeException("unknown penalty type: " + penaltyID);
		}
		
	}
	
}
