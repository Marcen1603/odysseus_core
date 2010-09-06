package de.uniol.inf.is.odysseus.broker.transaction;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.exceptions.CyclicQueryException;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;

/**
 * The TransactionDetector finds new transaction types for all physical brokers and reorganize broker settings.
 * 
 * @author Dennis Geesen
 */
public class TransactionDetector {
	
	public ArrayList<ILogicalOperator> visitedPreceeding;
	public ArrayList<ILogicalOperator> visitedFollowing;
	
	public TransactionDetector(){
		this.visitedPreceeding = new ArrayList<ILogicalOperator>();
		this.visitedFollowing = new ArrayList<ILogicalOperator>();
	}

	/**
	 * Finds new cycles and invokes each broker to reorganize his transaction types according to the new cycles.
	 *
	 * @param brokers a list of brokers
	 */
	public void reorganizeTransactions(List<BrokerPO<?>> brokers) {
		LoggerFactory.getLogger(TransactionDetector.class).debug("Reorganizing transactions");
		int count = 0;
		for (BrokerPO<?> broker : brokers) {
			List<CycleSubscription> results = GraphUtils.findCycles(broker); 			
			count = results.size();			
			broker.reorganizeTransactions(results);			
			this.visitedFollowing = new ArrayList<ILogicalOperator>();
			this.visitedPreceeding = new ArrayList<ILogicalOperator>();
		}				
		LoggerFactory.getLogger(TransactionDetector.class).debug("Found " + count + " cycle(s) in physical plan");
				
	}
	
	
	//============================================
	// The following methods are used in the
	// PQL language. In this language a logical
	// plan can be created with cycles. So, there
	// transactions can already be identified
	// on the logical level
	public void organizeTransactions(BrokerAO broker){
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
	private boolean goToFollowingOps(ILogicalOperator curNode, int curNodesInPort, BrokerAO currentBroker, int brokerOutPort){
		if(this.visitedFollowing.contains(curNode)){
			return false;
		}
		else{
			this.visitedFollowing.add(curNode);
		}
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
	
	private boolean goToPrecedingOps(ILogicalOperator curNode, int curNodesOutPort, BrokerAO currentBroker, int brokerInPort){
		if(this.visitedPreceeding.contains(curNode)){
			return false;
		}
		else{
			this.visitedPreceeding.add(curNode);
		}
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
		// Hack: Fall mehrere Broker im Plan vorkommen, kann es passieren, dass man von einem Broker in den Zyklus eines anderen Brokers
		// läuft. In diesem Fall entsteht ein StackOverflow.
		// Die folgende Bedingung verhindert das, erlaubt es aber auch nicht mehr, dass ein Zyklus aus mehreren Brokern besteht (braucht man das?)
		// Folgendes ist also nicht mehr möglich:
		// Broker(a) <- Op <- Op <- Broker(b) <- Op <- Op <- Broker(a)
		else{
			boolean cycleDetected = false;
			for(LogicalSubscription sub: curNode.getSubscribedToSource()){
				cycleDetected = cycleDetected || goToPrecedingOps(sub.getTarget(), sub.getSourceOutPort(), currentBroker, brokerInPort);
			}
			return cycleDetected;
		}
	}
}
