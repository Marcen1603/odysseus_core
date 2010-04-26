package de.uniol.inf.is.odysseus.broker.transaction;

import java.util.List;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;

/**
 * The TransactionDetector finds new transaction types for all physical brokers and reorganize broker settings.
 * 
 * @author Dennis Geesen
 */
public class TransactionDetector {

	/**
	 * Finds new cycles and invokes each broker to reorganize his transaction types according to the new cycles.
	 *
	 * @param brokers a list of brokers
	 */
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
