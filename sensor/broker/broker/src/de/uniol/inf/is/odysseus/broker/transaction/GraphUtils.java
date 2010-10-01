package de.uniol.inf.is.odysseus.broker.transaction;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;

/**
 * GraphUtils provides several algorithms for analyzing an operator graph.
 * 
 * @author Dennis Geesen
 */
public class GraphUtils {

	/**
	 * Finds cycles in a graph.
	 *
	 * @param top the top operator where to search for cyclic subscriptions 
	 * @return a list of cycles
	 */
	public static List<CycleSubscription> findCycles(IPhysicalOperator top) {
		List<CycleSubscription> cycles = new ArrayList<CycleSubscription>();
		findCycles(top, new ArrayList<IPhysicalOperator>(), new ArrayList<IPhysicalOperator>(), cycles, 0, 0);
		return cycles;
	}

	/**
	 * Finds cycles in a graph
	 *
	 * @param current the current operator while searching
	 * @param visited a list of all visited operators
	 * @param finished a list of all finished operators
	 * @param cycles a list of all found cycles
	 * @param currentOutgoingPort the current outgoing port from the top operator
	 * @param currentIncomingPort the current incoming port from the top operator
	 */
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

	/**
	 * Determines all logical brokers from which the given top operator is (transitively) reading.
	 *
	 * @param top the top to look for
	 * @return a list of all found brokers 
	 */
	public static List<BrokerAO> getReadingFromBrokers(ILogicalOperator top){
		List<BrokerAO> readingFromBrokers = new ArrayList<BrokerAO>();
		getReadingFromBrokers(top, new ArrayList<ILogicalOperator>(), readingFromBrokers);
		return readingFromBrokers;
	}
	
	/**
	 * Determines all brokers the current operator is (transitively) reading from.
	 * The results are saved in the readingFrom list
	 *
	 * @param current the current operator while searching
	 * @param visited a list of all visited operators
	 * @param readingFrom a list of all found brokers
	 */
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
	
	/**
	 * Converts a physical operator graph into a string
	 *
	 * @param physicalPO the physical operator to start from
	 * @param indent the indent
	 * @return the string
	 */
	public static String planToString(IPhysicalOperator physicalPO, String indent) {
		StringBuilder builder = new StringBuilder();
		builder.append(indent);		
		builder.append(planToString(physicalPO, indent,
				new ArrayList<IPhysicalOperator>()));
		return builder.toString();
	}

	/**
	 * Converts a physical operator graph into a string
	 *
	 * @param physicalPO the current physical operator 
	 * @param indent the current indent
	 * @param visited a list of all visited operators
	 * @return the result string
	 */
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
	
	/**
	 * Proves if an object is part of the given list.
	 * In contrast to the normal method contains which is based on equal, 
	 * this method will prove if it is really the same object by using ==. 
	 *
	 * @param list the list
	 * @param op the object to prove
	 * @return true, if successful
	 */
	private static boolean contains(List<?> list, Object op){
		for(Object other : list){
			if(other==op){
				return true;
			}
		}
		return false;
	}

}
