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
package de.uniol.inf.is.odysseus.p2p.ac.bidselection.standard;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.ac.bidselection.IP2PBidSelector;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;

/**
 * Standardimplementierung der Schnittstelle {@link IP2PBidSelector}. Es wird
 * das Gebot mit dem niedrigsten Gebot ausgewählt. Dieser Selector sollte nur im
 * Zusammenhang mit StandardP2PGenerator verwendet werden.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardP2PBidSelector implements IP2PBidSelector {

	@Override
	public Bid selectBid(List<Bid> bids) {
		// 1. Are there positiv bids?
		List<Bid> validBids = new ArrayList<Bid>();
		for (Bid b : bids) {
			int bidValue = b.getBidValue();

			if (bidValue >= 0) {
				validBids.add(b);
			}
		}

		if (!validBids.isEmpty()) {
			int lowestBidValue = Integer.MAX_VALUE;
			Bid lowestBid = null;
			for (Bid b : validBids) {
				int val = b.getBidValue();
				if (val < lowestBidValue) {
					lowestBidValue = val;
					lowestBid = b;
				}
			}

			return lowestBid;

		}
        return null;
	}

}
