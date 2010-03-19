package de.uniol.inf.is.odysseus.broker.transformation;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class BrokerTransformationHelper {

	@SuppressWarnings("unchecked")
	public static void remapReadingPorts(BrokerPO<?> broker){			
		// make a copy to avoid concurrent modification exception
		ArrayList<PhysicalSubscription<?>> liste = new ArrayList<PhysicalSubscription<?>>();
		for(PhysicalSubscription<?> sub: broker.getSubscriptions()){
			liste.add(sub);
		}
		BrokerDictionary.getInstance().clearAllReadingPorts(broker.getIdentifier());
		ArrayList<PhysicalSubscription<?>> kopie = (ArrayList<PhysicalSubscription<?>>) liste.clone();		
		for(PhysicalSubscription<?> sub: kopie){							
			int nextPort = BrokerDictionary.getInstance().addNewTransaction(broker.getIdentifier(), ReadTransaction.Continuous);					
			broker.unsubscribeSink((ISink)sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());			
			broker.subscribeSink((ISink)sub.getTarget(), sub.getSinkInPort(), nextPort, sub.getSchema());
			
		}
	}
	
}
