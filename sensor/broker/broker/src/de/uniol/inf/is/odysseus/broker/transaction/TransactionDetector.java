/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.broker.transaction;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.exceptions.CyclicQueryException;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;

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
		// Do nothing here this method was used for the case
		// when physical plans using the same broker were attached
		// to each other. This will not be used anymore.
		
//		LoggerFactory.getLogger(TransactionDetector.class).debug("Reorganizing transactions");
//		int count = 0;
//		for (BrokerPO<?> broker : brokers) {
//			List<CycleSubscription> results = GraphUtils.findCycles(broker); 			
//			count = results.size();			
//			broker.reorganizeTransactions(results);			
//			this.visitedFollowing = new ArrayList<ILogicalOperator>();
//			this.visitedPreceeding = new ArrayList<ILogicalOperator>();
//		}				
//		LoggerFactory.getLogger(TransactionDetector.class).debug("Found " + count + " cycle(s) in physical plan");
				
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
				int i = 100;
				throw new CyclicQueryException("Found cyclic read transaction, but no corresponding queue port mapping. Forgotten QUEUE clause in query?");
			}
			
			return true;
		}
        if(this.visitedFollowing.contains(curNode)){
        	return false;
        }
        /* curNode is another broker. Cycles
         * over multiple brokers will not be considered
         */
        else if(curNode instanceof BrokerAO){
        	this.visitedFollowing.add(curNode);
        	return false;
        }
        else{
        	this.visitedFollowing.add(curNode);
        }
        
        boolean cycleDetected = false;
        for(LogicalSubscription sub: curNode.getSubscriptions()){
        	cycleDetected = cycleDetected || goToFollowingOps(sub.getTarget(), sub.getSinkInPort(), currentBroker, brokerOutPort);
        }
        return cycleDetected;
	}
	
	private boolean goToPrecedingOps(ILogicalOperator curNode, int curNodesOutPort, BrokerAO currentBroker, int brokerInPort){

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
				int i = 1000;
				throw new CyclicQueryException("Found cyclic write transaction, but no corresponding queue port mapping. Forgotten QUEUE clause in query?");
			}
			
			return true;
		}
        if(this.visitedPreceeding.contains(curNode)){
        	return false;
        }
        // curNode is another broker.
        // Cycles over multiple brokers will not
        // be considered
        else if(curNode instanceof BrokerAO){
        	this.visitedPreceeding.add(curNode);
        	return false;
        }
        else{
        	this.visitedPreceeding.add(curNode);
        }
        boolean cycleDetected = false;
        for(LogicalSubscription sub: curNode.getSubscribedToSource()){
        	cycleDetected = cycleDetected || goToPrecedingOps(sub.getTarget(), sub.getSourceOutPort(), currentBroker, brokerInPort);
        }
        return cycleDetected;
	}
}
