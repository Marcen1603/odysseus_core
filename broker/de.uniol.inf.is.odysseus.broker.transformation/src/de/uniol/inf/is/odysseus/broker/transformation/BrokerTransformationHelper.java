package de.uniol.inf.is.odysseus.broker.transformation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class BrokerTransformationHelper {

	@SuppressWarnings("unchecked")
	public static void remapReadingPorts(BrokerPO<?> broker){			
		// make a copy to avoid concurrent modification exception
//		ArrayList<PhysicalSubscription<?>> liste = new ArrayList<PhysicalSubscription<?>>();
//		for(PhysicalSubscription<?> sub: broker.getSubscriptions()){
//			liste.add(sub);
//		}
//		BrokerDictionary.getInstance().removeAllReadingPorts(broker.getIdentifier());
//		ArrayList<PhysicalSubscription<?>> kopie = (ArrayList<PhysicalSubscription<?>>) liste.clone();		
//		for(PhysicalSubscription<?> sub: kopie){			
//			int nextPort = BrokerDictionary.getInstance().getNextReadPort(broker.getIdentifier());						
//			broker.unsubscribeSink((ISink)sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());			
//			broker.subscribeSink((ISink)sub.getTarget(), sub.getSinkInPort(), nextPort, sub.getSchema());			
//		}
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
		if (!visited.contains(physicalPO)) {
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
	
	
}
