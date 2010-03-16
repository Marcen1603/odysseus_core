package de.uniol.inf.is.odysseus.broker.transformation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class BrokerTransformationHelper {

	@SuppressWarnings("unchecked")
	public static void remapReadingPorts(BrokerPO<?> broker){	
		System.err.println("************* BEGIN **************");
		System.err.println(planToString(broker, ""));
		System.err.println("**********************************");
		// make a copy to avoid concurrent modification exception
		ArrayList<PhysicalSubscription<?>> liste = new ArrayList<PhysicalSubscription<?>>();
		for(PhysicalSubscription<?> sub: broker.getSubscriptions()){
			liste.add(sub);
		}
		BrokerDictionary.getInstance().removeAllReadingPorts(broker.getIdentifier());
		ArrayList<PhysicalSubscription<?>> kopie = (ArrayList<PhysicalSubscription<?>>) liste.clone();		
		for(PhysicalSubscription<?> sub: kopie){			
			int nextPort = BrokerDictionary.getInstance().getNextReadPort(broker.getIdentifier());
			System.err.println("Remap port of "+sub.getTarget()+" from "+sub.getSourceOutPort()+" to "+nextPort+" (Out:"+sub.getSinkInPort()+")");
			for(PhysicalSubscription<?> sub2: broker.getSubscriptions()){
				System.err.println("START: "+sub2);
			}			
			broker.unsubscribeSink((ISink)sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
			for(PhysicalSubscription<?> sub2: broker.getSubscriptions()){
				System.err.println("MITTE: "+sub2);
			}
			broker.subscribeSink((ISink)sub.getTarget(), sub.getSinkInPort(), nextPort, sub.getSchema());
			
			for(PhysicalSubscription<?> sub2: broker.getSubscriptions()){
				System.err.println("ENDE: "+sub2);
			}
		}
	}
	
	
	public static String planToString(IPhysicalOperator physicalPO, String indent) {
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		if (physicalPO.isSource()) {
			for (PhysicalSubscription<?> sub : ((ISource<?>) physicalPO)
					.getSubscriptions()) {				
				builder.append(sub);
				builder.append(" [");
				String sep = "";
				for(PhysicalSubscription<?> ne : ((ISource<?>)sub.getTarget()).getSubscriptions()){
					builder.append(sep+ne);
					sep=" |";
				}
				builder.append("]");
				builder.append('\n');
				
			}
			indent = "  " + indent;
		}
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
