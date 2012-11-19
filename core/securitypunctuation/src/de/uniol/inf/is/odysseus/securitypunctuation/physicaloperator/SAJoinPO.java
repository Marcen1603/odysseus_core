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
package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.BinarySecurityPunctuationCache;

public class SAJoinPO<K extends ITimeInterval, T extends IStreamObject<K>> extends JoinTIPO<K, T> {
	
    private static Logger LOG = LoggerFactory.getLogger(SAJoinPO.class);

	private BinarySecurityPunctuationCache spCache = new BinarySecurityPunctuationCache();
	
	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		if(port >= 0 && port <= 1) {
			spCache.add(sp, port);
		}
	}

	@Override
	protected void process_next(T object, int port) {
		
		LOG.debug("process_next - Port: " + port + " - Objekt: " + object);
//		LOG.debug("areas[port].size(): " + areas[port].size());
//		LOG.debug("areas[otherport].size(): " + areas[port^1].size());

		if (isDone()) {
			// TODO bei den sources abmelden ?? MG: Warum??
			// propagateDone gemeint?
			// JJ: weil man schon fertig sein
			// kann, wenn ein strom keine elemente liefert, der
			// andere aber noch, dann muss man von dem anderen keine
			// eingaben mehr verarbeiten, was dazu fuehren kann,
			// dass ein kompletter teilplan nicht mehr ausgefuehrt
			// werden muss, man also ressourcen spart
			return;
		}
		if (!isOpen()) {
//			getLogger().error(
//					"process next called on non opened operator " + this
//							+ " with " + object + " from " + port);
			return;
		}
		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		synchronized (this.areas[otherport]) {
			areas[otherport].purgeElements(object, order);
		}

		synchronized (this) {
			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (isDone()) {
				propagateDone();
				return;
			}
		}
		Iterator<T> qualifies;
		synchronized (this.areas) {
			synchronized (this.areas[otherport]) {
				qualifies = areas[otherport].queryCopy(object, order);
			}
			transferFunction.newElement(object, port);
			synchronized (areas[port]) {
				areas[port].insert(object);
			}
		}

		//here begin the changes for the SAJoin
		while (qualifies.hasNext()) {
			T next = qualifies.next();
			
			LOG.debug("qualifies: " + next);
			
			ISecurityPunctuation sp = spCache.getMatchingSP(object.getMetadata().getStart().getMainPoint(), port);
			ISecurityPunctuation otherSP = spCache.getMatchingSP(next.getMetadata().getStart().getMainPoint(), otherport);
			
			if(sp != null && otherSP != null) {
				Long newTS = next.getMetadata().getStart().getMainPoint();
				if(newTS.compareTo(object.getMetadata().getStart().getMainPoint()) < 0) {
					newTS = object.getMetadata().getStart().getMainPoint();
				} 				
				ISecurityPunctuation newSP = sp.intersect(otherSP, newTS);
				if(newSP != null && !newSP.isEmpty()) {
					T newElement =  dataMerge.merge(object, next, metadataMerge, order);	
					//Tupel mit komplett auseinander liegenden Intervallen können nicht gemerged werden!!!
					//Dann wäre getStart() == null...
					if(newElement.getMetadata() != null) { 				
						transferSecurityPunctuation(newSP);
						transferFunction.transfer(newElement);						
						
						LOG.debug("transfered Datatuple: " + newElement);
						LOG.debug("transfered SP: " + newSP);
					} 
				} 
			} 
		}
		
		Long minTS = null;
		if(!areas[port].isEmpty()) {
			if(!areas[otherport].isEmpty()) {
				if(areas[port].getMinTs().compareTo(areas[otherport].getMinTs()) <= 0) {
					minTS = areas[port].getMinTs().getMainPoint();
				} else {
					minTS = areas[otherport].getMinTs().getMainPoint();
				}	
			} else {
				minTS = areas[port].getMinTs().getMainPoint();
			}
		} else {
			if(!areas[otherport].isEmpty()) {
				minTS = areas[otherport].getMinTs().getMainPoint();
			}
		}
		if(minTS != null) {
			spCache.cleanCache(minTS, port);
		}
	}
	
	@Override
	public String getName() {
		return "SAJoin";
	}
}
