package de.uniol.inf.is.odysseus.broker.evaluation.transaction;

import java.util.List;

import de.uniol.inf.is.odysseus.broker.evaluation.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.evaluation.exceptions.CyclicQueryException;
import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;

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
		LoggerSystem.printlog(Accuracy.TRACE, "Broker: Reorganizing transactions...");
		int count = 0;
		for (BrokerPO<?> broker : brokers) {
			List<CycleSubscription> results = GraphUtils.findCycles(broker); 			
			count = results.size();			
			broker.reorganizeTransactions(results);			
			
		}				
		LoggerSystem.printlog(Accuracy.TRACE, "Broker: Found " + count + " cycle(s) in physical plan!");
				
	}
	
	
	//============================================
	// The following methods are used in the
	// PQL language. In this language a logical
	// plan can be created with cycles. So, there
	// transactions can already be identified
	// on the logical level
	public static void organizeTransactions(BrokerAO broker){
		// first check all reading transactions
		for(LogicalSubscription sub: broker.getSubscriptions()){
			boolean cycleDetected = goToFollowingOps(sub.getTarget(), sub.getSinkInPort(), broker, sub.getSourceOutPort());
			// if no cycle has been detected, the transaction is either one time reading (queue port mapping exists)
			// or continuous reading (no queue port mapping)
			if(!cycleDetected){
				boolean qpmFound = false;
				for(QueuePortMapping qpm: BrokerDictionary.getInstance().getQueuePortMappings(broker.getIdentifier())){
					if(qpm.getDataReadingPort()== sub.getSourceOutPort()){
						qpmFound = true;
						break;
					}
				}
				
				if(qpmFound){
					BrokerDictionary.getInstance().setReadTypeForPort(broker.getIdentifier(), sub.getSourceOutPort(), ReadTransaction.OneTime);
				}
				else{
					BrokerDictionary.getInstance().setReadTypeForPort(broker.getIdentifier(), sub.getSourceOutPort(), ReadTransaction.Continuous);
				}
			}
		}
		
		// second check all children
		for(LogicalSubscription sub: broker.getSubscribedToSource()){
			boolean cycleDetected = goToPrecedingOps(sub.getTarget(), sub.getSourceOutPort(), broker, sub.getSinkInPort());
			
			// if no cycle has been detected, the transaction is either timestamp (queue port mapping exists)
			// or continuous writing (no queue port mapping)
			if(!cycleDetected){
				boolean qpmFound = false;
				for(QueuePortMapping qpm: BrokerDictionary.getInstance().getQueuePortMappings(broker.getIdentifier())){
					// if there exists a queue port mapping with writing port
					// same as current sink in port, then this must be
					// timestamp input stream
					if(qpm.getQueueWritingPort() == sub.getSinkInPort()){
						qpmFound = true;
						break;
					}
				}
				
				if(qpmFound){
					BrokerDictionary.getInstance().setWriteTypeForPort(broker.getIdentifier(), sub.getSinkInPort(), WriteTransaction.Timestamp);
				}
				else{
					BrokerDictionary.getInstance().setWriteTypeForPort(broker.getIdentifier(), sub.getSinkInPort(), WriteTransaction.Continuous);
				}
			}
		}
	}
	
	/**
	 * This is a recursive method. It terminates, if no following operators occur any more or if the current broker
	 * is visited again.
	 * 
	 * @param curNode
	 * @param curNodesInPort
	 * @param currentBroker
	 * @param brokerOutPort
	 * 
	 * @return True, if a cycle has been detected, false otherwise
	 */
	private static boolean goToFollowingOps(ILogicalOperator curNode, int curNodesInPort, BrokerAO currentBroker, int brokerOutPort){
		if(curNode instanceof BrokerAO && ((BrokerAO)curNode).getIdentifier().equals(currentBroker.getIdentifier())){
			// cycle detected
			BrokerDictionary.getInstance().setReadTypeForPort(currentBroker.getIdentifier(), brokerOutPort, ReadTransaction.Cyclic);
			
			// check if QueuePortMapping exists
			// This should not be necessary, but
			// by this we can check if everything is
			// correct
			boolean found = false;
			for(QueuePortMapping qpm: BrokerDictionary.getInstance().getQueuePortMappings(currentBroker.getIdentifier())){
				if(qpm.getDataReadingPort() == brokerOutPort){
					found = true;
					break;
				}
			}
			
			if(!found){
				throw new CyclicQueryException("Found cyclic read transaction, but no corresponding queue port mapping. Forgotten QUEUE clause in query?");
			}
			
			return true;
		}
		else{
			boolean cycleDetected = false;
			for(LogicalSubscription sub: curNode.getSubscriptions()){
				cycleDetected = cycleDetected || goToFollowingOps(sub.getTarget(), sub.getSinkInPort(), currentBroker, brokerOutPort);
			}
			return cycleDetected;
		}
	}
	
	private static boolean goToPrecedingOps(ILogicalOperator curNode, int curNodesOutPort, BrokerAO currentBroker, int brokerInPort){
		if(curNode instanceof BrokerAO && ((BrokerAO)curNode).getIdentifier().equals(currentBroker.getIdentifier())){
			// cycle detected
			BrokerDictionary.getInstance().setWriteTypeForPort(currentBroker.getIdentifier(), brokerInPort, WriteTransaction.Cyclic);
			
			// check if QueuePortMapping exists for the
			// output port of the current node exists.
			// This should not be necessary, but
			// by this we can check, if everything is
			// correct
			boolean found = false;
			for(QueuePortMapping qpm: BrokerDictionary.getInstance().getQueuePortMappings(currentBroker.getIdentifier())){
				if(qpm.getDataReadingPort() == curNodesOutPort){
					found = true;
					break;
				}
			}
			
			if(!found){
				throw new CyclicQueryException("Found cyclic write transaction, but no corresponding queue port mapping. Forgotten QUEUE clause in query?");
			}
			
			return true;
		}
		else{
			boolean cycleDetected = false;
			for(LogicalSubscription sub: curNode.getSubscribedToSource()){
				cycleDetected = cycleDetected || goToPrecedingOps(sub.getTarget(), sub.getSourceOutPort(), currentBroker, brokerInPort);
			}
			return cycleDetected;
		}
	}
}
