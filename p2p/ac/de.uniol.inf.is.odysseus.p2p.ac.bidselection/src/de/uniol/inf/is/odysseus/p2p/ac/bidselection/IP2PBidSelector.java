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
