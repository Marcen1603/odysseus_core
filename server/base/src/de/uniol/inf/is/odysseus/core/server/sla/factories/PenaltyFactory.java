/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.sla.factories;

import de.uniol.inf.is.odysseus.core.server.sla.Penalty;
import de.uniol.inf.is.odysseus.core.server.sla.penalty.AbsolutePenalty;
import de.uniol.inf.is.odysseus.core.server.sla.penalty.PercentagePenalty;

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
