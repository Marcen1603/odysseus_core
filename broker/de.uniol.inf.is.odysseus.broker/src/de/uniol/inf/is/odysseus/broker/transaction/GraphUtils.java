package de.uniol.inf.is.odysseus.broker.transaction;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class GraphUtils {
	/**
	 * Checks if there is a BrokerAO with same identifier
	 * @return true if there is a BrokerAO with same identifier 
	 */
	public static boolean isCyclic(ILogicalOperator top, String identifier) {
		return isCyclic(top, identifier, new ArrayList<String>());
	}

	private static boolean isCyclic(ILogicalOperator current, String identifier,
			List<String> visited) {		
		if(visited.contains(identifier)){
			return true;
		}else{
			if(current instanceof BrokerAO){
				BrokerAO broker = (BrokerAO)current;
				visited.add(broker.getIdentifier());
				if(broker.getIdentifier().equals(identifier)){
					return true;
				}				
			}
			boolean found = false;
			for(LogicalSubscription sub : current.getSubscribedToSource()){				
				if(isCyclic(sub.getTarget(), identifier)){
					found = true;
				}				
			}
			return found;
		}					
	}
	
	public static void findCycle(IPhysicalOperator current){
		findCycle(current, new ArrayList<IPhysicalOperator>(), new ArrayList<IPhysicalOperator>());
	}
	
	private static void findCycle(IPhysicalOperator current, List<IPhysicalOperator> visited, List<IPhysicalOperator> finished){
		System.err.println("--:"+current);
		if(finished.contains(current)){
			return;
		}
		if(visited.contains(current)){
			System.err.println("ZYKLUS GEFUNDEN!");
			return;
		}
		visited.add(current);
		if (current.isSink()) {
			for (PhysicalSubscription<?> sub : ((ISink<?>) current).getSubscribedToSource()) {
				findCycle((IPhysicalOperator) sub.getTarget(), visited, finished);				
			}
		}
		finished.add(current);

	}
	
	
	

}
