package de.uniol.inf.is.odysseus.broker.transaction;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class GraphUtils {
	
	public static List<CycleSubscription> findCycles(IPhysicalOperator top){
		List<CycleSubscription> cycles = new ArrayList<CycleSubscription>();
		findCycles(top, new ArrayList<IPhysicalOperator>(), new ArrayList<IPhysicalOperator>(), cycles , 0, 0 );
		return cycles;
	}
	
	
	private static void findCycles(IPhysicalOperator current, List<IPhysicalOperator> visited, List<IPhysicalOperator> finished, List<CycleSubscription> cycles, int currentOutgoingPort, int currentIncomingPort){
		if(finished.contains(current)){
			return;
		}
		if(visited.contains(current)){		
			// cycle found			
			CycleSubscription cycle = new CycleSubscription(currentOutgoingPort, currentIncomingPort);		
			cycles.add(cycle);			
			return;
		}
		visited.add(current);
		if (current.isSink()) {			
			ISink<?> currentSink = (ISink<?>)current;			
			for (PhysicalSubscription<?> sub : currentSink.getSubscribedToSource()) {
				if(current instanceof BrokerPO<?>){
					currentIncomingPort = sub.getSinkInPort();
				}
				currentOutgoingPort = sub.getSourceOutPort();
				findCycles((IPhysicalOperator) sub.getTarget(), visited, finished, cycles, currentOutgoingPort, currentIncomingPort); 
			}		
		}
		finished.add(current);
	}
	
	
	
}
