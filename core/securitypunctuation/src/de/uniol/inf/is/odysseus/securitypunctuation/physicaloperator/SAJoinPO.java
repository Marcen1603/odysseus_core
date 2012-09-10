package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea.Order;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.SecurityPunctuationCache;

public class SAJoinPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>> extends JoinTIPO<K, T> {
	
    private static Logger LOG = LoggerFactory.getLogger(SAJoinPO.class);

	private SecurityPunctuationCache[] spCaches = new SecurityPunctuationCache[2];

//	private Integer counter = 0;
	
	public SAJoinPO(IDataMergeFunction<T> dataMerge,
			IMetadataMergeFunction<K> metadataMerge,
			ITransferArea<T, T> transferFunction,
			ITimeIntervalSweepArea<T>[] areas) {
		super(dataMerge, metadataMerge, transferFunction, areas);
		spCaches[0] = new SecurityPunctuationCache();
		spCaches[1] = new SecurityPunctuationCache();
	}

	public SAJoinPO() {
		super();
		spCaches[0] = new SecurityPunctuationCache();
		spCaches[1] = new SecurityPunctuationCache();
	}

	public SAJoinPO(SAJoinPO<K, T> join) {
		super(join);
		spCaches[0] = new SecurityPunctuationCache();
		spCaches[1] = new SecurityPunctuationCache();
	}
	
	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
//		this.transferSecurityPunctuation(sp);
		if(port >= 0 && port <= 1) {
			spCaches[port].add(sp);
		}
		//nicht transferSecurityPunctuation()!!!
	}

	@Override
	protected void process_next(T object, int port) {
		
		LOG.debug("process_next: " + object + " - Port: " + port);
		
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

		while (qualifies.hasNext()) {
			T next = qualifies.next();
			
			//Beide SP mergen!
			ISecurityPunctuation sp = spCaches[port].getMatchingSP(object.getMetadata().getStart().getMainPoint());
			ISecurityPunctuation otherSP = spCaches[otherport].getMatchingSP(next.getMetadata().getStart().getMainPoint());
			if(sp != null && otherSP != null) {
				ISecurityPunctuation newSP = sp.intersect(otherSP);
				if(newSP != null && !newSP.isEmpty()) {
					T newElement = merge(object, next, order);			
					transferFunction.transfer(newElement);
					transferSecurityPunctuation(newSP);
				}		
			}	
		}
		// Wann können SP gelöscht werden???
//		spCaches[otherport].cleanCache(object.getMetadata().getStart().getMainPoint());
	}
	
	@Override
	public String getName() {
		return "SAJoin";
	}
}
