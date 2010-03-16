package de.uniol.inf.is.odysseus.broker.transaction;

import java.util.List;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;

public class TransactionDetector {

	public static void reorganizeTransactions(List<BrokerPO<?>> brokers) {
		LoggerFactory.getLogger(TransactionDetector.class).debug("Reorganizing transactions");
		int count = 0;
		for (BrokerPO<?> broker : brokers) {
			List<CycleSubscription> results = GraphUtils.findCycles(broker); 			
			count = results.size();			
			broker.reorganizeTransactions(results);			
			
		}				
		LoggerFactory.getLogger(TransactionDetector.class).debug("Found " + count + " cycle(s) in physical plan");
				
	}
}
