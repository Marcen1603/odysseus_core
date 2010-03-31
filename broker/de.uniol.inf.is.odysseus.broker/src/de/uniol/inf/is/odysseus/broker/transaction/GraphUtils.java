package de.uniol.inf.is.odysseus.broker.transaction;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class GraphUtils {

	public static List<CycleSubscription> findCycles(IPhysicalOperator top) {
		List<CycleSubscription> cycles = new ArrayList<CycleSubscription>();
		findCycles(top, new ArrayList<IPhysicalOperator>(), new ArrayList<IPhysicalOperator>(), cycles, 0, 0);
		return cycles;
	}

	private static void findCycles(IPhysicalOperator current, List<IPhysicalOperator> visited, List<IPhysicalOperator> finished, List<CycleSubscription> cycles, int currentOutgoingPort, int currentIncomingPort) {
		if (contains(finished, current)) {
			return;
		}
		if (contains(visited, current)) {
			// cycle found
			CycleSubscription cycle = new CycleSubscription(currentOutgoingPort, currentIncomingPort);
			cycles.add(cycle);
			return;
		}
		visited.add(current);
		if (current.isSink()) {
			ISink<?> currentSink = (ISink<?>) current;
			for (PhysicalSubscription<?> sub : currentSink.getSubscribedToSource()) {
				if (current instanceof BrokerPO<?>) {
					currentIncomingPort = sub.getSinkInPort();
				}
				currentOutgoingPort = sub.getSourceOutPort();
				findCycles((IPhysicalOperator) sub.getTarget(), visited, finished, cycles, currentOutgoingPort, currentIncomingPort);
			}
		}
		finished.add(current);
	}

	public static List<BrokerAO> getReadingFromBrokers(ILogicalOperator top){
		List<BrokerAO> readingFromBrokers = new ArrayList<BrokerAO>();
		getReadingFromBrokers(top, new ArrayList<ILogicalOperator>(), readingFromBrokers);
		return readingFromBrokers;
	}
	
	private static void getReadingFromBrokers(ILogicalOperator current, List<ILogicalOperator> visited, List<BrokerAO> readingFrom) {		
		if (!contains(visited, current)) {
			visited.add(current);
			if (current instanceof BrokerAO) {					
				readingFrom.add((BrokerAO)current);					
			}
			for (LogicalSubscription sub : current.getSubscribedToSource()) {				
				getReadingFromBrokers(sub.getTarget(), visited, readingFrom);				
			}
		}		
	}
	
	public static String planToString(IPhysicalOperator physicalPO, String indent) {
		StringBuilder builder = new StringBuilder();
		builder.append(indent);		
		builder.append(planToString(physicalPO, indent,
				new ArrayList<IPhysicalOperator>()));
		return builder.toString();
	}

	private static String planToString(IPhysicalOperator physicalPO, String indent,
			List<IPhysicalOperator> visited) {
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		if (!contains(visited,physicalPO)) {
			visited.add(physicalPO);
			builder.append(physicalPO);
			builder.append('\n');
			if (physicalPO.isSink()) {
				for (PhysicalSubscription<?> sub : ((ISink<?>) physicalPO)
						.getSubscribedToSource()) {
					builder.append(planToString((IPhysicalOperator) sub
							.getTarget(), "  " + indent, visited));
				}
			}
		}else{
			builder.append(physicalPO);
			builder.append('\n');
			builder.append(indent+"  [see above for following operators]\n");
		}
		return builder.toString();
	}
	
	private static boolean contains(List<?> list, Object op){
		for(Object other : list){
			if(other==op){
				return true;
			}
		}
		return false;
	}

}
