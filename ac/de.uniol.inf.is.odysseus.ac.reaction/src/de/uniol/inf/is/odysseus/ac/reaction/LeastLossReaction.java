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
