package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea.Order;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.BinarySecurityPunctuationCache;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.SecurityPunctuationCache;

public class SAJoinPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>> extends JoinTIPO<K, T> {
	
    private static Logger LOG = LoggerFactory.getLogger(SAJoinPO.class);

	private BinarySecurityPunctuationCache spCache = new BinarySecurityPunctuationCache();
	
	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
//		this.transferSecurityPunctuation(sp);
		if(port >= 0 && port <= 1) {
			spCache.add(sp, port);
		}
		//nicht transferSecurityPunctuation()!!!
	}

	@Override
	protected void process_next(T object, int port) {
		
		LOG.debug("process_next - Port: " + port + " - Objekt: " + object);
		LOG.debug("areas[port].size(): " + areas[port].size());
		LOG.debug("areas[otherport].size(): " + areas[port^1].size());
		
		if(object.getMetadata().getStart().getMainPoint() > 55) {
			System.out.println("");
		}
		
//		if(counter++ >= 20) {
//			System.out.println("");
//		}


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
//			areas[otherport].purgeElements(object, order);
			
			//behelfsmäßiges Sliding-Window...
//			Long point = object.getMetadata().getStart().getMainPoint();
//			areas[otherport].purgeElementsBefore(new PointInTime(point - 20));
			areas[otherport].purgeElementsBefore(object.getMetadata().getStart());
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

		while (qualifies.hasNext()) {
			T next = qualifies.next();
			
			//Beide SP mergen!
			ISecurityPunctuation sp = spCache.getMatchingSP(object.getMetadata().getStart().getMainPoint(), port);
			ISecurityPunctuation otherSP = spCache.getMatchingSP(next.getMetadata().getStart().getMainPoint(), otherport);
			if(sp != null && otherSP != null) {
				
//				LOG.debug("sp != null && otherSP != null");
				
				ISecurityPunctuation newSP = sp.intersect(otherSP);
				if(newSP != null && !newSP.isEmpty()) {
					T newElement = merge(object, next, order);	
					//Tupel mit komplett auseinander liegenden Intervallen können nicht gemerged werden!!!
					//Dann wäre getStart() == null...
					if(newElement.getMetadata() != null) { 
//						if(newElement.getMetadata().getStart() != null) { 
						transferFunction.transfer(newElement);						
	//					LOG.debug("transferFunction.transfer(newElement): " + newElement);						
						transferSecurityPunctuation(newSP);
					}
				} else {
					if(newSP.isEmpty()) {
						LOG.debug("NewSP is empty!!!");
					}
				}
			}	
		}
		
//		printAreas();
		
		// Wann können SP gelöscht werden???
		if(areas[otherport].getMinTs() != null) {
//			LOG.debug("areas[otherport].getMinTs(): " + areas[otherport].getMinTs().getMainPoint());
//			LOG.debug("transferFunction.getMinTs().getMainPoint(): " + transferFunction.getMinTs().getMainPoint());
			spCache.cleanCache(areas[otherport].getMinTs().getMainPoint(), port);
			LOG.debug("spCache.size(otherport) after clean(): " + spCache.size(otherport));
			LOG.debug("spCache.size(port) after clean(): " + spCache.size(port));
			LOG.debug("areas[otherport].getMinTs(): " + areas[otherport].getMinTs());
			LOG.debug("areas[otherport].getMaxTs(): " + areas[otherport].getMaxTs());
			LOG.debug("areas[port].getMinTs(): " + areas[port].getMinTs());
			LOG.debug("areas[port].getMaxTs(): " + areas[port].getMaxTs());
//			spCache.printCache();
		}
	}
	
	@Override
	public String getName() {
		return "SAJoin";
	}
	
	private void printAreas() {
//		LOG.debug("");
//		LOG.debug("areas[0]:");
//		for(T lala:areas[0]) {
//			LOG.debug(lala.toString());
//		}
//		LOG.debug("areas[1]:");
//		for(T lala:areas[1]) {
//			LOG.debug(lala.toString());
//		}
//		LOG.debug("");
	}
}
